package com.mg.outad.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mg.outad.ooa.MConstant;
import com.mg.outad.ooa.MiiService;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,MiiService.MService.class);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
            i.putExtra(MConstant.sence.install,true);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)){
            i.putExtra(MConstant.sence.uninstall,true);
        }
        context.startService(i);
    }
}
