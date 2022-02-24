package com.yalemang.skinswitcherlibraray.bean;

import android.app.Activity;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * 刷新皮肤提供的实体类
 */
public class LmyRefreshSkin {
    //对应皮肤的资源,换肤时这个非常重要
    private Resources resources;
    //对应过滤出的控件对象
    private View view;
    //控件对象使用的资源id,换肤时这个非常重要
    private int resId;
    //xml中对应的属性名，例如:background
    private String attributeName;
    //xml中对应属性使用的value,无作用，因为笔者将其解析到resId
    private String attributeValue;
    //XML解析过后的属性与Value值都存在在它里面了，无作用
    private AttributeSet attributeSet;
    //当前界面的弱引用
    private WeakReference<Activity> activityWeakReference;

    public void setActivity(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    public Activity getActivity(){
        return activityWeakReference.get();
    }

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

    public AttributeSet getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(AttributeSet attributeSet) {
        this.attributeSet = attributeSet;
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
