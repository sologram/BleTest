package com.sologram.bluetooth;

import java.util.UUID;

public interface Role {
	static public final UUID UID_APPL = UUID.fromString("00000001-0000-1000-8000-00805F9B34FB");
	static public final UUID UID_STTD = UUID.fromString("00000002-0000-1000-8000-00805F9B34FB");
	static public final UUID UID_STTP = UUID.fromString("00000003-0000-1000-8000-00805F9B34FB");
	static public final UUID UID_STPS = UUID.fromString("00000004-0000-1000-8000-00805F9B34FB");
	static public final UUID UID_LOGS = UUID.fromString("00000005-0000-1000-8000-00805F9B34FB");

	static public final UUID UID_TNNL = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
	static public final UUID UID_DOWN = UUID.fromString("00000001-0000-1000-8000-00805F9B34FB");
	static public final UUID UID_UPLD = UUID.fromString("00000002-0000-1000-8000-00805F9B34FB");
	static public final UUID UID_CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9B34FB");

	static public final UUID UID_MISO = UUID.fromString("00000008-0000-1000-8000-00805F9B34FB");
	static public final UUID UID_MOSI = UUID.fromString("00000009-0000-1000-8000-00805F9B34FB");

	void close();

	static abstract class Activity extends android.app.Activity implements Listener {
	}

	interface Listener {
		void onBits(byte[] bits);

		void onConnected(String address);

		void onDisconnected(String address);
	}
}
