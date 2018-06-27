package com.houxj.andlattice.fonts;

import android.content.Context;

import com.houxj.andlattice.bean.CharInfo;
import com.houxj.andlattice.utils.JLogEx;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by 侯晓戬 on 2018/6/15.
 * HZK12 字体点阵库的
 */

public class HZK12Font extends GenFont12 {
    private final static String FONT_FILE_HZK12 = "fonts/HZKSZ12";
    private static byte[] FONT_CACHE_DATA = null;

    public HZK12Font(Context context) {
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
