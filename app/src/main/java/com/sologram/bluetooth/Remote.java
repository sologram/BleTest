package com.sologram.bluetooth;

/**
 * Created by hans on 2016/1/19.
 */

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import com.sologram.protocol.Protocol;

public class Remote extends Peripheral {
	private final static String TAG = Remote.class.getSimpleName();

	private BluetoothGattCharacteristic chi, cho;

	public Remote(Context handler) throws NotReady {
		super(handler);
		BluetoothGattService s = new BluetoothGattService(Protocol.UID_TNNL,
				BluetoothGattService.SERVICE_TYPE_PRIMARY);
		chi = new BluetoothGattCharacteristic(Protocol.UID_MISO,
				BluetoothGattCharacteristic.PROPERTY_READ |
						BluetoothGattCharacteristic.PROPERTY_BROADCAST |
						BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
				BluetoothGattCharacteristic.PERMISSION_READ |
						BluetoothGattCharacteristic.PERMISSION_WRITE);
		s.addCharacteristic(chi);
		cho = new BluetoothGattCharacteristic(Protocol.UID_MOSI,
				BluetoothGattCharacteristic.PROPERTY_BROADCAST |
						BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
				BluetoothGattCharacteristic.PERMISSION_WRITE);
		s.addCharacteristic(cho);
		server.addService(s);
	}

	@Override
	public void onCharacteristicWriteRequest(
			BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic,
			boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
		super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite,
				responseNeeded, offset, value);
		characteristic.setValue(value);
		if (responseNeeded)
			server.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
		//handler.onBits(cho.getValue());
	}

	public void put(byte[] bits) {
		if (device != null) {
			chi.setValue(bits);
			server.notifyCharacteristicChanged(device, chi, false);
		}
	}
}
