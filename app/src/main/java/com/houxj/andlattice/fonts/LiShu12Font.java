package com.houxj.andlattice.fonts;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by Administrator on 2018/6/26.
 */

public class LiShu12Font extends GenFont12 {
    private final static String FONT_FILE_NAME = "fonts/LiShu12";
    private static byte[] FONT_CACHE_DATA = null;

    public LiShu12Font(Context context) {
        super(context);
    }

    @Override
    protected String getFontFilePath() {
        return FONT_FILE_NAME;
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
