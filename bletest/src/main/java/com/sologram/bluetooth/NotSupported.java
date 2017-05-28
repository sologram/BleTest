package com.sologram.bluetooth;

/**
 * Created by hans on 2016/1/27.
 */

import android.app.Activity;

public class NotSupported extends ExceptionEx {
	public void show(Activity activity) {
		show(activity, "Bluetooth not support.");
	}
}
