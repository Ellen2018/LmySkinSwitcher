package com.yalemang.skinswitcher.skin;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.yalemang.skinswitcherlibraray.bean.LmyRefreshSkin;
import com.yalemang.skinswitcherlibraray.skinnterface.LmySkinRefresh;

public class MyLmySkinRefresh implements LmySkinRefresh {
    @Override
    public void refresh(LmyRefreshSkin lmyRefreshSkin) {
        View view = lmyRefreshSkin.getView();
        int resId = lmyRefreshSkin.getResId();
        String attributeName = lmyRefreshSkin.getAttributeName();
        Resources skinResource = lmyRefreshSkin.getResources();
        Log.d("Ellen2018","控件所在界面:"+lmyRefreshSkin.getActivity().getClass().getName());
        Log.d("Ellen2018",attributeName+"->"+lmyRefreshSkin.getAttributeValue());
        Log.d("Ellen2018","resId = "+resId);
        if (attributeName.equals("textColor")) {
            TextView textView = (TextView) view;
            textView.setTextColor(skinResource.getColor(resId));
        }
        if (attributeName.equals("background")) {
            try {
                view.setBackgroundColor(skinResource.getColor(resId));
            }catch (Exception e){
                view.setBackground(skinResource.getDrawable(resId));
            }
        }
        if (attributeName.equals("tabIndicatorColor")) {
            //TabLayout下划线颜色
            TabLayout tabLayout = (TabLayout) view;
            tabLayout.setSelectedTabIndicatorColor(skinResource.getColor(resId));
        }
        if (attributeName.equals("tabSelectedTextColor")) {
            //TabLayout选中文本颜色
            TabLayout tabLayout = (TabLayout) view;
            tabLayout.setTabTextColors(Color.BLACK, skinResource.getColor(resId));
        }
    }
}
