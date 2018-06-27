package com.houxj.andlattice.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.houxj.andlattice.LEDDefinition;
import com.houxj.andlattice.utils.CommonUtils;
import com.houxj.andlattice.utils.HTimer;
import com.houxj.andlattice.utils.JLogEx;

import java.util.Arrays;

/**
 * Created by 侯晓戬 on 2018/6/20.
 * 模拟LED灯板显示的视图
 */

public class LedView extends View {
    private final static int LIGHT_SIZE = 8;
    private final static int DEF_LIGHT_NUMBER_H = 12; //默认高度12颗灯
    private final static int DEF_LIGHT_NUMBER_W = 36;    //默认灯板宽度36颗灯
    private final static int DEF_LIGHT_CR = Color.CYAN;//默认灯的颜色
    private final static int DEF_LIGHT_BOARD_CR = Color.DKGRAY;//默认灯板的颜色
    private final static int DEF_LIGHT_TWINK_TIME = 8;    //默认闪烁时间(定时器100跑一次)

    private Paint mLightPaint;
    private RectF mLightRect;

    private int mLightPixelSize = LIGHT_SIZE*2;
    //属性值
    private int mLightNumberH = DEF_LIGHT_NUMBER_H; //灯板高度灯颗数
    private int mLightNumberW = DEF_LIGHT_NUMBER_W; //灯板宽度灯颗数
    private int mLightColor = DEF_LIGHT_CR;
    private int mLightBoardColor = DEF_LIGHT_BOARD_CR;
    //数据
    private byte[] mLEDData = new byte[]{0};//默认无数据
    private byte mMoveDirection = LEDDefinition.MOVE_ORNT_LEFT;
    private int mMoveSpeedTime = 1;  //毫秒数
    private boolean mTwinkingFalg = false;//是否闪烁

    private int mLatticeRowBitLen = 0; //字符点阵图一行的位数长度（总长度/行数 *8）

//    private HTimer mTimer = new HTimer();
//    private int mTimerNumberTWINKING = 0;
//    private int mTimerNumberMove = 0;
//
//    private int mMoveOffset = 0;    //移动偏移

