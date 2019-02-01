package com.roxyeris.drawinglauncher.app;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;

import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;

/**
 * Created by roxye on 2017-05-22.
 */

public class ExecSystem extends Executable {
    public ExecSystem(Context ctx) { super(ctx); }

    @Override
    public void execute(String arg) {
        //String arg = getPackage();
        // 시스템 동작 실행
        if(arg.equals("wifi")) {
            WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            int state = wifiManager.getWifiState();
            if(state == WIFI_STATE_DISABLED) {
                wifiManager.setWifiEnabled(true);
            }
            else if(state == WIFI_STATE_ENABLED) {
                wifiManager.setWifiEnabled(false);
            }
            else {
                Toast.makeText(ctx, "Can not change Wi-Fi State", Toast.LENGTH_SHORT).show();
            }
        }
        else if(arg.equals("gps")) {

        }
        else if(arg.equals("bt")) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            boolean isEnabled = bluetoothAdapter.isEnabled();
            if (!isEnabled) {
                bluetoothAdapter.enable();
            }
            else if(isEnabled) {
                bluetoothAdapter.disable();
            }
        }
        else if(arg.equals("airplane")) {
            boolean state = Settings.System.getInt(ctx.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
            Intent newIntent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            newIntent.putExtra("state", !state);
            ctx.sendBroadcast(newIntent);
        }
        else if(arg.equals("flash")) {
            boolean exist = ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

            if(exist) {

            }
            else {
                Toast.makeText(ctx, "No flash feature available", Toast.LENGTH_SHORT).show();
            }
        }
        else if(arg.equals("dialer")) {
            Intent dialer = new Intent(Intent.ACTION_DIAL);
            ctx.startActivity(dialer);
        }
    }
}
