package com.yalemang.skinswitcherlibraray;

import android.content.Context;
import android.content.res.TypedArray;

class LmySkinThemeUtils {

    public static int[] getResId(Context context, int[] attrs){
        int[] ints = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            ints[i] =  typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return ints;
    }
}