    public LedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs){

    }

    private void init(){
        mLightPixelSize = LIGHT_SIZE*(int) CommonUtils.getDisplayDensity(getContext());

        mLightPaint = new Paint();
        mLightPaint.setAntiAlias(true);// 设置是否抗锯齿
        mLightPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 帮助消除锯齿
        mLightPaint.setColor(mLightColor);
        mLightPaint.setStrokeWidth(1);
        mLightPaint.setStyle(Paint.Style.FILL);// 设置样式

        mLightRect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mLightNumberW * mLightPixelSize + getPaddingLeft() + getPaddingRight();
        int height = mLightNumberH * mLightPixelSize + getPaddingTop() + getPaddingBottom();
        mLightRect.set(getPaddingLeft(), getPaddingTop(),width - getPaddingRight(),height - getPaddingBottom());
        setMeasuredDimension(width, height);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mLightBoardColor);//绘制视图背景（灯板）
        drawLEDLight(canvas);
    }

    //绘制LED灯效果
    private void drawLEDLight(Canvas canvas){
        int cir_r = mLightPixelSize / 2;
        int cx = 0,cy = 0;

        //画灯
        for (int y = 0; y < mLightNumberH; y++) {
            for (int x = 0; x < mLightNumberW; x++) {
                mLightPaint.setStyle(Paint.Style.STROKE);
                mLightPaint.setColor(DEF_LIGHT_CR);
                if(checkLightState(x,y)){
                    mLightPaint.setStyle(Paint.Style.FILL);
                    mLightPaint.setColor(mLightColor);
                }
                cx = (int) ((x * mLightPixelSize) + cir_r + mLightRect.left);
                cy = (int) ((y * mLightPixelSize) + cir_r + mLightRect.top);
                canvas.drawCircle(cx, cy, cir_r - 2, mLightPaint);
            }
        }
    }

    //从数据中检查 x,y 对应灯是否需要开启
    private boolean checkLightState(int x,int y){
        boolean bRet = false;
        synchronized (this){
            if(null != mLEDData && x < mLatticeRowBitLen){
                int value = y * mLatticeRowBitLen + x;
                int index = value / 8;  //字节序号
                int bit = value % 8;    //位序号
                JLogEx.i("value=%d,index=%d,bit=%d", value, index, bit);
                if((mLEDData[index] & (0x80>>bit)) != 0x00) {//灯是亮的
                    bRet = true;
                }
            }
        }
        return bRet;
    }

    //TODO 设置点阵数据
    public void setLatticeData(byte[] data){
        synchronized (this){
            if(null != data) {
                mLEDData = Arrays.copyOf(data, data.length);
                mLatticeRowBitLen = (data.length / 12) * 8;
            }
        }
        JLogEx.d("%s,mLatticeRowBitLen=%d", mLEDData, mLatticeRowBitLen);
        postInvalidate();
    }

    //TODO 设置数据包（包括控制信息）
    //数据包包括：点阵高度，移动方向，灯亮度，移动速度，是否闪烁，点阵数据
    public void setDataPack(byte[] data){
        synchronized (this) {
            //解析数据
            int index = LEDDefinition.DATA_MMM_INDEX + LEDDefinition.DATA_MMM_LEN;
            byte cr = data[index];//颜色位
            byte id = data[index + 1];//id
            mMoveDirection = data[index + 2];//移动方向
            setLightLightness(data[index + 4]);//亮度
            setMoveSpeedMiSec(LEDDefinition.MOVE_SPEED_LV_0/*data[index + 5]*/);   //速度
            mTwinkingFalg = false;
            if (data[index + 6] == 0x31) { //是否闪烁
                mTwinkingFalg = true;
            }
        }
        setLatticeData(Arrays.copyOfRange(data, LEDDefinition.DATA_HEAD_LEN,data.length -1));
//        starTimer();//开启定时任务（移动，闪烁）
    }
    //亮度转换
    private void setLightLightness(byte value){
        int lightness = value - LEDDefinition.LIGHT_NEES_LV_0;
        float mylightness = lightness / 20.0f;
        mylightness += 0.6f;
        int color_val = (int) (255 * mylightness);
//        mLightColor = Color.rgb(0,color_val,color_val);
        JLogEx.d("%d, %04f, %d, %X", lightness,mylightness,color_val, mLightColor);
    }
    //移动速度转换
    private void setMoveSpeedMiSec(byte level){
        int myLevel = level - LEDDefinition.MOVE_SPEED_LV_0;
        mMoveSpeedTime = (60 + myLevel * 50) / 100;
    }

//    //启动定时器
//    private void starTimer(){
//        mTimer.interval(100, new HTimer.ITimeNext() {
//            @Override
//            public void doNext() {
//                runTimerTask();
//            }
//        });
//    }
//
//    //定时任务执行
//    private void runTimerTask(){
////        JLogEx.i("");
//        if(mTwinkingFalg){
//            mTimerNumberTWINKING ++;
//            checkTwinking();
//        }
//        mTimerNumberMove ++;
//        if(mTimerNumberMove >= mMoveSpeedTime) {
////            JLogEx.d("%d, %d, mMoveOffset=%d", mTimerNumberMove, mMoveSpeedTime, mMoveOffset);
//            mMoveOffset++;    //移动记数
//            mTimerNumberMove = 0;
//            postInvalidate(); //刷新绘制
//        }
//
//    }
//
//    //闪烁控制检测
//    private boolean checkTwinking(){
//        boolean bRet = false;
//        if(mTwinkingFalg){
//            if(mTimerNumberTWINKING > DEF_LIGHT_TWINK_TIME){
//                bRet = true;
//                if(mTimerNumberTWINKING == (DEF_LIGHT_TWINK_TIME+ 2)){
//                    mTimerNumberTWINKING = 0;//熄灭2秒后显示
//                }
//                postInvalidate(); //刷新绘制
//            }
//        }else{
//            mTimerNumberTWINKING = 0;
//        }
//        return bRet;
//    }
//    //左移处理
//    // value 点的序号值（即每个位在整个数据中位置）
//    private int moveLeftHandr(int value){
//        if(mMoveOffset >= mLatticeRowBitLen){
//            mMoveOffset = 0;
//        }
//        JLogEx.i("%d + %d = %d", mMoveOffset,value,value + mMoveOffset);
//        return value + mMoveOffset;
//    }

}
