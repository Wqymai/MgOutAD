//package com.mg.outad.an;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//
//import com.mg.outad.ooa.MConstant;
//import com.mg.outad.utils.LogUtils;
//
///**
// * Created by wuqiyan on 17/8/10.
// */
//
//public class StartService extends Service {
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        LogUtils.i(MConstant.TAG,"服务onCreate");
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        LogUtils.i(MConstant.TAG,"服务onStartCommand");
//        Intent i = new Intent(this,MiiActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
//        return super.onStartCommand(intent, flags, startId);
//    }
//}
