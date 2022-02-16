package com.yalemang.skinswitcherlibraray;

import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yalemang.skinswitcherlibraray.bean.LmyRefreshSkin;
import java.util.List;

class LmySkinAttribute {

    //过滤出皮肤需要的属性
    private static List<String> ATTRIBUTE;

    public LmySkinAttribute() {
        //过滤需要修改的皮肤属性
        ATTRIBUTE =  LmySkinManager.getInstance().getLmySkinSwitcherSetting().interceptFieldNames();
    }

    public void loadView(View view, AttributeSet attributeSet) {
        for (int i = 0; i < attributeSet.getAttributeCount(); i++) {
            String attributeName = attributeSet.getAttributeName(i);
            if (ATTRIBUTE.contains(attributeName)) {
                String attributeValue = attributeSet.getAttributeValue(i);
                if (attributeValue.startsWith("#")) {
                    //固定的Color值，无需修改
                } else {
                    int resId = 0;
                    //判断前缀是否为？
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    if (attributeValue.startsWith("?")) {
                        int[] array = {attrId};
                        resId = LmySkinThemeUtils.getResId(view.getContext(), array)[0];
                    } else {
                        resId = attrId;
                    }
                    if (resId != 0) {
                        LmySkin lmySkin = LmySkinManager.getInstance().getCurrentSkin();
                        LmySkinLoadApkPath skinLoadApkPath = new LmySkinLoadApkPath();
                        skinLoadApkPath.loadEmptyApkPath(lmySkin.getPath());
                        Resources skinResource = skinLoadApkPath.getSkinResource();
                        //将数据回调给接口，让使用者自行处理
                        LmyRefreshSkin lmyRefreshSkin = new LmyRefreshSkin();
                        lmyRefreshSkin.setAttributeName(attributeName);
                        lmyRefreshSkin.setAttributeValue(attributeValue);
                        lmyRefreshSkin.setView(view);
                        lmyRefreshSkin.setResId(resId);
                        lmyRefreshSkin.setResources(skinResource);
                        LmySkinManager.getInstance().getLmySkinSwitcherSetting().lmySkinRefresh().refresh(lmyRefreshSkin);
                    }
                }
            }
        }

    }

}
