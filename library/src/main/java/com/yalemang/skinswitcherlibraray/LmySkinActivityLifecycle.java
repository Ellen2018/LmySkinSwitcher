package com.yalemang.skinswitcherlibraray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

import com.yalemang.skinswitcherlibraray.exception.SkinSwitchUnknownException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class LmySkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private final List<Activity> activeActivityList = new ArrayList<>();
    private List<Activity> recreateTask = new ArrayList<>();
    //记录是否切换皮肤
    private volatile boolean isSwitchSkin = false;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        boolean isReflect = true;
        for (Class<? extends Activity> activityClass : LmySkinManager.getInstance().getShieldActivityList()) {
            if (activity.getClass().equals(activityClass)) {
                isReflect = false;
                break;
            }
        }
        if(isReflect) {
            activeActivityList.add(activity);
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            //反射setFactory2,Android Q及以上已经失效-> 报not field 异常
            //Android Q以上setFactory2问题
            //http://www.javashuo.com/article/p-sheppkca-ds.html
            forceSetFactory2(layoutInflater);
        }
    }

    /**
     * 最新的方式，适配Android Q
     * @param inflater
     */
    @SuppressLint("DiscouragedPrivateApi")
    private static void forceSetFactory2(LayoutInflater inflater) {
        Class<LayoutInflaterCompat> compatClass = LayoutInflaterCompat.class;
        Class<LayoutInflater> inflaterClass = LayoutInflater.class;
        try {
            Field sCheckedField = compatClass.getDeclaredField("sCheckedField");
            sCheckedField.setAccessible(true);
            sCheckedField.setBoolean(inflater, false);
            Field mFactory = inflaterClass.getDeclaredField("mFactory");
            mFactory.setAccessible(true);
            Field mFactory2 = inflaterClass.getDeclaredField("mFactory2");
            mFactory2.setAccessible(true);
            //自定义的Factory2
            LmySkinLayoutFactory skinLayoutFactory = new LmySkinLayoutFactory();
            mFactory2.set(inflater, skinLayoutFactory);
            mFactory.set(inflater, skinLayoutFactory);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            throw new SkinSwitchUnknownException(e.getMessage());
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (isSwitchSkin) {
            if (recreateTask != null && recreateTask.size() > 0) {
                int removePosition = -1;
                for (int i = 0; i < recreateTask.size(); i++) {
                    Activity recordActivity = recreateTask.get(i);
                    if(recordActivity.getClass().equals(activity.getClass())){
                        removePosition = i;
                        break;
                    }
                }
                recreateTask.remove(removePosition);
                if (recreateTask.size() == 0) {
                    //这里才是真正意义上皮肤切换完成
                    LmySkinManager.getInstance().switchSkinSuccess();
                    recreateTask = null;
                    isSwitchSkin = false;
                }
            }
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
       activeActivityList.remove(activity);
    }

    public void switchSkin() {
        isSwitchSkin = true;
        recreateTask = new ArrayList<>();
        for (Activity activity : activeActivityList) {
            if (LmySkinManager.getInstance().getShieldActivityList() == null) {
                recreateTask.add(activity);
            } else {
                boolean isReCreate = true;
                for (Class<? extends Activity> activityClass : LmySkinManager.getInstance().getShieldActivityList()) {
                    if (activity.getClass().equals(activityClass)) {
                        isReCreate = false;
                    }
                }
                if (isReCreate) {
                    recreateTask.add(activity);
                }
            }
        }
        if (recreateTask.size() > 0) {
            for (Activity activity : recreateTask) {
                //recreate实际上新new了一个Activity
                activity.recreate();
            }
        }
        //皮肤切换成功-这里严格意义上完成切换不对，recreate是异步的
        //界面没有重构完成，此处先调用了
        //LmySkinManager.getInstance().switchSkinSuccess();
    }
}
