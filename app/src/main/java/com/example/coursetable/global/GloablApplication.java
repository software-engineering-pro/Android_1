package com.example.coursetable.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by lyw on 2017/5/15.
 * 获取全局上下文对象
 * 在所有组件之前初始化。
 */

public class GloablApplication extends Application {


    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();

        //Fresco.initialize(this);
    }
    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }


}
