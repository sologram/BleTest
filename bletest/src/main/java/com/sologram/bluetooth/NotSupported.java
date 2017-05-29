package com.sologram.bluetooth;

import android.app.Activity;

public class NotSupported extends Exception {
	public void show(Activity activity) {
		show(activity, "Bluetooth not support.");
	}
}
