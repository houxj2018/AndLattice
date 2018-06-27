package com.houxj.andlattice.bean;

import java.util.Arrays;

/**
 * Created by 侯晓戬 on 2018/6/20.
 * 文字点阵信息类
 * 保持点阵数据和高宽
 */

public class CharInfo {
    private byte[] mData;   //点阵数据
    private int mWidth; //点阵宽度
    private int mHeight;    //点阵高度

    public CharInfo(byte[] data, int width, int height){
        this.mData = Arrays.copyOf(data, data.length);
        this.mWidth = width;
        this.mHeight = height;
    }

    public byte[] getData() {
        return mData;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
