package com.sologram.protocol;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.sologram.bluetooth.Central;
import com.sologram.bluetooth.NoAddress;
import com.sologram.bluetooth.NotReady;
import com.sologram.bluetooth.Role;

public class Master extends Central {
	static private final String TAG = Master.class.getSimpleName();

	private BluetoothGattCharacteristic tx;

	public Master(Context context, String address, Role.Listener listener)
			throws NotReady, NoAddress {
		super(context, address, listener);
	}

	@Override
	public void onCharacteristicChanged(BluetoothGatt gatt,
										BluetoothGattCharacteristic characteristic) {
		Log.v(TAG, "onCharacteristicChanged: " + characteristic);
		super.onCharacteristicChanged(gatt, characteristic);
		listener.onBits(characteristic.getValue());
	}

	@Override
	public void onServicesDiscovered(BluetoothGatt gatt, int status) {
		Log.w(TAG, "onServicesDiscovered");
		super.onServicesDiscovered(gatt, status);
		BluetoothGattService s = gatt.getService(Protocol.UID_TNNL);
		if (s != null) {
			tx = s.getCharacteristic(Protocol.UID_DOWN);

			BluetoothGattCharacteristic c = s.getCharacteristic(Protocol.UID_UPLD);
			if (c != null)
				gatt.setCharacteristicNotification(c, true);
			BluetoothGattDescriptor descriptor = c.getDescriptor(Protocol.UID_CCCD);
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			gatt.writeDescriptor(descriptor);
			//Log.w(TAG, "Characteristic: " + crx + ", " + ctx);
		}
	}

	public void put(byte[] bits) {
		if (tx != null) {
			tx.setValue(bits);
			gatt.writeCharacteristic(tx);
		}
	}

	interface Listener {
		void onBits(byte[] bits);
	}
}
