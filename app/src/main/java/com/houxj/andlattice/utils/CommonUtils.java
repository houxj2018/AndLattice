package com.houxj.andlattice.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by 侯晓戬 on 2018/6/20.
 * 共用工具类
 */

public class CommonUtils {
    //TODO 屏幕密度（像素比例）
    public static float getDisplayDensity(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }
}
