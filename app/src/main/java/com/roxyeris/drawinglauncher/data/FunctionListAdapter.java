package com.roxyeris.drawinglauncher.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;

import java.util.List;

/**
 * Created by arinpc on 2016-08-23.
 */
public class FunctionListAdapter extends BaseAdapter {
    private Context ctx;
    private List<FunctionListData> item;

    private ImageView icon;
    private TextView name;

    public FunctionListAdapter(Context ctx, List<FunctionListData> data) {
        this.ctx = ctx;
        item = data;
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
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Activity를 extend해야 사용 가능한 getSystemService를 Context를 이용하여 사용 가능 하도록 설정
            v = vi.inflate(R.layout.element_patternmanager, null);
        }

        icon = (ImageView) v.findViewById(R.id.appIcon);
        name = (TextView) v.findViewById(R.id.appName);

        FunctionListData data = item.get(i);

        icon.setImageDrawable(ctx.getResources().getDrawable(data.getIcon()));
        name.setText(data.getName());

        return v;
    }
}
