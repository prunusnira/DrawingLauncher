package com.roxyeris.drawinglauncher.setting;

/**
 * Created by arinpc on 2016-10-18.
 */

public class SettingMenuData {
    private int iconid;
    private String title;

    public int getIconid() {
        return iconid;
    }

    public void setIconid(int iconid) {
        this.iconid = iconid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
}
