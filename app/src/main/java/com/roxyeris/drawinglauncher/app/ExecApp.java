package com.roxyeris.drawinglauncher.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.app.AlertDialog;

import com.roxyeris.drawinglauncher.R;
import com.roxyeris.drawinglauncher.category.CategoryManager;

import java.util.List;

/**
 * Created by roxye on 2017-05-22.
 */

public class ExecApp extends Executable {
    private CategoryManager cm = CategoryManager.getInstance();

    public ExecApp(Context ctx) { super(ctx); }

    @Override
    public void execute(final String arg) {
        if(isPackageExist(arg)) {
            // 앱 실행
            ctx.startActivity(ctx.getPackageManager().getLaunchIntentForPackage(arg));
        }
        else {
            // 앱이 삭제된 앱일 경우 다이얼로그를 띄움
            // 다이얼로그를 통해 해당 리스트에서 앱 삭제 가능
            AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
            dlg.setTitle(ctx.getString(R.string.exec_app_no_t));
            dlg.setMessage(ctx.getString(R.string.exec_app_no_s));
            dlg.setCancelable(false);
            dlg.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 앱 삭제를 위한 메소드
                    cm.removeAppFromAllCategory(arg);
                    dialogInterface.dismiss();
                }
            });
            dlg.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dlg.create().show();
        }
    }

    // 앱 실행시 패키지의 존재유무를 확인
    public boolean isPackageExist(String pkgName) {
        boolean exist = false;
        List<PackageInfo> pkgs = ctx.getPackageManager()
                .getInstalledPackages(PackageManager.GET_META_DATA);

        for(PackageInfo p: pkgs) {
            if(p.packageName.equals(pkgName)) {
                exist = true;
            }
        }

        return exist;
    }
}
