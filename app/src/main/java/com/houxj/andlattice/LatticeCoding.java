package com.houxj.andlattice;

import android.content.Context;

import com.houxj.andlattice.bean.CharInfo;
import com.houxj.andlattice.fonts.FontBase;
import com.houxj.andlattice.fonts.HZK12Font;
import com.houxj.andlattice.utils.JLogEx;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by 侯晓戬 on 2018/6/15.
 * 点阵编码类
 * 根据配置输出编码数据包
 */

public class LatticeCoding {
    private FontBase mFont; //字体
    private byte[] mHead = null;   //数据头

    LatticeCoding(Builder builder){
        createFont(builder.getContext(), builder.getFontType());
        createHead(builder);
    }
    //建立字库
    private void createFont(Context context, int fontType){
        switch (fontType){
            case LEDDefinition.FONT_TYPE_HZK12:{
                mFont = new HZK12Font(context);
            }
            break;
            default:{
                mFont = new HZK12Font(context);
            }
        }
    }

    //TODO 获取编码数据包
    public byte[] getCoding(String text){
        return mFont.getTextLattice(text);
    }

    //TODO 创建数据包
    public byte[] createDataPack(String text){
        byte[] btRet = null;
        byte[] code = getCoding(text);//拼合的数据
        btRet = Arrays.copyOf(mHead, mHead.length + code.length + 1);
        System.arraycopy(code,0,btRet, mHead.length,code.length);
        //处理字位图数据长度
        int mylen = getTextSaveLen(text);
        byte[] text_byte_len =String.format(Locale.CHINA,"%03d", mylen) .getBytes();
        System.arraycopy(text_byte_len, 0, btRet, LEDDefinition.DATA_MMM_INDEX,LEDDefinition.DATA_MMM_LEN);
        //校验位
        btRet[btRet.length -1] = cc(btRet);
        return btRet;
    }

    //创建数据头
    //数据头定义：BT030220nnmmmcNMxxxxYY
    //  标识字符 BT03020  点阵高度值 nn(例如12 则是0x31,0x32)
    //  位图宽度 mmm (字数*点阵高度，例如1个字就是012 表示0x30,0x31,0x32)
    //  颜色位，支持多色灯用 c (当前默认使用彩色 0x33)
    //  消息号 N （信息编码位） 移动方向 M
    //  xxxx : 保留位(默认0x31)，亮度等值，速度等级，闪烁开关
    //  YY : 后续帧位，兼容作用，暂时未使用，默认00（0x30,0x30）
    // 数据头总长度是2个字节固定长度（无校验位）
    private void createHead(Builder builder){
        mHead = new byte[LEDDefinition.DATA_HEAD_LEN];
        byte[] temp = LEDDefinition.DATA_HEAD_FALG.getBytes();
        int len = temp.length;
        int offset = 0;
        //标识
        System.arraycopy(temp,0,mHead,offset, len);
        offset += len;
        //高度
        temp = String.valueOf(mFont.getHeight()).getBytes();
        len = temp.length;
        System.arraycopy(temp,0,mHead, offset, len);
        offset += len;
        //位图宽(这里是空着的。到后面有文字了再填)
        offset += LEDDefinition.DATA_MMM_LEN;
        //颜色位
        mHead[offset++] = 0x33;
        //信息编码位， 移动方向
        mHead[offset++] = builder.getNewsId();
        mHead[offset++] = builder.getDirection();
        //保留位置 亮度等值，速度等级，闪烁开关
        mHead[offset++] = 0x31;
        mHead[offset++] = builder.getBrightness();
        mHead[offset++] = builder.getSpeed();
        mHead[offset++] = builder.getWinking();
        //后续帧位
        mHead[offset++] = 0x30;
        mHead[offset] = 0x30;
        JLogEx.d("%02x",mHead);
    }
    //校验位计算
    private byte cc(byte[] data){
        byte temp = 0;
        for (int i = 9; i < data.length - 1; i++) {//前面标签和高度不计算,最后一位是校验位所在
            temp ^= data[i];
            temp = (byte) (temp & 0xFF);
        }
        return temp;
    }
    //获取位图宽度位
    private int getTextSaveLen(String text){
        return text.length() * mFont.getHeight();
    }


    ////////////////////////////////////////////////
    public static class Builder{
        private int mFontType = LEDDefinition.FONT_TYPE_HZK12;//字库ID
        private byte mDirection = LEDDefinition.MOVE_ORNT_LEFT; //方向
        private byte mWinking = LEDDefinition.TWINKING_CLOSE;  //闪烁开关
        private byte mSpeed = LEDDefinition.MOVE_SPEED_LV_4;    //速度
        private byte mNewsId = LEDDefinition.NEWS_TEMP_ID;   //数据包ID
        private byte mBrightness = LEDDefinition.LIGHT_NEES_LV_4;   //亮度
        private final Context mContext;

        public Builder(Context context){
            mContext = context;
        }

        public Context getContext(){
            return mContext;
        }

        public int getFontType() {
            return mFontType;
        }

        public LatticeCoding.Builder setFontType(int fonttype) {
            this.mFontType = fonttype;
            return this;
        }

        public byte getDirection() {
            return mDirection;
        }

        public LatticeCoding.Builder setDirection(byte direction) {
            this.mDirection = direction;
            return this;
        }

        public byte getWinking() {
            return mWinking;
        }

        public LatticeCoding.Builder setWinking(boolean enable) {
            this.mWinking = LEDDefinition.TWINKING_CLOSE;
            if(enable){
                this.mWinking = LEDDefinition.TWINKING_OPEN;
            }
            return this;
        }

        public byte getSpeed() {
            return mSpeed;
        }

        public LatticeCoding.Builder setSpeed(byte speed) {
            this.mSpeed = speed;
            return this;
        }

        public byte getNewsId() {
            return mNewsId;
        }

        public LatticeCoding.Builder setNewsId(byte newsid) {
            this.mNewsId = newsid;
            return this;
        }

        public byte getBrightness() {
            return mBrightness;
        }

        public LatticeCoding.Builder setBrightness(byte brightness) {
            this.mBrightness = brightness;
            return this;
        }

        public LatticeCoding create(){
            return new LatticeCoding(this);
        }
    }

    //TODO 检查是否是ASCII码字符串
    public static boolean checkAsciiChar(String text){
        boolean bRet = true;
        int len = text.length();
        int value = 0;
        for (int i=0; i< len; i++){
            value = text.charAt(i);
            JLogEx.i("%d", value);
            if(value< 0 || value > 0x7F){
                bRet = false;
                break;
            }
        }
        return bRet;
    }
}
