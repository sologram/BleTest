package com.sologram.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Scanner implements BluetoothAdapter.LeScanCallback {
	static private final String TAG = Scanner.class.getSimpleName();

	private BluetoothAdapter adapter;
	private Listener listener;

	public Scanner(Context context) throws NotReady {
		adapter = Adapter.get(context);
		if (adapter == null || !adapter.isEnabled())
			throw new NotReady();
	}

	@Override
	public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
		Log.v(TAG, "Found: " + device);
		if (listener != null)
			listener.onFound(device.getAddress(), device.getName(), parseUuids(scanRecord));
	}

	private List<UUID> parseUuids(byte[] advertisedData) {
		List<UUID> re = new ArrayList<UUID>();

		ByteBuffer b = ByteBuffer.wrap(advertisedData).order(ByteOrder.LITTLE_ENDIAN);
		while (b.remaining() > 2) {
			byte length = b.get();
			if (length == 0) break;

			byte type = b.get();
			switch (type) {
				case 0x02: // Partial list of 16-bit UUIDs
				case 0x03: // Complete list of 16-bit UUIDs
					while (length >= 2) {
						re.add(UUID.fromString(String.format(
								"%08x-0000-1000-8000-00805f9b34fb", b.getShort())));
						length -= 2;
					}
					break;

				case 0x06: // Partial list of 128-bit UUIDs
				case 0x07: // Complete list of 128-bit UUIDs
					while (length >= 16) {
						long lsb = b.getLong();
						long msb = b.getLong();
						re.add(new UUID(msb, lsb));
						length -= 16;
					}
					break;

				default:
					b.position(b.position() + length - 1);
					break;
			}
		}
		return re;
	}

	public Scanner start(UUID[] services, Listener listener) {
		Log.w(TAG, "start: " + services);
		this.listener = listener;
		adapter.startLeScan(services, this);
		return this;
	}

	public void stop() {
		Log.w(TAG, "stop");
		listener = null;
		adapter.stopLeScan(this);
	}

	public interface Listener {
		void onFound(String address, String name, List<UUID> uuids);
	}
}
