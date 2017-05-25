package com.sologram.bluetooth;

/**
 * Created by hans on 2016/1/19.
 */

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import java.util.UUID;

public class Master extends Central {
	static private final String TAG = Master.class.getSimpleName();

	private BluetoothGattCharacteristic crx, ctx;

	public Master(Activity handler, String address) throws NotReady, NoAddress {
		super(handler, address);
		Log.w(TAG, "open");
	}

	@Override
	public void close() {
		Log.w(TAG, "close");
		super.close();
		crx = null;
		ctx = null;
	}

	@Override
	public void onCharacteristicChanged(BluetoothGatt gatt,
	                                    BluetoothGattCharacteristic characteristic) {
		Log.v(TAG, "onCharacteristicChanged: " + characteristic);
		super.onCharacteristicChanged(gatt, characteristic);
		handler.onBits(crx.getValue());
	}

	@Override
	public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
		Log.v(TAG, "onConnectionStateChange: " + status);
		super.onConnectionStateChange(gatt, status, newState);
		if (newState == BluetoothProfile.STATE_DISCONNECTED) {
			crx = null;
			ctx = null;
		}
	}

	@Override
	public void onServicesDiscovered(BluetoothGatt gatt, int status) {
		Log.w(TAG, "onServicesDiscovered: " + status);
		super.onServicesDiscovered(gatt, status);
		BluetoothGattService s = gatt.getService(Role.UID_TNNL);
		if (s != null) {
			ctx = s.getCharacteristic(Role.UID_DOWN);
			crx = s.getCharacteristic(Role.UID_UPLD);
			if (crx != null)
				gatt.setCharacteristicNotification(crx, true);
			BluetoothGattDescriptor descriptor = crx.getDescriptor(UID_CCCD);
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			gatt.writeDescriptor(descriptor);
			//Log.w(TAG, "Characteristic: " + crx + ", " + ctx);
		}
	}

	public void put(byte[] bits) {
		if (ctx != null) {
			ctx.setValue(bits);
			gatt.writeCharacteristic(ctx);
		}
	}
}
