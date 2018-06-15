package com.mxOrkle.lmUdcf.aplication;

import android.app.Application;

import com.mxOrkle.lmUdcf.lwNm.klpsdk;

/**
 * Created by wuqiyan on 17/11/10.
 */

public class MiiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        klpsdk.getInstance().setApli();
    }
}
