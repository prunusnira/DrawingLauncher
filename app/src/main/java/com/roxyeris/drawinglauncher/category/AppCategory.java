package com.roxyeris.drawinglauncher.category;

import com.roxyeris.drawinglauncher.app.Executable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arinpc on 2016-10-18.
 */

public class AppCategory {
    private String name;
    //private List<ApplicationInfo> apps;   // 패키지 이름이 들어감
    private List<Executable> apps;

    public AppCategory() {
        apps = new ArrayList<>();
    }

    public void addApp(Executable app) {
        apps.add(app);
    }

    public void removeApp(Executable app) {
        apps.remove(app);
    }

    public void addAppList(List<Executable> app) {
        apps.addAll(app);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Executable> getAllApps() {
        return apps;
    }

    public JSONObject categoryToJSON() {
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();
        for(Executable a:apps) {
            arr.put(a.toJSON());
        }

        try {
            json.put("catname", name);
            json.put("apps", arr);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
