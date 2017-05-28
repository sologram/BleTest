package com.sologram.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.UUID;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Advertiser extends AdvertiseCallback {
	static private final String TAG = Advertiser.class.getSimpleName();

	private BluetoothLeAdvertiser adv;

	public Advertiser(Context context) throws NotSupported {
		BluetoothAdapter a = Adapter.get(context);
		if (a.isMultipleAdvertisementSupported())
			adv = a.getBluetoothLeAdvertiser();
		else throw new NotSupported();
	}

	@Override
	public void onStartSuccess(AdvertiseSettings settingsInEffect) {
		Log.v(TAG, "Advertising started successfully");
		super.onStartSuccess(settingsInEffect);
	}

	@Override
	public void onStartFailure(int errorCode) {
		Log.v(TAG, "Advertising failed: " + errorCode);
		super.onStartFailure(errorCode);
	}

	public Advertiser start(Context context, UUID uuid, String name) {
		Log.w(TAG, "start...");
		Adapter.get(context).setName(name);
		adv.startAdvertising(
				new AdvertiseSettings.Builder()
						.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
						.setConnectable(true)
						.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW)
						.build(),
				new AdvertiseData.Builder()
						.setIncludeDeviceName(true)
						.addServiceUuid(new ParcelUuid(uuid))
						.build(),
				this);
		return this;
	}

	public void stop() {
		Log.w(TAG, "stop");
		adv.stopAdvertising(this);
	}
}
