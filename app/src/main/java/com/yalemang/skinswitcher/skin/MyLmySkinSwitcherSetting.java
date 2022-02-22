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

    /**
     * 具体设置皮肤属性的逻辑
     * @return
     */
    @Override
    public LmySkinRefresh lmySkinRefresh() {
        //具体设置皮肤属性的逻辑
        return new MyLmySkinRefresh();
    }

    /**
     * 过滤皮肤切换要用到的属性
     * @return
     */
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

    /**
     * 屏蔽某些Activity不进行重构
     * @return
     */
    @Override
    public List<Class<? extends Activity>> shieldActivityList() {
        //屏蔽重构的界面
        List<Class<? extends Activity>> shieldActivityList = new ArrayList<>();
        shieldActivityList.add(SkinManagerActivity.class);
        return shieldActivityList;
    }

    /**
     * 应用启动时使用的皮肤
     * @return
     */
    @Override
    public LmySkin applyAppLaunchSkin() {
        //获取用户是否已切换的皮肤
        LmySkin lmySkin = SharePreferenceHelper.getInstance().getCurrentSaveSkin(App.INSTANCE);
        return lmySkin;
    }

    /**
     * 获取默认皮肤
     * @return
     */
    @Override
    public LmySkin defaultSkin() {
       return getDefaultSkin();
    }

    /**
     * 设置能解析的皮肤包格式
     * @return
     */
    @Override
    public List<String> skinFormats() {
        List<String> skinFormats = new ArrayList<>();
        skinFormats.add(".app");
        skinFormats.add(".apk");
        return skinFormats;
    }

    //此方法读者可以自行方式去实现，定义默认皮肤
    public static LmySkin getDefaultSkin(){
        return new LmySkin("default","default");
    }
}
