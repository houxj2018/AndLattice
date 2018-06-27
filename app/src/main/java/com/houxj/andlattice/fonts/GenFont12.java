package com.houxj.andlattice.fonts;

import android.content.Context;

import com.houxj.andlattice.utils.JLogEx;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by 侯晓戬 on 2018/6/26.
 * 通用12点阵字库的处理类
 */

public abstract class GenFont12 extends FontBase {
    private final static String FONT_ENCODE = "GB2312";
    private final static int FONT_LATTICE_H = 12;
    private final static int FONT_HZK12_LEN = (FONT_LATTICE_H*2);   //每个字点阵数据的长度（12行，每行2个字节（12位需要2个字节））

    public GenFont12(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context) {
        initHzk12Data(context);
    }

    @Override
    public byte[] getCharacteLattice(char text) {
        byte[] data = getFontCache();
        if(null != data){
            try {
                byte[] codeValue = String.valueOf(text).getBytes(FONT_ENCODE);
                if(codeValue.length ==1){//ascii码处理
                    codeValue = AsciiToGB2312(codeValue[0]);
                }
                if(codeValue.length == 2){//中文的是2位，其他的先不处理
                    int h = codeValue[0] & 0xFF;//byte保存不能超过0x80 ，超过0x80的会变成负数
                    int l = codeValue[1] & 0xFF;
                    int offset = ((h - 0xA1) * 94 + (l - 0xA1)) * FONT_HZK12_LEN;//
                    JLogEx.d("offset = %d h=%d(0x%02X),l=%d(0x%02X) ", offset,h,codeValue[0],l,codeValue[1]);
                    if (data.length > offset && offset >= 0) {
                        return Arrays.copyOfRange(data, offset, offset + FONT_HZK12_LEN);
                    }
                }
            }catch (UnsupportedEncodingException e){
                JLogEx.w(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 字符点阵拼合图示
     *       1个字符的数据    ->         2个拼合后数据        ->        3个拼合后数据
     * 0  xxxxxxxx xxxx0000       xxxxxxxx xxxxyyyy yyyyyyyy        xxxxxxxx xxxxyyyy yyyyyyyy zzzzzzzz zzzz0000
     * 1  xxxxxxxx xxxx0000       xxxxxxxx xxxxyyyy yyyyyyyy        xxxxxxxx xxxxyyyy yyyyyyyy zzzzzzzz zzzz0000
     *           .                       .                                 .
     *           .                       .                                 .
     * 11 xxxxxxxx xxxx0000       xxxxxxxx xxxxyyyy yyyyyyyy        xxxxxxxx xxxxyyyy yyyyyyyy zzzzzzzz zzzz0000
     *
     * 最后的数据存储位置 btye[0,1,....11] 如3个的时候是：
     *  [xx,xy,yy,xx,xy,yy,xx,xy,yy,xx,xy,yy,xx,xy,yy, ...xx,xy,yy,]
     *  [xx,xy,yy,zz,z0,xx,xy,yy,zz,z0,xx,xy,yy,zz,z0,xx,xy,yy,zz,z0,xx,xy,yy,zz,z0,....xx,xy,yy,zz,z0,]
     */

    //进行行拼合(将des数据按行拼接到src中)
    // 参数 index 是 目标 des 参数的文字在字符串中序号
    // index 会从1开始，因为第一个字的数据不需要拼接
    @Override
    protected byte[] mergeLatticeForRow(int index, byte[] src, byte[] des) {
        byte[] btRet = null;
        if(null != src){
            if(null != des){
                int merge_row_len = (index + 1) / 2 * 3 + (index+1) % 2 * 2; //合并后一行所占用的字节数
                btRet = new byte[merge_row_len * FONT_LATTICE_H];  //这个是合并后的数据存放区
                int src_row_len = src.length / FONT_LATTICE_H;  //源数据一行所占用的字节数
                int des_row_len = des.length / FONT_LATTICE_H;  //目标数据一行所占用的字节数
                int src_index = 0,des_index = 0, merge_index = 0;
                byte temp_src, temp_des;
                JLogEx.d("src_row_len=%d, des_row_len=%d, merge_row_len=%d", src_row_len, des_row_len, merge_row_len);
                for (int i=0; i< FONT_LATTICE_H; i++){//一行一行处理
                    src_index = i * src_row_len;
                    des_index = i * des_row_len;
                    merge_index = i * merge_row_len;
                    JLogEx.i("src_index=%d, des_index=%d, merge_index=%d", src_index, des_index, merge_index);
                    System.arraycopy(src,src_index,btRet,merge_index,src_row_len);//先将源数据行拷贝进入
                    JLogEx.i("0x%02X",btRet,merge_index,merge_row_len);
                    JLogEx.i("0x%02X",des,des_index,des_row_len);
                    if(index % 2 == 1){
                        //为了更好的利用数据，偶数位置的数据将拼接在前一个字的后一个字节的后4位上，
                        // 同时自己的数据要前移动，并抛弃自己的后一个字节
                        // 1、取源数据行最后一个字节(只有2个字节，所以直接+1)
                        temp_src = src[src_index + 1];
                        // 2、取目标数据行的第一个字节
                        temp_des = des[des_index];
                        JLogEx.i("2 0x%02X, 0x%02X", temp_src,temp_des);
                        // 3、源数据最后一个字节的后4位清零（他本身也是零，为了确保）
                        temp_src = (byte) (temp_src & 0xF0);
                        // 4、然后与目标数据第一个字节前4位相或（目标数据第一字节前4位填充到源数据最后一个字节后4位）
                        btRet[merge_index + 1] = (byte)(temp_src | (temp_des >> 4) & 0xFF);
                        JLogEx.i("4 0x%02X", btRet[merge_index + 1]);
                        // 5、目标数据第一字节后4位左移4位，第2字节前4位填充到第一字节后4位(并放入合并数据区当前行最后一个字节上)
                        temp_des = (byte) ((temp_des << 4)& 0xFF);
                        temp_des = (byte) (((des[des_index + 1] & 0xF0) >> 4 | temp_des) & 0xFF);
                        btRet[merge_index + merge_row_len -1] = temp_des;
                        JLogEx.i("5 0x%02X", temp_des);
                    }else{
                        //奇数位置的数据，因为前面的数据是刚好对齐的，直接按行拼接
                        System.arraycopy(des,des_index,btRet,merge_index + src_row_len,des_row_len);//再将目标数据行拷贝进入
                    }
                    JLogEx.i("0x%02X",btRet,merge_index,merge_row_len);
                }
            }else{
                return src;//如果目标数据为空，则直接返回源数据
            }
        }
        return btRet;
    }

    @Override
    public int getHeight() {
        return FONT_LATTICE_H;
    }

    private void initHzk12Data(Context context){
        if(null == getFontCache()) {
            try {
                InputStream in = context.getResources().getAssets().open(getFontFilePath());
                int lenght = in.available();
                byte[] temp = new byte[lenght + 1];
                in.read(temp);
                in.close();
                setFontCache(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //获取字库文件名称
    protected abstract String getFontFilePath();
    //设置字库缓存数据
    protected abstract void setFontCache(byte[] data);
    //获取字库缓存数据
    protected abstract byte[] getFontCache();

}
