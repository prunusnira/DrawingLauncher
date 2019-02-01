package com.roxyeris.drawinglauncher.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by arinpc on 2016-10-19.
 */

public class CategoryManagerAdapter extends BaseAdapter {
    private Context ctx;
    private List<String> categories;
    private ImageView iv;
    private TextView tv;

    public CategoryManagerAdapter(Context ctx, List<String> categories) {
        this.ctx = ctx;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null) {
            LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.element_icontext, null);
        }
        String data = categories.get(i);
        iv = (ImageView) v.findViewById(R.id.appIcon);
        tv = (TextView) v.findViewById(R.id.appName);

        iv.setImageResource(R.drawable.category);
        tv.setText(data);
        return v;
    }
}
