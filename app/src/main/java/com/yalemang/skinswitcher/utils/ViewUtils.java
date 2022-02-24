package com.yalemang.skinswitcher.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewParent;

/**
 * 视图工具类
 */
public class ViewUtils {

    /**
     * 获取视图层级
     * @param view
     * @return
     */
    public static int getViewHierarchy(View view){
        int hierarchy = 1;
        if(view.getParent() != null){
            View targetView = view;
            ViewParent viewParent = targetView.getParent();
            while (viewParent != null){
                hierarchy++;
                viewParent = viewParent.getParent();
            }
        }
        return hierarchy;
    }

    public static void viewTree(View view){
        ViewParent viewParent = view.getParent();
        Log.d("Ellen2018","当前视图:"+view.getClass().getName());
        while (viewParent != null){
           View targetView = (View) viewParent;
            Log.d("Ellen2018","当前视图:"+targetView.getClass().getName());
            viewParent = viewParent.getParent();
        }
    }

}
