package com.sologram.bluetooth;

import android.app.Activity;

public class NotSupported extends ExceptionEx {
	public void show(Activity activity) {
		show(activity, "Bluetooth not support.");
	}
}
