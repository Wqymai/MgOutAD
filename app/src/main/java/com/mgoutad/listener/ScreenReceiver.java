package com.mgoutad.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mgoutad.ooa.MConstant;
import com.mgoutad.ooa.MiiService;


public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MiiService.MService.class);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            i.putExtra(MConstant.sence.screen_on, true);
        }else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            i.putExtra(MConstant.sence.screen_off, true);
        }
        context.startService(i);
    }
}
