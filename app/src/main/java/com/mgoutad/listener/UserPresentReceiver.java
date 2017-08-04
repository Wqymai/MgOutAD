package com.mgoutad.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mgoutad.ooa.MConstant;
import com.mgoutad.ooa.MiiService;


/**
 * Created by wuqiyan on 16/11/3.
 */
public class UserPresentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, MiiService.MService.class);
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            i.putExtra(MConstant.sence.user_present, true);
        }
        context.startService(i);
    }
}
