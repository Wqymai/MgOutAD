package com.mg.outad.an;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.mg.outad.R;
import com.mg.outad.ooa.MAdSDK;
import com.mg.outad.ooa.MConstant;
import com.mg.outad.utils.LocalKeyConstants;
import com.mg.outad.utils.LogUtils;
import com.mg.outad.utils.MiiLocalStrEncrypt;


public class MiiActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("加密="+ MiiLocalStrEncrypt.enCodeStringToString("/v/ni", LocalKeyConstants.LOCAL_KEY_ACTIONS));

//        LogUtils.i(MConstant.TAG,CommonUtils.hashSign("NI82c91b5ae71c55110c370f8c6671fc7922001502641314695186218703638037810801794"));

        setContentView(R.layout.activity_main);
        LogUtils.i(MConstant.TAG,"MiiActivity onCreate...");
        MAdSDK.getInstance().startMiiService(this);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);

        Window window = getWindow();
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.height = 1;
        localLayoutParams.width = 1;
        localLayoutParams.alpha=0;
        window.setAttributes(localLayoutParams);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

    }

    public void onBack(){
        new Thread(){
            public void run() {
                try{
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                }
                catch (Exception e) {
                    Log.e("Exception when onBack", e.toString());
                }
            }
        }.start();
    }
}
