package com.sologram.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.sologram.protocol.Protocol;

public class Appls extends Peripheral {
	private final static String TAG = Appls.class.getSimpleName();

	private BluetoothGattCharacteristic chd, chp;

	public Appls(Context handler, String address) throws NotReady {
		super(handler);
		BluetoothGattService s = new BluetoothGattService(Protocol.UID_APPL,
				BluetoothGattService.SERVICE_TYPE_PRIMARY);
		chd = new BluetoothGattCharacteristic(Protocol.UID_STTD,
				BluetoothGattCharacteristic.PROPERTY_BROADCAST |
						BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
				BluetoothGattCharacteristic.PERMISSION_WRITE);
		s.addCharacteristic(chd);
		chp = new BluetoothGattCharacteristic(Protocol.UID_STTP,
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
