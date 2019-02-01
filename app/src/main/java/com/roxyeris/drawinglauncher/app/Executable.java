package com.roxyeris.drawinglauncher.app;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.roxyeris.drawinglauncher.data.Code;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by roxye on 2017-05-22.
 */

public abstract class Executable {
    private int type;
    private Drawable icon;
    private String name;
    private String pkg;
    protected Context ctx;

    public Executable(Context ctx) { this.ctx = ctx; }

    // 아이콘, 이름, 패키지명 저장
    public void setIcon(Drawable icon) { this.icon = icon; }
    public void setName(String name) { this.name = name; }
    public void setPackage(String pkg) { this.pkg = pkg; }
    public void setType(int type) { this.type = type; }

    // getter
    public Drawable getIcon() { return icon; }
    public String getName() { return name; }
    public String getPackage() { return pkg; }
    public int getType() { return type; }

    // toJSON
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("name", getName());
            json.put("pkg", getPackage());
            json.put("type", getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    // 앱 실행
    public abstract void execute(String arg);
}
