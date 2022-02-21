package com.yalemang.skinswitcher;

import android.app.Application;

import com.yalemang.skinswitcher.skin.MyLmySkinSwitcherSetting;
import com.yalemang.skinswitcher.utils.SharePreferenceHelper;
import com.yalemang.skinswitcherlibraray.LmySkin;
import com.yalemang.skinswitcherlibraray.LmySkinManager;
import com.yalemang.skinswitcherlibraray.skinnterface.LmySkinSwitchListener;

public class App extends Application {

    public static  App INSTANCE = null;



    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        SharePreferenceHelper.getInstance().init(this);
        LmySkinManager.getInstance().initApp(this);
        //必须调用，而且要实现每个方法
        LmySkinManager.getInstance().setting(new MyLmySkinSwitcherSetting());
    }
}
