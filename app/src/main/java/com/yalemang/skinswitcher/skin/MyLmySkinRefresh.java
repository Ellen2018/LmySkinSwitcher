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
        if (attributeName.equals("textColor")) {
            TextView textView = (TextView) view;
            textView.setTextColor(skinResource.getColorStateList(resId));
        }
        if (attributeName.equals("background")) {
            view.setBackgroundColor(skinResource.getColor(resId));
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