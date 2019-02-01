package com.roxyeris.drawinglauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.roxyeris.drawinglauncher.category.AppCategory;
import com.roxyeris.drawinglauncher.category.CategoryManager;
import com.roxyeris.drawinglauncher.data.DrawerPagerAdapter;
import com.roxyeris.drawinglauncher.app.Executor;
import com.roxyeris.drawinglauncher.data.PatternMatching;
import com.roxyeris.drawinglauncher.setting.SettingMain;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity
        implements GestureOverlayView.OnGestureListener,
                    GestureOverlayView.OnGesturePerformedListener {
    /**
     * 공통 변수
     */
    private SharedPreferences pref;
    private RelativeLayout homescr;

    /**
     * 그리기용 변수
     */
    private GestureOverlayView drawerView;
    private ImageButton setting;
    private ImageButton drawer;
    private Gesture gesture;
    private PatternMatching pm;

    /**
     * 앱 서랍의 동작 형태
     * 1. GridView에서 앱을 나열
     * 2. 이름 오름/내림차순, 그룹별 정렬 가능, 내 맘대로 정렬
     */
    private LinearLayout drawerCover;
    private ViewPager drawerPager;
    private DrawerPagerAdapter pagerAdapter;
    private ArrayList<AppCategory> categories;
    private CategoryManager cm;

    /**
     * Receiver
     */
    private AppReceiver apprecv = new AppReceiver();
    private HomeKeyReceiver homerecv = new HomeKeyReceiver();
    private boolean isapprecv = false;
    private boolean ishomerecv = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_launcher);

        // 변수 초기화
        initVariable();

        // 첫 구동 테스트
        testFirstRun();

        // 앱 변경에 대한 broadcast receive 등록
        registerBroadcast();
    }

    public void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addDataScheme("package");

        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        //IntentFilter forceCloseFilter = new IntentFilter(Intent.)

        //if(!isapprecv) {
            registerReceiver(apprecv, filter);
            isapprecv = true;
        //}

        //if(!ishomerecv) {
            registerReceiver(homerecv, homeFilter);
            ishomerecv = true;
        //}
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if(isapprecv) {
            unregisterReceiver(apprecv);
            isapprecv = false;
        }

        if(ishomerecv) {
            unregisterReceiver(homerecv);
            ishomerecv = false;
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 화면 방향
        int orival = pref.getInt("orientation", 0);
        if(orival == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        // 카테고리 업데이트
        updateDrawer();
        //cm.setChangedFalse();

        drawerView.addOnGesturePerformedListener(this); // 이걸 적용해야 Gesture 색이 바뀜, 내용은 없어도 됨

        // 색 변경시 업데이트
        int color = drawerView.getGestureColor();
        float alpha = drawerView.getAlpha();

        String hexColor = String.format("%06X", (0xFFFFFF & color));

        String convColor = pref.getString("gestureColor", "303F9F");
        float convAlpha = pref.getFloat("gestureAlpha", 0.5f);

        if(!hexColor.equals(convColor) || alpha != convAlpha) {
            drawerView.setUncertainGestureColor(Color.parseColor("#"+convColor));
            drawerView.setGestureColor(Color.parseColor("#"+convColor));
            drawerView.setAlpha(convAlpha);
        }

        drawerView.setGestureStrokeWidth(pref.getFloat("gestureThick", 100f));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 앱 서랍이 열려있으면 닫고 아니면 동작 아무것도 안함
        if(drawerCover.getVisibility() == View.VISIBLE) {
            hideDrawer();
        }
    }

    private void initVariable() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        homescr = (RelativeLayout) findViewById(R.id.homescr);
        drawerView = (GestureOverlayView) findViewById(R.id.drawerView);
        setting = (ImageButton) findViewById(R.id.btn_setting);
        drawer = (ImageButton) findViewById(R.id.btn_drawer);
        drawerCover = (LinearLayout) findViewById(R.id.drawerCover);
        drawerPager = (ViewPager) findViewById(R.id.drawerPager);
        pm = PatternMatching.getInstance();
        cm = CategoryManager.getInstance();

        pm.setCtx(this);
        pm.initialize();
        cm.initialize(this);

        categories = cm.getAllCategory();

        drawerView.addOnGestureListener(this);

        // 알파 적용
        drawerCover.setBackgroundColor(Color.argb(130, 0, 0, 0));

        drawerView.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_SINGLE);

        hideDrawer();

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btn_setting) {
                    startActivity(new Intent(MainActivity.this, SettingMain.class));
                }
            }
        });

        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btn_drawer) {
                    Executor e = new Executor("LAUNCHER:drawer", MainActivity.this);
                    e.start();
                }
            }
        });

        pagerAdapter = new DrawerPagerAdapter(getSupportFragmentManager());
        updateDrawer();
        drawerPager.setAdapter(pagerAdapter);
    }

    private void testFirstRun() {
        if(pref.getBoolean("firstrun", true)) {
            startActivity(new Intent(this, FirstRun.class));
        }
    }

    private void updateDrawer() {
        // 기존 카테고리 추가
        pagerAdapter.clearFragment();
        for(AppCategory c:categories) {
            DrawerFragment frag = new DrawerFragment();
            frag.init(pref.getInt("DRAWER_ROW", 6), pref.getInt("DRAWER_COL", 5), c.getAllApps());
            pagerAdapter.addNewFragment(frag, c.getName());
        }

        // 새 카테고리 추가
        NewCategoryFragment newfrag = new NewCategoryFragment();
        pagerAdapter.addNewFragment(newfrag, getString(R.string.drawer_new_cat2)); // 이름 부분 string으로 대체

        // 다 추가하면 갱신
        pagerAdapter.notifyDataSetChanged();
    }

    public void hideDrawer() {
        drawerCover.setVisibility(View.INVISIBLE);
    }

    public void showDrawer() {
        drawerCover.setVisibility(View.VISIBLE);
    }

    // Listener for Gestures
    @Override
    public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

    }

    @Override
    public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

    }

    @Override
    public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        gesture = gestureOverlayView.getGesture();

        // 길이가 0인 단순 터치 방지
        if(gesture.getLength() > 0) {
            // 명령 실행
            String job = pm.compareGesture(gesture);
            if (!job.equals("nojob")) {
                Executor exc = new Executor(job, this);
                exc.start();
            }
        }
    }

    @Override
    public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {

    }

    /**
     * Inner class로 broadcast receiver를 생성
     */
    private class AppReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // All Apps 카테고리만 데이터를 다시 생성
            cm.resetAllApps();
            pagerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 홈키 Broadcast Receiver
     */
    private class HomeKeyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if (reason.equals("homekey")) {
                        // 앱서랍 닫기
                        if(drawerCover.getVisibility() == View.VISIBLE) {
                            hideDrawer();
                        }
                    //} else if (reason.equals("recentapps")) {
                    }
                }
            }
        }
    }
}
