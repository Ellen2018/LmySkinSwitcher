package com.yalemang.skinswitcherlibraray;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Objects;

class LmySkinLayoutFactory implements LayoutInflater.Factory2 {

    //具体拦截逻辑都在该类里
    private final LmySkinAttribute skinAttribute;

    public LmySkinLayoutFactory(){
        skinAttribute = new LmySkinAttribute();
    }

    //系统自带的控件名包名路径
    //因为布局中会直接使用<TextView没带全路径的，所以我们该手动加上
    private static final String[] systemViewPackage = {
            "androidx.widget.",
            "androidx.view.",
            "androidx.webkit.",
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    //反射控件对应的构造器而使用
    private static final Class<?>[] mConstructorSignature = new Class<?>[]{Context.class,AttributeSet.class};
    //存储控件的构造器，避免重复创建
    private static final HashMap<String, Constructor<? extends View>> mConstructor = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        View view = onCreateViewFromTag(name,context,attributeSet);
        if(view == null){
            view = onCreateView(name, context, attributeSet);
        }
        //筛选符合属性的View
        skinAttribute.loadView(view,attributeSet);
        return view;
    }


    /**
     * 通过反射构建控件对象
     * @param name
     * @param context
     * @param attributeSet
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        Constructor<? extends View> constructor = mConstructor.get(name);
        View view = null;
        if(constructor == null){
            try {
                Class<? extends View> viewClass = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = viewClass.getConstructor(mConstructorSignature);
                mConstructor.put(name,constructor);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if(constructor != null){
            try {
                view = constructor.newInstance(context,attributeSet);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    private View onCreateViewFromTag(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet){
        if(name.indexOf(".") > 0){
           //说明XML中该控件带有包名全路径
        }
        View view = null;
        for(String packageName:systemViewPackage){
            view = onCreateView(packageName+name,context,attributeSet);
            if(view != null){
                break;
            }
        }
        return view;
    }
}
