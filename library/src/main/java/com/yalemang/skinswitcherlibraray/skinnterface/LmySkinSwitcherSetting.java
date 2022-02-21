package com.yalemang.skinswitcherlibraray.skinnterface;

import android.app.Activity;

import com.yalemang.skinswitcherlibraray.LmySkin;

import java.util.List;

/**
 * 皮肤切换器切换设置接口
 */
public interface LmySkinSwitcherSetting {
    //使用者通过此接口完成皮肤刷新
    LmySkinRefresh lmySkinRefresh();
    //设置拦截的属性name
    List<String> interceptFieldNames();
    //过滤掉重构的Activity,一般设置为皮肤设置界面
    List<Class<? extends Activity>> shieldActivityList();
    //设置应用启动后采用的皮肤
    LmySkin applyAppLaunchSkin();
    //默认皮肤
    LmySkin defaultSkin();
    //皮肤格式
    List<String> skinFormats();
}
