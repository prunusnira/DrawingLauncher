package com.roxyeris.drawinglauncher.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;
import com.roxyeris.drawinglauncher.data.SettingAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by arinpc on 2016-08-24.
 */
public class SettingMenuBase extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView settingList;
    private SettingAdapter stAdapter;
    public ArrayList<SettingMenuData> settingItems;
    public SharedPreferences pref;
    public SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_setting);

        initialize();
    }

    private void initialize() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        settingList = (ListView) findViewById(R.id.list_timeline);

        settingItems = new ArrayList<>();
        setSettingItems();
        stAdapter = new SettingAdapter(this, settingItems);
        settingList.setAdapter(stAdapter);
        settingList.setOnItemClickListener(this);
        stAdapter.notifyDataSetChanged();
    }

    public void setSettingItems() {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 0 && resultCode == RESULT_OK) {
            if(intent != null) {
                File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        +"/Android/data/com.roxyeris.drawinglauncher", "bg");

                edit = pref.edit();
                edit.putString("bgimg", f.getAbsolutePath());
                edit.commit();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
