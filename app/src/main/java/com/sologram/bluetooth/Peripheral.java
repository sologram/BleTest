package com.sologram.bluetooth;

/**
 * Created by hans on 2016/2/2.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

public class Peripheral extends BluetoothGattServerCallback implements Role {
	static private final String TAG = Peripheral.class.getSimpleName();

	protected BluetoothDevice device;
	protected Activity handler;
	protected BluetoothGattServer server;

	public Peripheral(Activity activity) throws NotReady {
		handler = activity;
		BluetoothManager m = (BluetoothManager) activity.getSystemService(
				Context.BLUETOOTH_SERVICE);
		BluetoothAdapter a = m.getAdapter();
		if (a != null && a.isEnabled()) {
			server = m.openGattServer(activity, this);
			if (server != null)
				return;
		}
		throw new NotReady();
	}

	@Override
	public void close() {
		if (server != null) {
			server.close();
			server = null;
		}
		device = null;
	}

	@Override
	public void onCharacteristicReadRequest(
			BluetoothDevice device, int requestId, int offset,
			BluetoothGattCharacteristic characteristic) {
		Log.v(TAG, "onCharacteristicReadRequest");
		super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
		server.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, new byte[1]);
	}

	@Override
	public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
		Log.v(TAG, "onConnectionStateChange: " + status + ", " + newState);
		super.onConnectionStateChange(device, status, newState);
		switch (newState) {
			case BluetoothProfile.STATE_CONNECTED:
				Log.i(TAG, "STATE_CONNECTED: " + server);
				this.device = device;
				handler.onConnected(null);
				break;
			case BluetoothProfile.STATE_DISCONNECTED:
				Log.i(TAG, "STATE_DISCONNECTED: " + server);
				handler.onDisconnected(null);
				this.device = null;
		}
	}
}
