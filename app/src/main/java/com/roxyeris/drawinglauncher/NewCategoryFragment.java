package com.roxyeris.drawinglauncher;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.category.CategoryManageActivity;
import com.roxyeris.drawinglauncher.category.CategoryManager;
import com.roxyeris.drawinglauncher.setting.AddPattern;
import com.roxyeris.drawinglauncher.view.AppDrawerIcons;

import java.util.List;

/**
 * Created by arinpc on 2017-02-01.
 */

public class NewCategoryFragment extends Fragment implements View.OnClickListener {
    private LinearLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.element_newcatpager, viewGroup, false);

        layout = (LinearLayout) root.findViewById(R.id.newcatpager);
        layout.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), CategoryManageActivity.class));
    }
}
