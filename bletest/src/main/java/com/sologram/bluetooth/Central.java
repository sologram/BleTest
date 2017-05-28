package com.sologram.bluetooth;

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
	private Context context;
	protected BluetoothGatt gatt;
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
		if (listener != null) {
			listener.onDisconnected(gatt.getDevice().getAddress());
			listener = null;
		}
		if (gatt != null) {
			gatt.close();
			gatt = null;
		}
		if (device != null)
			device = null;
	}

	@Override
	public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
		Log.v(TAG, "onConnectionStateChange: " + status + ", " + newState);
		super.onConnectionStateChange(gatt, status, newState);
		switch (newState) {
			case BluetoothProfile.STATE_CONNECTED:
				if (listener != null)
					listener.onConnected(gatt.getDevice().getAddress());
				gatt.discoverServices();
				break;
			case BluetoothProfile.STATE_DISCONNECTED:
				if (listener != null)
					listener.onDisconnected(gatt.getDevice().getAddress());
				gatt.close();
				this.gatt = device.connectGatt(context, false, this);
		}
	}
}
