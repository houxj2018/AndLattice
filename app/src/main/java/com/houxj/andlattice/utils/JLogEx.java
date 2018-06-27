package com.houxj.andlattice.utils;

import android.util.Log;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by 侯晓戬 on 2018/6/15.
 * 调试日志类
 */

public class JLogEx {
    private final static int METHOD_INDEX = 5;
    private static boolean mDebugEnable = false;//是否输出Debug日志功能
    private static boolean mInfoEnable = false;//是否输出Info日志功能
    private static String TAG = "JLogEx";

    //TODO 是否开启Debug打印
    public static void setDebugEnable(boolean enable){
        mDebugEnable = enable;
    }
    //TODO 是否开启Info打印
    public static void setInfoEnable(boolean enable){
        mInfoEnable = enable;
    }
    //TODO 设置TAG
    public static void setTAG(String tag){
        TAG = tag;
    }

    //TODO DEBUG打印函数信息和格式化日志
    public static void d(String format, Object... args){
        d_log(getLogInfo() + " : " + String.format(Locale.CHINA,format,args));
    }
    //TODO DEBUG打印函数和行数
    public static void d(){
        d_log(getLogInfo());
    }
    //TODO 打印数组
    public static <T> void  d(T[] array){
        StringBuilder builder = new StringBuilder();
        if(null != array){
            builder.append("[");
            for (int i=0; i< array.length; i++){
                builder.append(array[i]);
                builder.append(",");
            }
            builder.append("]");
        }
        d_log(getLogInfo() + " : " + builder.toString());
    }
    public static <T> void  d(T[] array, int from, int len){
        StringBuilder builder = new StringBuilder();
        if(null != array && from< array.length){
            int end = from + len;
            if(end > array.length){
                end = array.length;
            }
            builder.append("[");
            for (int i = from; i< end; i++){
                builder.append(array[i]);
                builder.append(",");
            }
            builder.append("]");
        }
        d_log(getLogInfo() + " : " + builder.toString());
    }
    public static void d(String format,byte[] array){
        StringBuilder builder = new StringBuilder();
        if(null != array){
            builder.append("[");
            for (int i=0; i< array.length; i++){
                builder.append(String.format(format,array[i]));
                builder.append(",");
            }
            builder.append("]");
        }
        d_log(getLogInfo() + " : " + builder.toString());
    }
    public static void d(String format,byte[] array, int from, int len){
        StringBuilder builder = new StringBuilder();
        if(null != array && from< array.length){
            int end = from + len;
            if(end > array.length){
                end = array.length;
            }
            builder.append("[");
            for (int i = from; i< end; i++){
                builder.append(String.format(format,array[i]));
                builder.append(",");
            }
            builder.append("]");
        }
        d_log(getLogInfo() + " : " + builder.toString());
    }


    //TODO INFO打印函数信息和格式化日志
    public static void i(String format, Object... args){
        i_log(getLogInfo() + " : " + String.format(Locale.CHINA,format,args));
    }

    public static void i(String format,byte[] array,int from, int len){
        StringBuilder builder = new StringBuilder();
        if(null != array && from< array.length){
            int end = from + len;
            if(end > array.length){
                end = array.length;
            }
            builder.append("[");
            for (int i = from; i< end; i++){
                builder.append(String.format(format,array[i]));
                builder.append(",");
            }
            builder.append("]");
        }
        i_log(getLogInfo() + " : " + builder.toString());
    }

    //TODO 警告信息打印，不受调试开关控制
    public static void w(String log){
        Log.e(TAG+"(W)", getLogInfo() + " : " + log);
    }
    //TODO 打印调试日志
    private static void d_log(String log){
        if(mDebugEnable){
            Log.w(TAG+"(D)", log);
        }
    }
    //TODO 打印信息日志
    private static void i_log(String log){
        if(mInfoEnable){
            Log.i(TAG+"(I)", log);
        }
    }
    //获取函数方法的信息
    private static StackTraceElement getMethodInfo(int Level){
        StackTraceElement[] s = Thread.currentThread().getStackTrace();
        if(Level >= 0 && s.length >= Level){
//            Log.i(TAG, String.format("%s->%s->%s->%s->%s->%s",s[0].getMethodName(),s[1].getMethodName()
//                    ,s[2].getMethodName(),s[3].getMethodName(),s[4].getMethodName(),s[5].getMethodName()));
            return s[Level];
        }
        return null;
    }
    //获取不包含包名的类名
    private static String getSimpleName(String className){
        String simpleClassName = className;
        int lastIndex = simpleClassName.lastIndexOf(".") + 1;
        if(lastIndex >=0) {
            simpleClassName = simpleClassName.substring(lastIndex);
        }
        return simpleClassName;
    }
    //获取日志调用函数信息
    private static String getLogInfo(){
        StringBuilder builder = new StringBuilder();
        StackTraceElement myMethodCall = getMethodInfo(METHOD_INDEX + 1);
        if(null != myMethodCall){//调用者
            builder.append(getSimpleName(myMethodCall.getClassName()))
                    .append(".")
                    .append(myMethodCall.getMethodName())
                    .append("(")
                    .append(myMethodCall.getLineNumber())
                    .append(") -> ");
        }
        StackTraceElement myMethod = getMethodInfo(METHOD_INDEX);
        if(null != myMethod){
            builder.append(getSimpleName(myMethod.getClassName()))
                    .append(".")
                    .append(myMethod.getMethodName())
                    .append("(")
                    .append(myMethod.getLineNumber())
                    .append(")");
        }
        return builder.toString();
    }

}
