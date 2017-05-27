package com.sologram.protocol;

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

public class Activity extends android.app.Activity implements Role.Listener {
	static private final String TAG = Activity.class.getSimpleName();

	private Role master;
	private String macadd;

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
	public void onBits(byte[] bits) {
	}

	@Override
	public void onConnected(String address) {
	}

	@Override
	public void onDisconnected(String address) {
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (master != null) {
			master.close();
			master = null;
		}
	}
}
