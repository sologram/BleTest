package com.sologram.bletest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sologram.bluetooth.Master;
import com.sologram.bluetooth.NoAddress;
import com.sologram.bluetooth.NotReady;
import com.sologram.bluetooth.Role;
import com.sologram.bluetooth.Scanner;
import com.sologram.common.PopupMenu;

import java.util.List;
import java.util.UUID;

public class Activity extends Role.Activity implements PopupMenu.Listener, View.OnClickListener,
		Scanner.Listener {
	static private final String TAG = Activity.class.getSimpleName();

	private PopupMenu menu;
	private Role master;
	private Scanner scanner;
	private String macadd;
	private View addr;

	protected void connect(String address) {
		Log.w(TAG, "connect: " + address);
		try {
			if (master != null)
				master.close();
			master = new Master(this, address);
		} catch (NotReady notReady) {
			notReady.printStackTrace();
		} catch (NoAddress noAddress) {
			noAddress.printStackTrace();
		}
	}

	protected void disconnect() {
		Log.w(TAG, "disconnect");
		if (master != null)
			master.close();
	}

	@Override
	protected void onBits(byte[] bits) {
	}

	@Override
	public void onClick(View v) {
		try {
			disconnect();
			scanner = new Scanner(this).start(null, this);
			menu = new PopupMenu(this, "Scan ...");
			menu.setListener(this);
			menu.show(v);
		} catch (NotReady notReady) {
			notReady.printStackTrace();
		}
	}

	@Override
	protected void onConnected(String address) {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		addr = findViewById(R.id.addr);
		addr.setOnClickListener(this);
	}

	@Override
	protected void onDisconnected(String address) {
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
		if (master != null) {
			master.close();
			master = null;
		}
		if (scanner != null) {
			scanner.stop();
			scanner = null;
		}
	}
}
