package com.roxyeris.drawinglauncher.app;

import android.content.Context;
import android.content.Intent;

import com.roxyeris.drawinglauncher.MainActivity;
import com.roxyeris.drawinglauncher.setting.SettingMain;

/**
 * Created by roxye on 2017-05-22.
 */

public class ExecFunction extends Executable {
    public ExecFunction(Context ctx) { super(ctx); }

    @Override
    public void execute(String arg) {
        //String arg = getPackage();
        // 앱 서랍 열기
        // 설정 열기
        if(arg.equals("setting")) {
            // 새 인텐트 열기
            ctx.startActivity(new Intent(ctx, SettingMain.class));
        }
        else if(arg.equals("drawer")) {
            // 앱 서랍 열기
            MainActivity main = (MainActivity) ctx;
            main.showDrawer();
        }
    }
}
