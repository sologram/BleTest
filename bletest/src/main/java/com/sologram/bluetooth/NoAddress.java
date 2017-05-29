package com.sologram.bluetooth;

import android.app.Activity;

public class NoAddress extends ExceptionEx {
	public void show(Activity activity) {
		show(activity, "No address.");
	}
}
