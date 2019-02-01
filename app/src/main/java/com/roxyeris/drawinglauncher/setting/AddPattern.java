package com.roxyeris.drawinglauncher.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.roxyeris.drawinglauncher.R;
import com.roxyeris.drawinglauncher.data.AppListAdapter;
import com.roxyeris.drawinglauncher.data.FixedArrayData;
import com.roxyeris.drawinglauncher.data.PatternMatching;
import com.roxyeris.drawinglauncher.data.FunctionListAdapter;

/**
 * Created by arinpc on 2016-08-17.
 */
public class AddPattern extends AppCompatActivity
        implements GestureOverlayView.OnGestureListener, View.OnClickListener {
    /**
     * 그리기용 변수
     */
    private Gesture gesture;
    private String newJob;

    private String feature;
    private String argument;
    private ArrayList<ApplicationInfo> apps;

    private LinearLayout buttons;
    private GestureOverlayView view;
    private ImageView iv;
    private Button add;
    private Button cancel;
    private TextView nojob;
    private TextView desc;

    private PatternMatching pm;
    private FixedArrayData fad;

    private AppListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addpattern);

        // Intent에서 받을 데이터가 있다면 받기
        Intent recv = getIntent();
        newJob = recv.getStringExtra("app");
        int num = recv.getIntExtra("num", -1);

        initialize();

        // 첫 시작은 기능 선택
        if(newJob == null) {
            selectFeature();
        }
        else {
            //setNewJob("APP:"+newJob);
            feature = "APP";
            argument = newJob;
            handlerApp.sendEmptyMessage(num);
        }
    }

    private void initialize() {
        apps = new ArrayList<>();
        pm = PatternMatching.getInstance();
        fad = new FixedArrayData(this);

        view = (GestureOverlayView) findViewById(R.id.drawerView);
        buttons = (LinearLayout) findViewById(R.id.buttonPanel);
        iv = (ImageView) findViewById(R.id.patternImgView);

        add = (Button) findViewById(R.id.btn_addpattern);
        cancel = (Button) findViewById(R.id.btn_cancelpattern);

        nojob = (TextView) findViewById(R.id.nojob);
        desc = (TextView) findViewById(R.id.addptn_desc);

        buttons.setVisibility(View.INVISIBLE);
        nojob.setVisibility(View.INVISIBLE);

        view.setGestureStrokeWidth(100f);
        view.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_SINGLE);

        // 앱에 대한 패턴 동작을 대비하여 미리 정보를 가져옴
        // (중간의 프리징 방지)
        adapter = new AppListAdapter(this, apps);

        List<ApplicationInfo> appsAll = getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);

        for(int i = 0; i < appsAll.size(); i++) {
            if(checkUserApp(appsAll.get(i))) {
                apps.add(appsAll.get(i));
            }
        }

        Collections.sort(apps, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo o1, ApplicationInfo o2) {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.loadLabel(getPackageManager()).toString(), o2.loadLabel(getPackageManager()).toString());
            }
        });

        view.addOnGestureListener(this);
        add.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void selectFeature() {
        // List dialog open
        AlertDialog.Builder firstDialog = new AlertDialog.Builder(this);
        firstDialog.setTitle(getText(R.string.add_dlgfirst_title));
        firstDialog.setItems(R.array.dlgfirst, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i) {
                    case 0:
                        handler.sendEmptyMessage(0);
                        break;
                    case 1:
                        handler.sendEmptyMessage(1);
                        break;
                    case 2:
                        handler.sendEmptyMessage(2);
                        break;
                    case 3:
                        finish();
                        break;
                }
            }
        });
        firstDialog.setCancelable(false);
        firstDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        firstDialog.create().show();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch(message.what) {
                case 0:
                    feature = "APP";
                    selectApp();
                    break;
                case 1:
                    feature = "SYSTEM";
                    //selectSystem();
                    setNewJob("SYSTEM:dialer");
                    break;
                case 2:
                    feature = "LAUNCHER";
                    selectLauncher();
                    break;
            }
            //Toast.makeText(AddPattern.this, feature, Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    private void selectApp() {
        adapter.notifyDataSetChanged();

        // List dialog open
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getText(R.string.add_dlgapp_title));
        dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handlerApp.sendEmptyMessage(i);
            }
        });
        dialog.setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                selectFeature();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        adapter.notifyDataSetChanged();
        dialog.create().show();
    }

    private void selectSystem() {
        /**
         * 시스템 동작
         * 1. 와이파이
         * 2. gps
         * 3. 블루투스
         * 4. 비행기
         * 5. 손전등
         * 6. 다이얼러
         */
        FunctionListAdapter adapter = new FunctionListAdapter(this, fad.system);

        // List dialog open
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getText(R.string.add_dlgapp_title));
        dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handlerSystem.sendEmptyMessage(i);
            }
        });
        dialog.setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                selectFeature();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        dialog.setCancelable(false);
        adapter.notifyDataSetChanged();
        dialog.create().show();
    }

    private void selectLauncher() {
        /**
         * 런처동작 정의
         * 1. 앱 서랍 열기
         * 2. 런처 옵션 열기
         * 3. 새 패턴 추가
         * 4. 패턴관리
         */
        FunctionListAdapter adapter = new FunctionListAdapter(this, fad.launcher);

        // List dialog open
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getText(R.string.add_dlgapp_title));
        dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handlerLauncher.sendEmptyMessage(i);
            }
        });
        dialog.setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                selectFeature();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        dialog.setCancelable(false);
        adapter.notifyDataSetChanged();
        dialog.create().show();
    }

    private boolean checkUserApp(ApplicationInfo app) {
        return getPackageManager().getLaunchIntentForPackage(app.packageName) != null;
    }

    private Handler handlerApp = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            int id = message.what;
            ApplicationInfo selected = apps.get(id);
            apps.clear();

            argument = selected.packageName;

            setNewJob(feature+":"+argument);

            try {
                String appName = getPackageManager().getApplicationInfo(argument, PackageManager.GET_META_DATA)
                        .loadLabel(getPackageManager()).toString();
                desc.setText(desc.getText()+"\n"+appName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return false;
        }
    });

    private Handler handlerLauncher = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            int id = message.what;

            switch(id) {
                case 0:
                    argument = "drawer";
                    desc.setText(desc.getText()+"\nfor Open app drawer");
                    break;
                case 1:
                    argument = "setting";
                    desc.setText(desc.getText()+"\nfor Open launcher setting");
                    break;
                case 2:
                    argument = "addpat";
                    desc.setText(desc.getText()+"\nfor Add new pattern");
                    break;
                case 3:
                    argument = "managepat";
                    desc.setText(desc.getText()+"\nfor Manage pattern");
                    break;
                case 4:
                    finish();
                    break;
                default:
                    break;
            }

            setNewJob(feature+":"+argument);
            return false;
        }
    });

    private Handler handlerSystem = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            int id = message.what;

            switch(id) {
                /*case 0:
                    argument = "wifi";
                    break;
                case 1:
                    argument = "gps";
                    break;
                case 2:
                    argument = "bt";
                    break;
                case 3:
                    if(Build.VERSION.SDK_INT < 17) {
                        argument = "airplane";
                    }
                    else {
                        argument = "";
                        Toast.makeText(AddPattern.this,
                                "Changing Airplane Mode is supported only below Android 4.2",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    argument = "flash";
                    break;*/
                case 0:
                    argument = "dialer";
                    break;
                case 1:
                    finish();
                    break;
                default:
                    break;
            }

            setNewJob(feature+":"+argument);
            return false;
        }
    });

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_addpattern) {
            pm.saveGesture(newJob, gesture);
            this.view.invalidate();
            finish();
        }
        else if(view.getId() == R.id.btn_cancelpattern) {
            finish();
        }
    }

    @Override
    public void onGestureStarted(GestureOverlayView gestureOverlayView,
                                 MotionEvent motionEvent) {

    }

    @Override
    public void onGesture(GestureOverlayView gestureOverlayView,
                          MotionEvent motionEvent) {

    }

    @Override
    public void onGestureEnded(GestureOverlayView gestureOverlayView,
                               MotionEvent motionEvent) {
        gesture = gestureOverlayView.getGesture();

        // 길이가 0인 단순 터치 방지
        if(gesture.getLength() > 0) {
            Bitmap gb = gesture.toBitmap(100, 100, 0, Color.argb(135, 0, 0, 255));
            iv.setImageBitmap(gb);

            // 데이터를 받아오면서 선택 버튼도 활성화
            buttons.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGestureCancelled(GestureOverlayView gestureOverlayView,
                                   MotionEvent motionEvent) {

    }

    public void setNewJob(String job) {
        newJob = job;
    }
}
