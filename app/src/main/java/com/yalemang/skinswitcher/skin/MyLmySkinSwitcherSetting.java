package com.yalemang.skinswitcher.skin;

import android.app.Activity;

import com.yalemang.skinswitcher.App;
import com.yalemang.skinswitcher.SkinManagerActivity;
import com.yalemang.skinswitcher.utils.SharePreferenceHelper;
import com.yalemang.skinswitcherlibraray.LmySkin;
import com.yalemang.skinswitcherlibraray.skinnterface.LmySkinRefresh;
import com.yalemang.skinswitcherlibraray.skinnterface.LmySkinSwitcherSetting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyLmySkinSwitcherSetting implements LmySkinSwitcherSetting {
    @Override
    public LmySkinRefresh lmySkinRefresh() {
        //具体设置皮肤属性的逻辑
        return new MyLmySkinRefresh();
    }

    @Override
    public List<String> interceptFieldNames() {
        List<String>  xmlFields = new ArrayList<>();
        //背景属性拦截
        xmlFields.add("background");
        //文本字颜色拦截
        xmlFields.add("textColor");
        //TabLayout
        xmlFields.add("tabIndicatorColor");
        xmlFields.add("tabSelectedTextColor");
        return xmlFields;
    }

    @Override
    public List<Class<? extends Activity>> shieldActivityList() {
        //屏蔽重构的界面
        List<Class<? extends Activity>> shieldActivityList = new ArrayList<>();
        shieldActivityList.add(SkinManagerActivity.class);
        return shieldActivityList;
    }

    @Override
    public LmySkin applyAppLaunchSkin() {
        //获取用户是否已切换的皮肤
        LmySkin lmySkin = SharePreferenceHelper.getInstance().getCurrentSaveSkin(App.INSTANCE);
        return lmySkin;
    }

    @Override
    public LmySkin defaultSkin() {
       return getDefaultSkin();
    }

    public static LmySkin getDefaultSkin(){
        return new LmySkin("default","default");
    }
}
