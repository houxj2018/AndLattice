package com.houxj.andlattice.fonts;

import android.content.Context;

import com.houxj.andlattice.bean.CharInfo;
import com.houxj.andlattice.utils.JLogEx;

/**
 * Created by 侯晓戬 on 2018/6/15.
 * 字体基础类（虚类）
 * 用于提取字的点阵数据
 */

public abstract class FontBase {
    FontBase(Context context){
        init(context);
    }
    //TODO 初始化字体类
    protected abstract void init(Context context);
    //TODO 获取字符对应字体的点阵数据
    public abstract byte[] getCharacteLattice(char text);
    //TODO 获取字符串点阵（多个字符拼合，每种点阵库拼合模式不一样）
    public byte[] getTextLattice(String text){
        byte[] btRet = null;
        if(null != text && text.length() > 0){
            btRet = getCharacteLattice(text.charAt(0));
            int len = text.length();
            for (int i =1; i< len; i++){
                btRet = mergeLatticeForRow(i,btRet,getCharacteLattice(text.charAt(i)));
            }
        }
        return btRet;
    }
    protected abstract byte[] mergeLatticeForRow(int index,  byte[] src,byte[] des);
    //TODO 获取字库字高
    public abstract int getHeight();
    //TODO ASCII码转GB2312码值（ascii码字符）
    protected byte[] AsciiToGB2312(byte ascii){
        JLogEx.d("0x%x",ascii);
        byte[] gb2312 = new byte[2];
        if(0x20 == ascii){//空格
            gb2312 = new byte[]{(byte) 0xA1, (byte) 0xA1};
        }else{
            gb2312[0] = (byte) 0xA3;
            if (ascii >= 0x41 && ascii <= 0x5A) {
                gb2312[1] = (byte) (0xC1 + (ascii - 0x41));
            } else if (ascii >= 'a' && ascii <= 'z') {
                gb2312[1] = (byte) (0xE1 + (ascii - 0x61));
            } else if (ascii >= '0' && ascii <= '9') {
                gb2312[1] = (byte) (0xB0 + (ascii - 0x30));
            }else{
                gb2312 = new byte[]{(byte) 0xA1, (byte) 0xA1};//不支持的都显示空格
            }
        }
        return gb2312;
    }
}
