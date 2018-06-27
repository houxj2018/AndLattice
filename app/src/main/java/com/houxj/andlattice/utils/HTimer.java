package com.houxj.andlattice.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 侯晓戬 on 2018/6/25.
 * 定时器类
 */

public class HTimer {
    public interface ITimeNext{
        public void doNext();
    }
    private ITimeNext mCallBack = null;
    private Timer mTimer = null;
    private TimerTask mTask = null;

    public void timer(long milliseconds,final ITimeNext next){
        createTimer();
        mCallBack = next;
        mTimer.schedule(mTask, milliseconds);
    }

    public void interval(long milliseconds,final ITimeNext next){
        createTimer();
        mCallBack = next;
        mTimer.schedule(mTask,milliseconds, milliseconds);
    }

    public void cancel(){
        if(null != mTimer){
            mTimer.cancel();
            mTimer = null;
        }
        mTask = null;
    }

    private void createTimer(){
        cancel();
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                if(null != mCallBack){
                    mCallBack.doNext();
                }
            }
        };
    }

}
