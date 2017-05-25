package com.sologram.bluetooth;

/**
 * Created by hans on 2016/1/19.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

public class Central extends BluetoothGattCallback implements Role {
	static private final String TAG = Central.class.getSimpleName();

	private BluetoothDevice dev;
	protected BluetoothGatt gatt;
	protected Activity handler;

	public Central(Activity activity, String address) throws NoAddress, NotReady {
		if (address == null)
			throw new NoAddress();
		handler = activity;
		BluetoothAdapter a = Adapter.get(activity);
		if (a == null || !a.isEnabled())
			throw new NotReady();
		dev = a.getRemoteDevice(address);
		gatt = dev.connectGatt(activity, false, this);
	}

	@Override
	public void close() {
		gatt.close();
		dev = null;
	}

	@Override
	public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
		Log.v(TAG, "onConnectionStateChange: " + status + ", " + newState);
		super.onConnectionStateChange(gatt, status, newState);
		switch (newState) {
			case BluetoothProfile.STATE_CONNECTED:
				gatt.discoverServices();
				handler.onConnected(gatt.getDevice().getAddress());
				break;
			case BluetoothProfile.STATE_DISCONNECTED:
				this.gatt.close();
				if (dev != null) {
					handler.onDisconnected(gatt.getDevice().getAddress());
					this.gatt = dev.connectGatt(handler, false, this);
				}
		}
	}
}
