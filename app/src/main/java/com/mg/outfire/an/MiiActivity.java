//package com.mg.outad.an;
//
//import android.app.Activity;
//import android.app.Instrumentation;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.Window;
//import android.view.WindowManager;
//
//import R;
//import MAdSDK;
//import MConstant;
//import LogUtils;
//
//
//public class MiiActivity extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        LogUtils.i(MConstant.TAG,"MiiActivity onCreate...");
//        MAdSDK.getInstance().startMiiService(this);
//        Handler h = new Handler();
//        h.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finish();
//            }
//        },1000);
//
//        Window window = getWindow();
//        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
//        localLayoutParams.height = 1;
//        localLayoutParams.width = 1;
//        localLayoutParams.alpha=0;
//        window.setAttributes(localLayoutParams);
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
//
//    }
//
//    public void onBack(){
//        new Thread(){
//            public void run() {
//                try{
//                    Instrumentation inst = new Instrumentation();
//                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//                }
//                catch (Exception e) {
//                    Log.e("Exception when onBack", e.toString());
//                }
//            }
//        }.start();
//    }
//}