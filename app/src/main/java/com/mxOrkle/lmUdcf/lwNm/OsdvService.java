package com.mxOrkle.lmUdcf.lwNm;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.mxOrkle.lmUdcf.utils.LogUtils;
import com.mxOrkle.lmUdcf.v4.Nullable;


/**
 * Created by wuqiyan on 17/6/7.
 */

public class OsdvService extends Service {
    static final int NOTIFY_ID = 9521;
    private static OsdvService instance;
    public static OsdvService getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(MConstant.TAG,"OsdvService onCreate()...");
        instance = this;

    }

    static void startKernel() {
        try {
            Intent intent = new Intent(instance.getApplicationContext(), MService.class);
            instance.getApplicationContext().startService(intent);
        } catch (Exception e) {

        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i(MConstant.TAG,"OsdvService onStartCommand()...");
        startKernel();
        return super.onStartCommand(intent, flags, startId);
    }


    public static class MService extends Service {
        private Context mContext;
        klpsdk adSDK = klpsdk.getInstance();
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            LogUtils.i(MConstant.TAG,"MService onCreate...");
            mContext = getApplicationContext();
            adSDK.init(mContext);
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            try {
                LogUtils.i(MConstant.TAG,"MService onStartCommand...");
                OsdvService fakeService = OsdvService.getInstance();
                fakeService.startForeground(NOTIFY_ID, new Notification());
                startForeground(NOTIFY_ID, new Notification());
                fakeService.stopForeground(true);
            } catch (Exception e) {

            }
            adSDK.startAd(intent,getApplication());

            return START_STICKY;
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
        }

    }

}
