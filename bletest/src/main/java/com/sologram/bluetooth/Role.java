package com.sologram.bluetooth;

public interface Role {

	void close();

	interface Listener {
		void onConnected(String address);

		void onDisconnected(String address);
	}
}
