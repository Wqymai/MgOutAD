package com.wqy.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by wuqiyan on 17/8/4.
 */

public class LService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ci","LService onCreate()...");
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        Log.i("ci","LService onStartCommand()...");
        return super.onStartCommand(intent, flags, startId);
    }
}
