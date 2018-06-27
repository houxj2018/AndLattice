package com.houxj.andlattice.fonts;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by 侯晓戬 on 2018/6/26.
 * 幼圆体 12 字库类
 */

public class YouYuan12Font extends GenFont12 {
    private final static String FONT_FILE_HZK12 = "fonts/YouYuan12";
    private static byte[] FONT_CACHE_DATA = null;

    public YouYuan12Font(Context context) {
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
