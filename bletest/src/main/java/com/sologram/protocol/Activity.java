package com.sologram.protocol;

import android.util.Log;

import com.sologram.bluetooth.NoAddress;
import com.sologram.bluetooth.NotReady;
import com.sologram.bluetooth.Role;

public class Activity extends android.app.Activity implements Role.Listener {
	static private final String TAG = Activity.class.getSimpleName();

	private Role master;

	protected void connect(String address) {
		Log.w(TAG, "connect: " + address);
		try {
			if (master != null) {
				master.close();
				master = null;
			}
			if (address != null)
				master = new Master(this, address, this);
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
		Log.w(TAG, "onConnected: " + address);
	}

	@Override
	public void onDisconnected(String address) {
		Log.w(TAG, "onDisconnected: " + address);
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
