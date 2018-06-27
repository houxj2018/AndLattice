package com.houxj.andlattice.fonts;

import android.content.Context;

import com.houxj.andlattice.utils.JLogEx;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by 侯晓戬 on 2018/6/25.
 * Ascii 码的字库
 */

public class Ascii12Font extends FontBase {
    private final static String FONT_FILE_ASC12 = "fonts/ASCZ12";
    private final static String FONT_ENCODE = "ASCII";
    private final static int FONT_LATTICE_H = 12;
    private final static int FONT_LATTICE_ASC12_W = 8;
    private final static int FONT_HZK12_LEN = (FONT_LATTICE_H);   //每个字点阵数据的长度（12行，每行1个字节（8位需要1个字节））

    private static byte[] FONT_ASCZ12_DATA = null;

    public Ascii12Font(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context) {
        initAsc12Data(context);
    }

    @Override
    public byte[] getCharacteLattice(char text) {
        if(null != FONT_ASCZ12_DATA){
            try {
                byte[] codeValue = String.valueOf(text).getBytes(FONT_ENCODE);
                if(codeValue.length > 1){
                    codeValue = new byte[]{0x20};//所有非ascii都使用空格显示
                }
                if(codeValue.length == 1){//中文的是2位，其他的先不处理
                    int index = codeValue[0] & 0xFF;//byte保存不能超过0x80 ，超过0x80的会变成负数
                    int offset = index * FONT_HZK12_LEN;//
                    JLogEx.d("offset = %d index=%d(0x%02X)", offset,index,codeValue[0]);
                    if (FONT_ASCZ12_DATA.length > offset && offset >= 0) {
                        return Arrays.copyOfRange(FONT_ASCZ12_DATA, offset, offset + FONT_HZK12_LEN);
                    }
                }
            }catch (UnsupportedEncodingException e){
                JLogEx.w(e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected byte[] mergeLatticeForRow(int index, byte[] src, byte[] des) {
        byte[] btRet = null;
        if(null != src){
            if(null != des){
                int merge_row_len = (index + 1); //合并后一行所占用的字节数
                btRet = new byte[merge_row_len * FONT_LATTICE_H];  //这个是合并后的数据存放区
                int src_row_len = src.length / FONT_LATTICE_H;  //源数据一行所占用的字节数
                int des_row_len = des.length / FONT_LATTICE_H;  //目标数据一行所占用的字节数
                int src_index = 0,des_index = 0, merge_index = 0;
                JLogEx.d("src_row_len=%d, des_row_len=%d, merge_row_len=%d", src_row_len, des_row_len, merge_row_len);
                for (int i=0; i< FONT_LATTICE_H; i++){//一行一行处理
                    src_index = i * src_row_len;
                    des_index = i * des_row_len;
                    merge_index = i * merge_row_len;
                    JLogEx.i("src_index=%d, des_index=%d, merge_index=%d", src_index, des_index, merge_index);
                    System.arraycopy(src,src_index,btRet,merge_index,src_row_len);//先将源数据行拷贝进入
                    JLogEx.i("0x%02X",btRet,merge_index,merge_row_len);
                    JLogEx.i("0x%02X",des,des_index,des_row_len);
                    //前面的数据是刚好对齐的，直接按行拼接
                    System.arraycopy(des,des_index,btRet,merge_index + src_row_len,des_row_len);//再将目标数据行拷贝进入
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

    private static void initAsc12Data(Context context){
        if(null == FONT_ASCZ12_DATA) {
            try {
                InputStream in = context.getResources().getAssets().open(FONT_FILE_ASC12);
                int lenght = in.available();
                FONT_ASCZ12_DATA = new byte[lenght + 1];
                in.read(FONT_ASCZ12_DATA);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
