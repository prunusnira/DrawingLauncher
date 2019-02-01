package com.roxyeris.drawinglauncher.data;

/**
 * Created by arinpc on 2016-08-23.
 */
public class FunctionListData {
    private int icon;
    private String name;
    private int jobcode;

    public FunctionListData() {}

    public FunctionListData(int icon, String name, int jobcode) {
        setIcon(icon);
        setName(name);
        setJobcode(jobcode);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getJobcode() {
        return jobcode;
    }

    public void setJobcode(int jobcode) {
        this.jobcode = jobcode;
    }
}
