package com.sologram.bluetooth;

public interface Role {

	void close();

	interface Listener {
		//void onBits(byte[] bits);

		void onConnected(String address);

		void onDisconnected(String address);
	}
}
