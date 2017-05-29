package com.sologram.bluetooth;

import android.app.Activity;

public class NotReady extends ExceptionEx {
	public void show(Activity activity) {
		show(activity, "Bluetooth not ready.");
	}
}
