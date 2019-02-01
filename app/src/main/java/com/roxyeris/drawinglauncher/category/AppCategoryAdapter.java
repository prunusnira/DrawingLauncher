package com.roxyeris.drawinglauncher.category;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;
import com.roxyeris.drawinglauncher.app.Executable;

import java.util.List;

/**
 * Created by arinpc on 2016-11-01.
 */

public class AppCategoryAdapter extends BaseAdapter {
    private List<Executable> item;
    private Context ctx;
    private ImageView iv;
    private TextView tv;

    public AppCategoryAdapter(Context ctx, AppCategory cat) {
        this.ctx = ctx;
        this.item = cat.getAllApps();
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null) {
            LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.element_icontext, null);
        }

        iv = (ImageView) v.findViewById(R.id.appIcon);
        tv = (TextView) v.findViewById(R.id.appName);

        Object c = item.get(i);
        if(c instanceof ApplicationInfo) {
            iv.setImageDrawable(((ApplicationInfo)c).loadIcon(ctx.getPackageManager()));
            tv.setText(((ApplicationInfo)c).loadLabel(ctx.getPackageManager()));
        }
        else if(c instanceof Intent) {
            iv.setImageResource(android.R.drawable.ic_dialog_dialer);
            tv.setText("Call");
        }
        return v;
    }

    public String getItemString(int i) {
        return item.get(i).getName();
    }
}
