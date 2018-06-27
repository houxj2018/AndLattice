package com.houxj.andlattice.fonts;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by 侯晓戬 on 2018/6/15.
 * 叶根友行书 12 字体点阵库的
 */

public class YeGenYou12Font extends GenFont12 {
    private final static String FONT_FILE_HZK12 = "fonts/YeGenYouXingShu12";
    private static byte[] FONT_CACHE_DATA = null;

    public YeGenYou12Font(Context context) {
        super(context);
    }

    @Override
    protected String getFontFilePath() {
        return FONT_FILE_HZK12;
    }

    @Override
    protected void setFontCache(byte[] data) {
        FONT_CACHE_DATA = Arrays.copyOf(data,data.length);
    }

    @Override
    protected byte[] getFontCache() {
        return FONT_CACHE_DATA;
    }
}
