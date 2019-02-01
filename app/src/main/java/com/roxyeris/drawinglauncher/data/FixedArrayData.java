package com.roxyeris.drawinglauncher.data;

import android.content.Context;

import com.roxyeris.drawinglauncher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arinpc on 2016-08-23.
 */
public class FixedArrayData {
    public List<FunctionListData> system;
    public List<FunctionListData> launcher;

    public FixedArrayData(Context ctx) {
        system = new ArrayList<>();

        launcher = new ArrayList<>();

        /*system.add(new FunctionListData(R.drawable.icon, ctx.getText(R.string.add_sys_wifi).toString(), Code.SYS_WIFI));
        system.add(new FunctionListData(R.drawable.icon, ctx.getText(R.string.add_sys_gps).toString(), Code.SYS_GPS));
        system.add(new FunctionListData(R.drawable.icon, ctx.getText(R.string.add_sys_bt).toString(), Code.SYS_BT));
        system.add(new FunctionListData(R.drawable.icon, ctx.getText(R.string.add_sys_airplane).toString(), Code.SYS_AIRPLANE));
        system.add(new FunctionListData(R.drawable.icon, ctx.getText(R.string.add_sys_flash).toString(), Code.SYS_LIGHT));*/
        system.add(new FunctionListData(R.drawable.icon, ctx.getText(R.string.add_sys_dialer).toString(), Code.SYS_DIALER));


        launcher.add(new FunctionListData(R.drawable.icon, ctx.getText(R.string.add_laf_drawer).toString(), Code.LAF_OPEN_DRAWER));
        launcher.add(new FunctionListData(R.drawable.icon, ctx.getText(R.string.add_laf_option).toString(), Code.LAF_OPEN_SETTING));
        launcher.add(new FunctionListData(R.drawable.icon, ctx.getText(R.string.add_laf_newpattern).toString(), Code.LAF_ADD_PATTERN));
        launcher.add(new FunctionListData(R.drawable.icon, ctx.getText(R.string.add_laf_managepattern).toString(), Code.LAF_MANAGE_PATTERN));
    }
}
