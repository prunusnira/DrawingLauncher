package com.roxyeris.drawinglauncher.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roxyeris.drawinglauncher.R;
import com.roxyeris.drawinglauncher.app.Executable;

/**
 * Created by arinpc on 2016-08-16.
 */
public class AppDrawerIcons extends LinearLayout {
    public Context ctx;
    public Executable info;
    public View v;

    private LinearLayout linearLayout;
    private LinearLayout outLinearLayout;
    private ImageView icon;
    private TextView label;


    public AppDrawerIcons(final Context ctx, Executable info,
                          int row, int col, int width, int height) {
        super(ctx);
        this.ctx = ctx;
        this.info = info;
        int wsize = width/col;
        int hsize = height/row;

        LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Activity를 extend해야 사용 가능한 getSystemService를 Context를 이용하여 사용 가능 하도록 설정
        v = vi.inflate(R.layout.element_appdrawer, this);

        outLinearLayout = (LinearLayout) v.findViewById(R.id.appOut);
        linearLayout = (LinearLayout) v.findViewById(R.id.appBack);
        icon = (ImageView) v.findViewById(R.id.appIcon);
        label = (TextView) v.findViewById(R.id.appName);

        outLinearLayout.getLayoutParams().width = wsize;
        outLinearLayout.getLayoutParams().height = hsize;

        icon.getLayoutParams().width = (int)(0.5*wsize);
        icon.getLayoutParams().height = (int)(0.5*hsize);

        switch(col) {
            case 3:
                label.setTextSize(18);
                break;
            case 4:
                label.setTextSize(15);
                break;
            case 5:
                label.setTextSize(13);
                break;
            case 6:
                label.setTextSize(11);
                break;
        }

        icon.setImageDrawable(info.getIcon());
        label.setText(info.getName());

        label.getLayoutParams().width = (int)(0.8*wsize);
    }

    public LinearLayout getOuterView() {
        return linearLayout;
    }
}
