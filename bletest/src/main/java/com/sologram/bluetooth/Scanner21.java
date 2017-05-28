package com.sologram.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Hans on 2017/5/5.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Scanner21 extends ScanCallback implements Role {
    static private final String TAG = Scanner21.class.getSimpleName();

    private BluetoothAdapter adp;
    private Context hnd;
    BluetoothLeScanner bluetoothLeScanner;

    public Scanner21(Context handler) throws NotReady {
        hnd = handler;
        adp = ((BluetoothManager) handler.getSystemService(Context.BLUETOOTH_SERVICE)).
                getAdapter();
        bluetoothLeScanner = adp.getBluetoothLeScanner();
        if (adp != null && adp.isEnabled()) {
            Log.w(TAG, "startScan");
            bluetoothLeScanner.startScan(this);
        } else throw new NotReady();
    }

    @Override
    public void close() {
        if (adp != null) {
            bluetoothLeScanner.stopScan(this);
            adp = null;
        }
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        BluetoothDevice d = result.getDevice();
        //hnd.onFound(d.getAddress(), d.getName(), parseUuids(result.getScanRecord().getBytes()));
        Log.w(TAG, "onScanResult: " + result.getDevice().getAddress());
    }

    @Override
    public void onScanFailed(int errorCode) {
        super.onScanFailed(errorCode);
        Log.w(TAG, "onScanFailed: " + errorCode);
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

    /*public interface Handler extends Role.Handler {
        void onFound(String address, String name, List<UUID> uuids);
    }*/
}
