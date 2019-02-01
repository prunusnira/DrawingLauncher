package com.roxyeris.drawinglauncher.data;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;

import java.util.ArrayList;

/**
 * Created by ArinCherryBlossom on 2015-05-10.
 */
public class AppListAdapter extends BaseAdapter {
    private ArrayList<ApplicationInfo> items;
    private Context context;
    private ImageView icon;
    private TextView label;

    public AppListAdapter(Context context, ArrayList<ApplicationInfo> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Activity를 extend해야 사용 가능한 getSystemService를 Context를 이용하여 사용 가능 하도록 설정
            v = vi.inflate(R.layout.element_icontext, null);
        }
        icon = (ImageView) v.findViewById(R.id.appIcon);
        label = (TextView) v.findViewById(R.id.appName);

        ApplicationInfo data = items.get(position);

        icon.setImageDrawable(data.loadIcon(context.getPackageManager()));
        label.setText(data.loadLabel(context.getPackageManager()).toString());

        return v;
    }
}
