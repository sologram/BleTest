package com.sologram.bletest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sologram.bluetooth.NotReady;
import com.sologram.bluetooth.Scanner;
import com.sologram.common.PopupMenu;

import java.util.List;
import java.util.UUID;

public class Activity extends com.sologram.protocol.Activity
		implements PopupMenu.Listener, View.OnClickListener, Scanner.Listener {
	static private final String TAG = Activity.class.getSimpleName();

	private PopupMenu menu;
	private Scanner scanner;
	private String macadd;
	private TextView addr;

	@Override
	public void onBits(byte[] bits) {
	}

	@Override
	public void onClick(View v) {
		try {
			disconnect();
			menu = new PopupMenu(this, "Scan ...");
			menu.setListener(this);
			menu.show(v);
			scanner = new Scanner(this).start(null, this);
		} catch (NotReady notReady) {
			notReady.printStackTrace();
		}
	}

	@Override
	public void onConnected(final String address) {
		final String a = address;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				addr.setText(a);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		addr = (TextView) findViewById(R.id.addr);
		addr.setOnClickListener(this);
	}

	@Override
	public void onDisconnected(String address) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				addr.setText("Disconnected");
			}
		});
	}

	@Override
	public void onDismiss() {
		Log.w(TAG, "onDismiss");
		menu = null;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (scanner != null) {
					scanner.stop();
					scanner = null;
				}
				connect(macadd);
			}
		});
	}

	@Override
	public void onFound(final String address, final String name, List<UUID> uuids) {
		//Log.w(TAG, "onFound: " + address);
		if (menu != null) {
			final String a = address;
			final String n = name;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (menu != null)
						menu.addItem(a, n == null ? a : n);
				}
			});
		}
	}

	@Override
	public void onItemClick(Object key, String text) {
		macadd = (String) key;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (scanner != null) {
			scanner.stop();
			scanner = null;
		}
		if (menu != null) {
			menu.dismiss();
			menu = null;
		}
	}
}
