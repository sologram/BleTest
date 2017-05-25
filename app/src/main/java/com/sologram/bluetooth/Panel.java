package com.sologram.bluetooth;

/**
 * Created by hans on 2016/1/19.
 */

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

public class Panel extends Central {
	static private final String TAG = Panel.class.getSimpleName();

	private BluetoothGattCharacteristic chd, chp;

	public Panel(Activity handler, String address) throws NotReady, NoAddress {
		super(handler, address);
		Log.w(TAG, "open");
	}

	@Override
	public void close() {
		Log.w(TAG, "close");
		super.close();
		chd = null;
		chp = null;
	}

	@Override
	public void onCharacteristicChanged(BluetoothGatt gatt,
										BluetoothGattCharacteristic characteristic) {
//		Log.i(TAG, "onCharacteristicChanged: " + characteristic);
		super.onCharacteristicChanged(gatt, characteristic);
		handler.onBits(chp.getValue());
	}

	@Override
	public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
		super.onConnectionStateChange(gatt, status, newState);
		if (newState == BluetoothProfile.STATE_DISCONNECTED) {
			chd = null;
			chp = null;
		}
	}

	@Override
	public void onServicesDiscovered(BluetoothGatt gatt, int status) {
		Log.v(TAG, "onServicesDiscovered: " + status);
		super.onServicesDiscovered(gatt, status);
		BluetoothGattService s = gatt.getService(Role.UID_APPL);
		if (s != null) {
			chd = s.getCharacteristic(Role.UID_STTD);
			if (chd != null)
				gatt.setCharacteristicNotification(chd, true);
			chp = s.getCharacteristic(Role.UID_STTD);
			if (chp != null)
				gatt.setCharacteristicNotification(chp, true);
		}
	}

	public void put(byte[] bits) {
		if (chd != null) {
			chd.setValue(bits);
			gatt.writeCharacteristic(chd);
		}
	}
}
