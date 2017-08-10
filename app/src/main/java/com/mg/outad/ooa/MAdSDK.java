package com.mg.outad.ooa;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

import com.mg.outad.layer.LayerManager;
import com.mg.outad.listener.BootReceiver;
import com.mg.outad.listener.ScreenReceiver;
import com.mg.outad.listener.UserPresentReceiver;
import com.mg.outad.manager.RuleManager;
import com.mg.outad.model.DeviceInfo;
import com.mg.outad.task.IDeviceInfoListener;
import com.mg.outad.utils.CommonUtils;
import com.mg.outad.utils.LogUtils;
import com.mg.outad.v4.ActivityCompat;
import com.mg.outad.v4.ContextCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION_CODES.M;
import static com.mg.outad.ooa.MConstant.sence.screen_on;
import static com.mg.outad.utils.SystemUtils.checkPasswordToUnLock;


public class MAdSDK implements IDeviceInfoListener {

    private Context mContext;
    public static MAdSDK sInstance = null;

    public static MAdSDK getInstance() {
        if (sInstance == null){
            synchronized (MAdSDK.class){
                if (sInstance == null){
                    sInstance = new MAdSDK();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context){
        this.mContext = context.getApplicationContext();
        initReceivers(mContext);
        add1ProtectWindow(mContext);
    }

    private  void  add1ProtectWindow(Context context){
        LogUtils.i(MConstant.TAG,"添加1像素悬浮窗保护");
        Button button = new Button(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST; // 这里是关键，你也可以试试2003
        wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        wmParams.width = 1;
        wmParams.height = 1;
        wmParams.gravity= Gravity.LEFT|Gravity.TOP;
        wm.addView(button, wmParams); // 创建View

    }


    public Context getContext(){
        return mContext;
    }

    @Override
    public void deviceInfoLoaded(DeviceInfo deviceInfo) {
        LogUtils.i(MConstant.TAG,"init load device info over");
        CommonUtils.writeParcel(mContext, MConstant.DEVICE_FILE_NAME, deviceInfo);
    }

    private void initReceivers(Context pContext) {


        UserPresentReceiver userPresentReceiver=new UserPresentReceiver();
        IntentFilter if_userpresent = new IntentFilter();
        if_userpresent.addAction(Intent.ACTION_USER_PRESENT);
        pContext.registerReceiver(userPresentReceiver,if_userpresent);

        BootReceiver bootReceiver=new BootReceiver();
        IntentFilter if_boot=new IntentFilter();
        if_boot.addAction(Intent.ACTION_PACKAGE_ADDED);
        if_boot.addAction(Intent.ACTION_PACKAGE_REMOVED);
        if_boot.addDataScheme("package");
        pContext.registerReceiver(bootReceiver,if_boot);

        ScreenReceiver screenReceiver=new ScreenReceiver();
        IntentFilter screen=new IntentFilter();
        screen.addAction(Intent.ACTION_SCREEN_OFF);
        screen.addAction(Intent.ACTION_SCREEN_ON);
        pContext.registerReceiver(screenReceiver,screen);

    }
    public void startMiiService(Activity activity){
         if (Build.VERSION.SDK_INT >= M){
            checkPermission(activity);
         }
         else {
             activity.startService(new Intent(activity, MiiService.class));
         }
    }

    /**
     android 6.0以上检查权限WRITE_EXTERNAL_STORAGE
     */
    private void checkPermission(final Activity activity){
        try {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(activity.getApplicationContext(), ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(activity.getApplicationContext(), WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    ) {

                ActivityCompat.requestPermissions(activity, new String[]{READ_PHONE_STATE,ACCESS_COARSE_LOCATION,WRITE_EXTERNAL_STORAGE}, 123);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(activity.getApplicationContext(), ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(activity.getApplicationContext(), WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED
                                ) {


                        }
                        else {
                            activity.startService(new Intent(activity, MiiService.class));
                        }
                    }
                },5000);

            }
            else {

                activity.startService(new Intent(activity, MiiService.class));

            }
        }catch (Exception e){


            e.printStackTrace();

        }
    }

    public void startAd(Intent intent, Application application){
        LogUtils.i(MConstant.TAG,"startAd");
        String appid = "82c91b5ae71c55110c370f8c6671fc79";
        String IID = "tDF9HggD";
        String KID = "ZaexwvBE";
        String BID = "uyasA5Lo";
        String lid;
        boolean isScene = false;
        int show_percentage = (int) ((Math.random() * 100)+1);
        int pt = 0;
        if (show_percentage <= 40){
            pt = 2;
            lid = KID;
        }
        else if (40 < show_percentage && show_percentage <= 70 ){
            pt = 1;
            lid = BID;
        }
        else {
            pt = 3;
            lid = IID;
        }

        RuleManager rule = RuleManager.getInstance(application, LayerManager.obtainLocal(application));
        boolean needListen = true;
        if (intent != null) {
            if (intent.getBooleanExtra(MConstant.sence.user_present, false)) {
                LogUtils.i(MConstant.TAG,"收到解锁广播");
                MConstant.isBlack=false;
                pt = 2;
                lid = KID;
                isScene = true;
                needListen = true;
            }
            if (intent.getBooleanExtra(MConstant.sence.screen_off, false)) {
                LogUtils.i(MConstant.TAG,"收到screen_off广播");
                MConstant.isBlack=true;
                needListen=false;
            }
            if (intent.getBooleanExtra(screen_on, false)) {
                LogUtils.i(MConstant.TAG,"收到screen_on广播");
                if (!checkPasswordToUnLock(application)){
                    MConstant.isBlack=false;
                }
            }
            if (intent.getBooleanExtra(MConstant.sence.install, false)){
                LogUtils.i(MConstant.TAG,"收到安装广播");
                MConstant.isBlack=false;
                pt = 3;
                lid = IID;
                isScene = true;
                needListen = true;
            }
            if (intent.getBooleanExtra(MConstant.sence.uninstall, false)){
                LogUtils.i(MConstant.TAG,"收到卸载广播");
                MConstant.isBlack=false;
                pt = 3;
                lid = IID;
                isScene = true;
                needListen = true;
            }
            if (needListen) {
                rule.listen(pt,appid,lid,isScene);
            }

        }
    }

}
