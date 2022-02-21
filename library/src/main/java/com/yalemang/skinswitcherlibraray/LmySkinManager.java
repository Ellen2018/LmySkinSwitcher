package com.yalemang.skinswitcherlibraray;

import android.app.Activity;
import android.app.Application;

import com.yalemang.skinswitcherlibraray.exception.SkinFileFormatException;
import com.yalemang.skinswitcherlibraray.exception.SkinFileNotFoundException;
import com.yalemang.skinswitcherlibraray.skinnterface.LmySkinSwitchListener;
import com.yalemang.skinswitcherlibraray.skinnterface.LmySkinSwitcherSetting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//皮肤管理类
public class LmySkinManager {

    //单例对象
    private volatile static LmySkinManager INSTANCE;
    //Application对象
    private Application application;
    //记录当前应用的皮肤
    private LmySkin currentSkin = null;
    //记录默认皮肤
    private LmySkin defaultSkin = null;
    //应用Activity生命周期监听
    private LmySkinActivityLifecycle lmySkinActivityLifecycle;
    //屏蔽刷新的Activity
    private List<Class<? extends Activity>> shieldActivityList;
    //皮肤切换设置接口
    private LmySkinSwitcherSetting lmySkinSwitcherSetting;
    //皮肤切换监听集合
    private List<LmySkinSwitchListener> lmySkinSwitchListenerList;

    //皮肤文件格式
    private List<String> skinFileFormats = null;

    private LmySkinManager(){
        //初始化皮肤监听集合
        lmySkinSwitchListenerList = new ArrayList<>();
    }

    /**
     * 是否选择这个皮肤
     */
    public boolean isSelectSkin(LmySkin lmySkin) {
        return currentSkin.getPath().equals(lmySkin.getPath());
    }

    private boolean isContainsFormat(LmySkin lmySkin){
        boolean isContainsFormat = false;
        for (String format : skinFileFormats) {
            if(lmySkin.getPath().endsWith(format)){
                isContainsFormat = true;
                break;
            }
        }
        return isContainsFormat;
    }

    /**
     * 切换皮肤
     *
     * @param lmySkin
     */
    public void switchSkin(LmySkin lmySkin) {
        this.currentSkin = lmySkin;
        if(!isContainsFormat(lmySkin)){
            //不能使用的皮肤包格式
            throw new SkinFileFormatException();
        }
        if (!new File(lmySkin.getPath()).exists()) {
            //皮肤包本地不存在异常，请先将皮肤包缓存到本地再切换
            throw new SkinFileNotFoundException();
        }
        lmySkinActivityLifecycle.switchSkin();
    }

    /**
     * 是否是默认皮肤
     * @return
     */
    public boolean isDefaultSkin(){
        return currentSkin.getPath().equals("default") && currentSkin.getName().equals("default");
    }

    /**
     * 获取到当前的皮肤名
     * @return
     */
    public LmySkin getCurrentSkin(){
        return currentSkin;
    }

    /**
     * 获取当前默认皮肤
     * @return
     */
    public LmySkin getDefaultSkin() {
        return defaultSkin;
    }

    public List<Class<? extends Activity>> getShieldActivityList() {
        return shieldActivityList;
    }

    public static LmySkinManager getInstance(){
        if(INSTANCE == null){
            synchronized (LmySkinManager.class){
                if(INSTANCE == null){
                    INSTANCE = new LmySkinManager();
                }
            }
        }

        return INSTANCE;
    }

    public Application getApplication(){
        return application;
    }

    /**
     * 添加皮肤切换监听器
     * @param lmySkinSwitchListener
     */
    public void addLmySkinSwitchListener(LmySkinSwitchListener lmySkinSwitchListener){
        this.lmySkinSwitchListenerList.add(lmySkinSwitchListener);
    }

    /**
     * 移除皮肤切换监听器
     * @param lmySkinSwitchListener
     */
    public void removeLmySkinSwitchListener(LmySkinSwitchListener lmySkinSwitchListener){
        this.lmySkinSwitchListenerList.remove(lmySkinSwitchListener);
    }

    /**
     * 切换皮肤成功调用
     */
    void switchSkinSuccess(){
        for(LmySkinSwitchListener lmySkinSwitchListener:lmySkinSwitchListenerList){
            lmySkinSwitchListener.switchSkin(currentSkin);
        }
    }

    /**
     * 皮肤管理初始化
     * @param app
     */
    public void initApp(Application app){
        this.application = app;
        //对所有Activity的声明周期进行监听
        app.registerActivityLifecycleCallbacks(lmySkinActivityLifecycle = new LmySkinActivityLifecycle());
    }

    public void setting(LmySkinSwitcherSetting lmySkinSwitcherSetting) {
        this.lmySkinSwitcherSetting = lmySkinSwitcherSetting;
        if (this.lmySkinSwitcherSetting.lmySkinRefresh() == null) {
            //抛出异常
            throw new NullPointerException("lmySkinRefresh is null");
        }
        if (this.lmySkinSwitcherSetting.applyAppLaunchSkin() == null) {
            //抛出异常
            throw new NullPointerException("currentSkin is null");
        } else {
            currentSkin = this.lmySkinSwitcherSetting.applyAppLaunchSkin();
        }
        if (this.lmySkinSwitcherSetting.defaultSkin() == null) {
            //抛出异常
            throw new NullPointerException("defaultSkin is null");
        } else {
            defaultSkin = this.lmySkinSwitcherSetting.defaultSkin();
        }
        if (this.lmySkinSwitcherSetting.skinFormats() == null) {
            //抛出异常
            throw new NullPointerException("skinFormats is null");
        } else {
            skinFileFormats = this.lmySkinSwitcherSetting.skinFormats();
        }
        this.shieldActivityList = this.lmySkinSwitcherSetting.shieldActivityList();

        //验证格式
        if(!isContainsFormat(currentSkin)){
            throw new SkinFileFormatException();
        }
    }

    LmySkinSwitcherSetting getLmySkinSwitcherSetting() {
        return lmySkinSwitcherSetting;
    }
}
