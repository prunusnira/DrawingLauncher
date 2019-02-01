package com.roxyeris.drawinglauncher.setting;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;
import com.roxyeris.drawinglauncher.data.PatternMatching;

import java.util.Set;

/**
 * Created by arinpc on 2016-08-27.
 */
public class PatternManagerAdapter extends BaseAdapter {
    private PatternMatching pm = PatternMatching.getInstance();
    private Set<String> item = pm.getPatternEntries();
    private Context ctx;

    private ImageView pv;
    private ImageView iv;
    private TextView tv;

    public PatternManagerAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return pm.getPatternEntries().size();
    }

    public String getItemTitle(int i) {
        return (String)item.toArray()[i];
    }

    @Override
    public Object getItem(int i) {
        return pm.getGesture((String)item.toArray()[i]);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.element_patternmanager, null);
        }

        Gesture g = (Gesture)getItem(i);
        String job = (String)item.toArray()[i];

        pv = (ImageView) v.findViewById(R.id.pattern);
        iv = (ImageView) v.findViewById(R.id.appIcon);
        tv = (TextView) v.findViewById(R.id.appName);

        pv.setBackgroundColor(Color.WHITE);

        String[] jobde = job.split(":");

        pv.setImageBitmap(g.toBitmap(dpToPx(100), dpToPx(100), 0, Color.rgb(0, 0, 0)));
        if(jobde[0].equals("APP")) {
            try {
                ApplicationInfo info = ctx.getPackageManager().getApplicationInfo(jobde[1], PackageManager.GET_META_DATA);
                iv.setImageDrawable(info.loadIcon(ctx.getPackageManager()));
                tv.setText(info.loadLabel(ctx.getPackageManager()));
            }
            catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(jobde[0].equals("LAUNCHER")) {
            iv.setImageResource(R.drawable.icon);
            switch(jobde[1]) {
                case "drawer":
                    tv.setText(R.string.set_pat_launcher_drawer);
                    break;
                case "setting":
                    tv.setText(R.string.set_pat_launcher_setting);
                    break;
                default:
                    tv.setText(jobde[1]);
                    break;
            }
        }
        else if(jobde[0].equals("SYSTEM")) {
            iv.setImageResource(R.drawable.icon);
            switch(jobde[1]) {
                case "dialer":
                    tv.setText(R.string.set_pat_system_dialer);
                    break;
                default:
                    tv.setText(jobde[1]);
                    break;
            }
        }

        return v;
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
