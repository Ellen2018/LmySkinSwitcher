package com.yalemang.skinswitcher;

import android.app.Application;

import com.yalemang.skinswitcher.skin.MyLmySkinSwitcherSetting;
import com.yalemang.skinswitcherlibraray.LmySkinManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LmySkinManager.getInstance().initApp(this);
        //必须调用，而且要实现每个方法
        LmySkinManager.getInstance().setting(new MyLmySkinSwitcherSetting());
    }
}
