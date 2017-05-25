package com.sologram.bluetooth;

/**
 * Created by hans on 2016/1/19.
 */

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

public class Local extends Central {
	static private final String TAG = Local.class.getSimpleName();

	private BluetoothGattCharacteristic chi, cho;

	public Local(Activity handler, String address) throws NotReady, NoAddress {
		super(handler, address);
	}

	@Override
	public void close() {
		super.close();
		chi = null;
		cho = null;
	}

	@Override
	public void onCharacteristicChanged(BluetoothGatt gatt,
										BluetoothGattCharacteristic characteristic) {
//		Log.i(TAG, "onCharacteristicChanged: " + characteristic);
		super.onCharacteristicChanged(gatt, characteristic);
		//if (listener != null)
		//	listener.onBits(chi.getValue());
	}

	@Override
	public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
		super.onConnectionStateChange(gatt, status, newState);
		if (newState == BluetoothProfile.STATE_DISCONNECTED) {
			chi = null;
			cho = null;
		}
	}

	@Override
	public void onServicesDiscovered(BluetoothGatt gatt, int status) {
		Log.v(TAG, "onServicesDiscovered: " + status);
		super.onServicesDiscovered(gatt, status);
		BluetoothGattService s = gatt.getService(Role.UID_TNNL);
		if (s != null) {
			cho = s.getCharacteristic(Role.UID_MOSI);
			if (cho != null)
				gatt.setCharacteristicNotification(cho, true);
			chi = s.getCharacteristic(Role.UID_MISO);
			if (chi != null)
				gatt.setCharacteristicNotification(chi, true);
		}
	}

	public boolean put(byte[] bits) {
		if (cho != null) {
			cho.setValue(bits);
			gatt.writeCharacteristic(cho);
			return true;
		}
		return false;
	}
}
