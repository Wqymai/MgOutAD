package com.mxOrkle.lmUdcf.an;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.mg.outfire.R;
import com.mxOrkle.lmUdcf.lwNm.MConstant;
import com.mxOrkle.lmUdcf.lwNm.klpsdk;
import com.mxOrkle.lmUdcf.utils.LogUtils;


public class MiiActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtils.i(MConstant.TAG,"MiiActivity onCreate...");
        klpsdk.getInstance().startNi(this);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                klpsdk.getInstance().startMiiService(MiiActivity.this);
            }
        },3000);

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

    }

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
}
