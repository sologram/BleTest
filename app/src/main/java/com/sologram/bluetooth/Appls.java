package com.sologram.bluetooth;

/**
 * Created by hans on 2016/1/19.
 */

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

public class Appls extends Peripheral {
	private final static String TAG = Appls.class.getSimpleName();

	private BluetoothGattCharacteristic chd, chp;

	public Appls(Activity handler, String address) throws NotReady {
		super(handler);
		BluetoothGattService s = new BluetoothGattService(Role.UID_APPL,
				BluetoothGattService.SERVICE_TYPE_PRIMARY);
		chd = new BluetoothGattCharacteristic(Role.UID_STTD,
				BluetoothGattCharacteristic.PROPERTY_BROADCAST |
						BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
				BluetoothGattCharacteristic.PERMISSION_WRITE);
		s.addCharacteristic(chd);
		chp = new BluetoothGattCharacteristic(Role.UID_STTP,
				BluetoothGattCharacteristic.PROPERTY_READ |
						BluetoothGattCharacteristic.PROPERTY_BROADCAST |
						BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
				BluetoothGattCharacteristic.PERMISSION_READ |
						BluetoothGattCharacteristic.PERMISSION_WRITE);
		s.addCharacteristic(chp);
		server.addService(s);
		Log.w(TAG, "open");
	}

	public void close() {
		super.close();
		Log.w(TAG, "close");
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
		//handler.onBits(chd.getValue());
	}

	public void put(byte[] bits) {
		if (device != null) {
			chp.setValue(bits);
			server.notifyCharacteristicChanged(device, chp, false);
		}
	}

	/*public interface Handler extends Role.Handler {
		void onEvent(int event);

		void onPower(int power);

		void onStep(int step);

		void onStatus(int status);

		void onTemperature(int temperature);

		void onTimer(int time);
	}//*/
}
