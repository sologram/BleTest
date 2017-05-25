package com.sologram.bletest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sologram.bluetooth.NotReady;
import com.sologram.bluetooth.Role;
import com.sologram.bluetooth.Scanner;
import com.sologram.common.PopupMenu;

import java.util.List;
import java.util.UUID;

public class Activity extends Role.Activity implements PopupMenu.Listener, View.OnClickListener,
		Scanner.Listener {
	static private final String TAG = Activity.class.getSimpleName();

	PopupMenu menu;
	Scanner scanner;
	View hello;

	@Override
	protected void onBits(byte[] bits) {
	}

	@Override
	public void onClick(View v) {
		try {
			scanner = new Scanner(this).start(null);
			scanner.setListener(this);
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
		hello = findViewById(R.id.hello);
		hello.setOnClickListener(this);
	}

	@Override
	protected void onDisconnected(String address) {
	}

	@Override
	public void onDismiss() {
		Log.w(TAG, "onDismiss");
		if (scanner != null) {
			scanner.stop();
			scanner = null;
		}
		menu = null;
	}

	@Override
	public void onFound(final String address, final String name, List<UUID> uuids) {
		if (menu != null) {
			final String a = address;
			final String n = name;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					menu.addItem(a, n == null ? a : n);
				}
			});
		}
	}

	@Override
	public void onItemClick(Object key, String text) {
	}
}
