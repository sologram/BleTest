package com.sologram.bluetooth;

/**
 * Created by hans on 2016/1/19.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

public class Central extends BluetoothGattCallback implements Role {
	static private final String TAG = Central.class.getSimpleName();

	private BluetoothDevice device;
	protected BluetoothGatt gatt;
	protected Context context;
	protected Role.Listener listener;

	public Central(Context context, String address, Role.Listener listener)
			throws NoAddress, NotReady {
		if (address == null)
			throw new NoAddress();
		BluetoothAdapter a = Adapter.get(context);
		if (a == null || !a.isEnabled())
			throw new NotReady();
		this.context = context;
		this.listener = listener;
		this.device = a.getRemoteDevice(address);
		this.gatt = device.connectGatt(context, false, this);
	}

	@Override
	public void close() {
		gatt.close();
		device = null;
	}

	@Override
	public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
		Log.v(TAG, "onConnectionStateChange: " + status + ", " + newState);
		super.onConnectionStateChange(gatt, status, newState);
		switch (newState) {
			case BluetoothProfile.STATE_CONNECTED:
				gatt.discoverServices();
				listener.onConnected(gatt.getDevice().getAddress());
				break;
			case BluetoothProfile.STATE_DISCONNECTED:
				this.gatt.close();
				if (device != null) {
					listener.onDisconnected(gatt.getDevice().getAddress());
					this.gatt = device.connectGatt(context, false, this);
				}
		}
	}
}
