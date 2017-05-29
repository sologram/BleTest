package com.sologram.bluetooth;

import android.app.Activity;

public class NoAddress extends Exception {
	public void show(Activity activity) {
		show(activity, "No address.");
	}
}
