package com.sologram.bluetooth;

/**
 * Created by hans on 29/04/2017.
 */

import android.app.Activity;
import android.widget.Toast;

public class ExceptionEx extends java.lang.Exception {
	public void show(Activity activity) {
		show(activity, "ExceptionEx");
	}

	public void show(Activity activity, String message) {
		Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
		activity.finish();
	}
}