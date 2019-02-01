package com.roxyeris.drawinglauncher;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.roxyeris.drawinglauncher.app.Executable;
import com.roxyeris.drawinglauncher.setting.AddPattern;
import com.roxyeris.drawinglauncher.view.AppDrawerIcons;

import java.util.List;

/**
 * Created by arinpc on 2017-02-01.
 */

public class DrawerFragment extends Fragment {
    private GridLayout layout;
    private int row;
    private int col;
    private int pxWidth;
    private int pxHeight;
    private List<Executable> apps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.element_drawerpager, viewGroup, false);

        layout = (GridLayout) root.findViewById(R.id.drawerCategory);

        getDisplaySize();
        setDrawerData();
        deployDrawerApp(apps);

        return root;
    }

    public void init(int row, int col, List<Executable> apps) {
        this.apps = apps;
        this.row = row;
        this.col = col;
    }

    private void setDrawerData() {
        // Layout
        layout.setColumnCount(col);
        layout.setRowCount(row);
        layout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
    }

    private void deployDrawerApp(final List<Executable> apps){//}, TextView emptyView) {
        if(apps != null) {
            //emptyView.setVisibility(View.GONE);
            for (final Executable capp : apps) {
                if (checkUserApp(capp)) {
                    AppDrawerIcons child =
                            new AppDrawerIcons(getActivity(), capp, row, col, pxWidth, pxHeight);
                    final LinearLayout outer = child.getOuterView();

                    // 모든 터치시 배경 색 변경
                    outer.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_CANCEL:
                                    outer.setBackgroundColor(Color.TRANSPARENT);
                                    break;
                                case MotionEvent.ACTION_DOWN:
                                    outer.setBackgroundColor(Color.argb(130, 0, 85, 255));
                                    break;
                                case MotionEvent.ACTION_UP:
                                    outer.setBackgroundColor(Color.TRANSPARENT);
                                    break;
                            }
                            return false;
                        }
                    });

                    // 짧게 누르면 앱 실행
                    outer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            capp.execute(capp.getPackage());
                            //getActivity().startActivity(getActivity().getPackageManager().getLaunchIntentForPackage(packageName));
                        }
                    });

                    // 길게 누르면 메뉴 표시
                    outer.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                            popupMenu.inflate(R.menu.appshortcut);
                            popupMenu.show();

                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    if (menuItem.getItemId() == R.id.appshortcut_addpattern) {
                                        // 파라미터를 추가하여 패턴 등록 수행
                                        Intent i = new Intent(getActivity(), AddPattern.class);
                                        i.putExtra("num", apps.indexOf(capp));
                                        i.putExtra("app", capp.getPackage());
                                        getActivity().startActivity(i);
                                    } else if (menuItem.getItemId() == R.id.appshortcut_removeapp) {
                                        // 앱 삭제
                                        ApplicationInfo ainfo = null;
                                        try {
                                            ainfo = getContext().getPackageManager().getApplicationInfo(capp.getPackage(), 0);
                                        } catch (PackageManager.NameNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        if (ainfo != null && ((ainfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)) {
                                            getActivity().startActivity(new Intent(Intent.ACTION_DELETE)
                                                    .setData(Uri.parse("package:" + capp.getPackage())));
                                        } else {
                                            AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                                            dlg.setTitle(getActivity().getString(R.string.app_rm_alert));
                                            dlg.setMessage(getActivity().getString(R.string.app_rm_t));
                                            dlg.setNeutralButton(getActivity().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                            dlg.create().show();
                                        }
                                    }
                                    return false;
                                }
                            });
                            return true;
                        }
                    });
                    layout.addView(child);
                }
            }
        }
        else {
            //emptyView.setVisibility(View.VISIBLE);
        }
    }

    // 사용자 앱 여부 확인
    private boolean checkUserApp(Executable app) {
        return getActivity().getPackageManager().getLaunchIntentForPackage(app.getPackage()) != null;
    }

    // 앱 서랍의 아이콘 정렬을 위한 화면 크기
    private void getDisplaySize() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        pxWidth = dm.widthPixels;
        pxHeight = dm.heightPixels;
    }
}
