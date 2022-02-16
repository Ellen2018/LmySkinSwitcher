package com.yalemang.skinswitcherlibraray.bean;

import android.content.res.Resources;
import android.view.View;

/**
 * 刷新皮肤提供的实体类
 */
public class LmyRefreshSkin {
    private Resources resources;
    private View view;
    private int resId;
    private String attributeName;
    private String attributeValue;

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
