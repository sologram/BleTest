package com.sologram.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

public class Adapter {
	static BluetoothAdapter get(Context context) {
		return ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).
				getAdapter();
	}
}
