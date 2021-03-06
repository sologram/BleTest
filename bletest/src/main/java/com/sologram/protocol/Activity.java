package com.sologram.protocol;

import android.util.Log;

import com.sologram.bluetooth.NoAddress;
import com.sologram.bluetooth.NotReady;
import com.sologram.bluetooth.Role;

public abstract class Activity extends android.app.Activity implements Master.Listener {
	static private final String TAG = Activity.class.getSimpleName();

	private Role master;

	protected void connect(String address) {
		Log.v(TAG, "connect: " + address);
		try {
			if (master != null) {
				master.close();
				master = null;
			}
			if (address != null)
				master = new Master(this, address, this);
		} catch (NotReady | NoAddress e) {
			e.show(this);
		}
	}

	protected void disconnect() {
		Log.v(TAG, "disconnect");
		if (master != null)
			master.close();
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
