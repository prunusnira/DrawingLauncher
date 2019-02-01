package com.roxyeris.drawinglauncher;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.roxyeris.drawinglauncher.data.PatternMatching;
import com.roxyeris.drawinglauncher.setting.AddPattern;

/**
 * Created by arinpc on 2016-08-08.
 */
public class FirstRun extends Activity implements View.OnClickListener {
    private SharedPreferences pref;
    private PatternMatching pm;
    private LinearLayout imgCover;
    private LinearLayout imgCover2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstrun);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        pm = PatternMatching.getInstance();
        imgCover = (LinearLayout) findViewById(R.id.firstRunImgs);

        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("firstrun", false);
        edit.putString("gestureColor", "303F9F");
        edit.putFloat("gestureAlpha", 0.5f);
        edit.putFloat("gestureThick", 100f);
        edit.commit();
        edit.apply();

        Button nextbtn1 = (Button)findViewById(R.id.firstStep1Next);

        ImageView iv1 = new ImageView(this);
        ImageView iv2 = new ImageView(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1);

        iv1.setImageResource(R.drawable.f1);

        imgCover.addView(iv1, lp);
        imgCover.addView(iv2, lp);

        nextbtn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 버튼을 누르면 다음 View Layout 으로 이동
        // 1페이지
        if(v.getId() == R.id.firstStep1Next) {
            setContentView(R.layout.firstrun2);
            Button nextbtn2 = (Button)findViewById(R.id.firstStep2Next);
            imgCover = (LinearLayout)findViewById(R.id.firstRunImgs);
            imgCover2 = (LinearLayout)findViewById(R.id.firstRunImgs2);
            ImageView iv1 = new ImageView(this);
            ImageView iv2 = new ImageView(this);
            ImageView iv3 = new ImageView(this);
            ImageView iv4 = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1);
            iv1.setImageResource(R.drawable.f2_1);
            iv2.setImageResource(R.drawable.f2_2);
            iv3.setImageResource(R.drawable.f2_3);
            iv4.setImageResource(R.drawable.f2_4);
            imgCover.addView(iv1, lp);
            imgCover.addView(iv2, lp);
            imgCover2.addView(iv3, lp);
            imgCover2.addView(iv4, lp);
            nextbtn2.setOnClickListener(this);
        }
        // 2페이지
        if(v.getId() == R.id.firstStep2Next) {
            setContentView(R.layout.firstrun3);
            Button nextbtn3 = (Button)findViewById(R.id.firstStep3Next);
            imgCover = (LinearLayout)findViewById(R.id.firstRunImgs);
            ImageView iv1 = new ImageView(this);
            ImageView iv2 = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1);
            iv1.setImageResource(R.drawable.f3_1);
            iv2.setImageResource(R.drawable.f3_2);
            imgCover.addView(iv1, lp);
            imgCover.addView(iv2, lp);
            nextbtn3.setOnClickListener(this);
        }
        // 3페이지
        if(v.getId() == R.id.firstStep3Next) {
            setContentView(R.layout.firstrun4);
            Button nextbtn4 = (Button)findViewById(R.id.firstStep4Next);
            nextbtn4.setOnClickListener(this);
        }
        // 마지막에 최초 패턴 등록을 위해 이동
        if(v.getId() == R.id.firstStep4Next) {
            startActivityForResult(new Intent(this, AddPattern.class), 9);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int rstCode, Intent data) {
        super.onActivityResult(reqCode, rstCode, data);

        if(reqCode == 9) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(R.string.firstrun_cont_t);
            dlg.setMessage(R.string.firstrun_cont_s);
            dlg.setCancelable(false);
            dlg.setPositiveButton(R.string.firstrun_cont_p, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    startActivity(new Intent(FirstRun.this, AddPattern.class));
                }
            });
            dlg.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dlg.create().show();
        }
    }

    @Override
    public void onBackPressed() {
        // 아무것도 하지 않음 - 액티비티 종료 방지
    }
}
