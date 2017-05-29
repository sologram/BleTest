package com.sologram.bluetooth;

import android.app.Activity;
import android.widget.Toast;

public class Exception extends java.lang.Exception {
	public void show(Activity activity) {
		show(activity, "Exception");
	}

	public void show(Activity activity, String message) {
		Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
		activity.finish();
	}
}
