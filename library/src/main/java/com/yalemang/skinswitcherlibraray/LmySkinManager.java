package com.yalemang.skinswitcherlibraray;

import android.app.Activity;
import android.app.Application;

import com.yalemang.skinswitcherlibraray.skinnterface.LmySkinSwitchListener;
import com.yalemang.skinswitcherlibraray.skinnterface.LmySkinSwitcherSetting;

import java.util.ArrayList;
import java.util.List;

//皮肤管理类
public class LmySkinManager {

    //单例对象
    private volatile static LmySkinManager INSTANCE;
    //Application对象
    private Application application;
    //皮肤名字集合
    private List<LmySkin> lmySkinList = new ArrayList<>();
    //记录当前应用的皮肤名
    private LmySkin currentSkin = null;
    //应用Activity生命周期监听
    private LmySkinActivityLifecycle lmySkinActivityLifecycle;
    //屏蔽刷新的Activity
    private List<Class<? extends  Activity>> shieldActivityList;
    //皮肤切换设置接口
    private LmySkinSwitcherSetting lmySkinSwitcherSetting;
    //皮肤切换监听集合
    private List<LmySkinSwitchListener> lmySkinSwitchListenerList;

    //皮肤文件格式
    private String skinFileFormat = ".apk";

    private LmySkinManager(){
       //默认皮肤
        LmySkin defaultSkin = new LmySkin("default","default");
        lmySkinList.add(defaultSkin);
        currentSkin = defaultSkin;
        //初始化皮肤监听集合
        lmySkinSwitchListenerList = new ArrayList<>();
    }

    public List<LmySkin> getSkinData(){
        return lmySkinList;
    }

    /**
     * 添加皮肤数据
     * @param lmySkin
     */
    public void addLmySkin(LmySkin lmySkin){
        boolean isAdd = true;
        for(LmySkin ls:lmySkinList){
            if(ls.getPath().equals(lmySkin.getPath())){
                isAdd = false;
                break;
            }
        }
        if(isAdd) {
            lmySkinList.add(lmySkin);
        }
    }

    /**
     * 移除皮肤数据
     * @param lmySkin
     */
    public void removeLmySkin(LmySkin lmySkin){
        lmySkinList.remove(lmySkin);
    }

    /**
     * 是否选择这个皮肤
     */
    public boolean isSelectSkin(LmySkin lmySkin){
        return currentSkin.getPath().equals(lmySkin.getPath());
    }

    /**
     * 获取皮肤格式
     * @return
     */
    String getSkinFileFormat() {
        return skinFileFormat;
    }

    /**
     * 切换皮肤
     * @param lmySkin
     */
    public void switchSkin(LmySkin lmySkin){
        this.currentSkin = lmySkin;
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

    public void setting(LmySkinSwitcherSetting lmySkinSwitcherSetting){
        this.lmySkinSwitcherSetting = lmySkinSwitcherSetting;
        if(this.lmySkinSwitcherSetting.lmySkinRefresh() == null){
            //抛出异常
            throw new NullPointerException("lmySkinRefresh is null");
        }
        this.shieldActivityList = this.lmySkinSwitcherSetting.shieldActivityList();
    }

    LmySkinSwitcherSetting getLmySkinSwitcherSetting() {
        return lmySkinSwitcherSetting;
    }
}
