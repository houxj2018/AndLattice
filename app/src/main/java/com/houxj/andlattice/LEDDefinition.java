package com.houxj.andlattice;

/**
 * Created by 侯晓戬 on 2018/6/22.
 * LED配置定义
 */

public final class LEDDefinition {
    public final static String DATA_HEAD_FALG = "BT03020";
    public final static int DATA_MMM_INDEX = 9;//位图宽度位在数据包中起始序号
    public final static int DATA_MMM_LEN = 3;//位图宽度位在数据包中长度
    public final static int DATA_HEAD_LEN = 21;//数据头固定长度(无校验位)
    //灯的移动方向定义
    public final static byte MOVE_ORNT_LEFT = 0x33;     //左移
    public final static byte MOVE_ORNT_RIGHT = 0x34;    //右移
    public final static byte MOVE_ORNT_UP = 0x36;       //上移
    public final static byte MOVE_ORNT_DOWN = 0x37;     //下移
    public final static byte MOVE_ORNT_NO = 0x38;       //静止
    //闪烁开关
    public final static byte TWINKING_CLOSE = 0x30;      //关闭闪烁
    public final static byte TWINKING_OPEN = 0x31;      //开启闪烁
    //移动速度
    public final static byte MOVE_SPEED_LV_0 = 0x30;     //移动速度等级 0
    public final static byte MOVE_SPEED_LV_1 = 0x31;     //移动速度等级 1
    public final static byte MOVE_SPEED_LV_2 = 0x32;     //移动速度等级 2
    public final static byte MOVE_SPEED_LV_3 = 0x33;     //移动速度等级 3
    public final static byte MOVE_SPEED_LV_4 = 0x34;     //移动速度等级 4
    public final static byte MOVE_SPEED_LV_5 = 0x35;     //移动速度等级 5
    public final static byte MOVE_SPEED_LV_6 = 0x36;     //移动速度等级 6
    public final static byte MOVE_SPEED_LV_7 = 0x37;     //移动速度等级 7
    public final static byte MOVE_SPEED_LV_8 = 0x38;     //移动速度等级 8
    //灯的亮度
    public final static byte LIGHT_NEES_LV_0 = 0x30;     //灯的亮度等级 0
    public final static byte LIGHT_NEES_LV_1 = 0x31;     //灯的亮度等级 1
    public final static byte LIGHT_NEES_LV_2 = 0x32;     //灯的亮度等级 2
    public final static byte LIGHT_NEES_LV_3 = 0x33;     //灯的亮度等级 3
    public final static byte LIGHT_NEES_LV_4 = 0x34;     //灯的亮度等级 4
    public final static byte LIGHT_NEES_LV_5 = 0x35;     //灯的亮度等级 5
    public final static byte LIGHT_NEES_LV_6 = 0x36;     //灯的亮度等级 6
    public final static byte LIGHT_NEES_LV_7 = 0x37;     //灯的亮度等级 7
    public final static byte LIGHT_NEES_LV_8 = 0x38;     //灯的亮度等级 8
    //信息编码位
    public final static byte NEWS_TEMP_ID = 0x30;     //临时信息编码，对应一键编辑
    public final static byte NEWS_SAVE_ID = 0x31;     //存储信息编码，对应广告接收
    public final static byte NEWS_ASCII_ID = 0x32;     //纯Ascii码标识信息编码

    //字体定义
    public final static int FONT_TYPE_HZK12 = 1;    //HZK12 GB2312字库
    public final static int FONT_TYPE_ASCII12 = 2;    //ASCII12字库

}
