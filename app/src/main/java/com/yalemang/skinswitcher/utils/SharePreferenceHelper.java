package com.yalemang.skinswitcher.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.yalemang.skinswitcher.skin.MyLmySkinSwitcherSetting;
import com.yalemang.skinswitcherlibraray.LmySkin;

import java.io.File;

public class SharePreferenceHelper{

    private SharedPreferences sharedPreferences;
    private static final String SKIN_DATA = "skin_data";
    private static volatile SharePreferenceHelper INSTANCE;
    /*
     * 保存手机里面的名字
     */
    private SharedPreferences.Editor editor;


    public static SharePreferenceHelper getInstance(){
        if(INSTANCE == null){
            synchronized (SharePreferenceHelper.class){
                if(INSTANCE == null){
                    INSTANCE = new SharePreferenceHelper();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Context context){
        sharedPreferences = context.getSharedPreferences(SKIN_DATA,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private SharePreferenceHelper(){

    }

    /**
     * 保存切换的皮肤
     * @param lmySkin
     */
    public void saveSwitchSkin(LmySkin lmySkin){
        save(SKIN_DATA,lmySkin.getName());
    }

    /**
     * 获取当前保存的皮肤
     * @param context
     */
    public LmySkin getCurrentSaveSkin(Context context){
        String skinName = getValue(SKIN_DATA,null);
        LmySkin lmySkin = null;
        if(skinName == null){
            //不存在
            return MyLmySkinSwitcherSetting.getDefaultSkin();
        }else {
            if(skinName.equals("default")){
                return MyLmySkinSwitcherSetting.getDefaultSkin();
            }else {
                File file = new File(context.getCacheDir(), skinName);
                lmySkin = new LmySkin(file.getAbsolutePath());
                return lmySkin;
            }
        }
    }

    private void save(String key, Object value) {
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }
        editor.commit();
    }

    private  <T> T getValue(String key, T defaultObject) {
        Object objectValue;
        if (defaultObject instanceof String) {
            objectValue = sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            objectValue = sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            objectValue = sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            objectValue = sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            objectValue = sharedPreferences.getLong(key, (Long) defaultObject);
        } else {
            objectValue = sharedPreferences.getString(key, null);
        }
        return (T) objectValue;
    }
}
