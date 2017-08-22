package com.mg.outad.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.mg.outad.http.HttpListener;
import com.mg.outad.http.HttpResponse;
import com.mg.outad.http.HttpUtils;
import com.mg.outad.message.ISender;
import com.mg.outad.message.MessageObjects;
import com.mg.outad.model.AdModel;
import com.mg.outad.model.AdReport;
import com.mg.outad.model.DeviceInfo;
import com.mg.outad.model.RequestModel;
import com.mg.outad.model.SDKConfigModel;
import com.mg.outad.ooa.AdError;
import com.mg.outad.ooa.MConstant;
import com.mg.outad.task.DeviceInfoTask;
import com.mg.outad.task.IDeviceInfoListener;
import com.mg.outad.task.LoactionHelper;
import com.mg.outad.utils.AdParser;
import com.mg.outad.utils.CommonUtils;
import com.mg.outad.utils.ConfigParser;
import com.mg.outad.utils.LocalKeyConstants;
import com.mg.outad.utils.LogUtils;
import com.mg.outad.utils.MiiBase64;
import com.mg.outad.utils.MiiLocalStrEncrypt;
import com.mg.outad.utils.SP;
import com.mg.outad.v4.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by nemo on 2016/6/28.
 */
public class HttpManager {
    public static final String NI = "NI";
    public static final String P = "P";
    public static final String RA="RA";
    public static final String HB ="hb";
    public static HttpManager instance;
    private DeviceInfo mDeviceInfo;
    private Context mContext;
    private JSONArray hosts;
    private int index;
    private ISender mSender;
    private int adType;
    private int ts;
    private static boolean isUpdate;

    public static HttpManager getInstance(Context mContext, ISender mSender) {
        if (instance == null){
            synchronized (HttpManager.class){
                if (instance == null){
                    synchronized (HttpManager.class){
                        instance = new HttpManager(mContext, mSender);
                    }
                }
            }
        }
        return instance;
    }

    public HttpManager(Context mContext,ISender mSender) {
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);
        }
        this.mContext = mContext;
        this.mSender = mSender;
    }

    public  boolean updateNi(int nextTime,String appid,String lid){

        long currenTime = System.currentTimeMillis();
        long hbTime = (long) SP.getParam(SP.CONFIG,mContext,SP.LAST_REQUEST_NI,0l);
        LogUtils.i(MConstant.TAG,"updateNi......diff_Time:"+hbTime+" config.getNext():"+nextTime);
        if (((currenTime-hbTime)/1000) >= nextTime){
            checkNiBefore(appid,lid);
            return true;
        }
        return false;
    }



    public String getRaUrl(){
        StringBuilder sb = new StringBuilder();
        if (MConstant.HB_HOST.equals("")){
            sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS));
        }
        else {
            sb.append(MConstant.HB_HOST);
        }
        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.SUFFIX_TRA,LocalKeyConstants.LOCAL_KEY_ACTIONS));
        return sb.toString();
    }


    public Map<String, String> getSraParams(int pt,String appid,String lid){
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext,MConstant.DEVICE_FILE_NAME);
            return null;
        }
        return RequestModel.getRequestParams2(mDeviceInfo,pt,appid,lid,mContext);
    }

    /**
     * 请求广告
     */
    public void requestRa(final int pt, String appid, String lid){

//        long currentTime = System.currentTimeMillis();
//        long lastRa = (long) SP.getParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA,0l);
//        long diffRa = (currentTime - lastRa)/1000;

//        if (lastRa != 0l && diffRa < 20){
//            return;
//        }
        LogUtils.i(MConstant.TAG,"start AD request...");
        if (!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }

        HttpUtils httpUtils = new HttpUtils(mContext);
        final  String url = getRaUrl();
        if (url == null || url.equals("")){
            return;
        }
        Map<String,String> params = getSraParams(pt,appid,lid);

        httpUtils.post(url.trim(), new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
//                SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_RA, System.currentTimeMillis());
                dealHbSuc(response,pt);
            }
            @Override
            public void onFail(Exception e) {
                LogUtils.i(MConstant.TAG,new AdError(AdError.ERROR_CODE_INVALID_REQUEST) + e.toString());
            }
        },params);

    }

    private void dealHbSuc(HttpResponse response,int pt){

        List<AdModel> ads;
        String temp = new String(response.entity());
        if (temp == null){
            return;
        }
        ads = AdParser.parseAd(temp);



        if (ads == null || ads.size() <= 0){
            return;
        }

        AdModel ad = ads.get(0);

//        SharedPreferences sp=mContext.getSharedPreferences(SP.CONFIG,Context.MODE_PRIVATE);
//        ad.setFlag(sp.getInt("ce",0));//默认触发广告

        if (mSender == null){
            return;
        }
        MessageObjects messageObjects = MessageObjects.obtain();
        messageObjects.obj0 = ad;
        messageObjects.arg0 = pt;
//        messageObjects.arg1 = ts;
        mSender.sendMessage(mSender.obtainMessage(MConstant.what.ads_result, messageObjects));
    }

    public void checkNiBefore(final String appid, final String lid){
        DeviceInfo mDeviceInfo= CommonUtils.readParcel(mContext, MConstant.DEVICE_FILE_NAME);
        if (mDeviceInfo == null){

            new DeviceInfoTask(new IDeviceInfoListener() {
                @Override
                public void deviceInfoLoaded(DeviceInfo deviceInfo) {
                    CommonUtils.writeParcel(mContext, MConstant.DEVICE_FILE_NAME, deviceInfo);
                    requestNi(appid,lid);
                }
            }, mContext).execute();
        }
        else {
            requestNi(appid,lid);
        }
    }

    /**
     * 配置信息初始化
     */
    public void requestNi(String appid,String lid){
        LogUtils.i(MConstant.TAG,"requestNi");

        if (CommonUtils.isNetworkAvailable(mContext)){

//            final long currenTime = System.currentTimeMillis();

//            long lastNi = (long) SP.getParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, 0l);
//
//            long diff_ni = (currenTime - lastNi)/1000;
//
//            LogUtils.i(MConstant.TAG,"lastNi:"+lastNi+" diff_ni:"+diff_ni);
//
//            if (lastNi != 0l && diff_ni < MConstant.DIFF_NI){
//                return;
//            }

            HttpUtils httpUtils = new HttpUtils(mContext);
            final String url = getHbUrl();
            if (url == null||url.equals("")){
                return;
            }
            Map<String,String> params = getHbParams(appid,lid);

            httpUtils.post(url.trim(), new HttpListener() {
                @Override
                public void onSuccess(HttpResponse response) {
                    SP.setParam(SP.CONFIG, mContext, SP.LAST_REQUEST_NI, System.currentTimeMillis());
                    MConstant.HB_HOST = MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST,LocalKeyConstants.LOCAL_KEY_DOMAINS);
                    dealNiSuc(response);
                }

                @Override
                public void onFail(Exception e) {
//                    requestVGD();
                    LogUtils.e(new AdError(AdError.ERROR_CODE_INVALID_REQUEST));
                }
            },params);

        }else{
            LogUtils.i(MConstant.TAG,"ni 网络不可用......");
        }
    }


    public Map<String,String> getHbParams(String appid,String lid){
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext,MConstant.DEVICE_FILE_NAME);
        }
        Map<String, String> params = new HashMap<>();
        String str;
        long currentTime = System.currentTimeMillis();
        params.put("action",NI);
        params.put("appid", appid);
        params.put("adrid",mDeviceInfo.getAndroidId());
        params.put("ver", MConstant.MSDK_VERSION);
        params.put("tp", String.valueOf(currentTime));
        params.put("dt","1");
        params.put("dtv",mDeviceInfo.getImei());
        str = NI + appid + MConstant.MSDK_VERSION + currentTime+"1"+mDeviceInfo.getImei()+mDeviceInfo.getScreenWidth()+mDeviceInfo.getScreenHeight();
        params.put("sign",CommonUtils.hashSign(str));
        params.put("w",String.valueOf(mDeviceInfo.getScreenWidth()));
        params.put("h",String.valueOf(mDeviceInfo.getScreenHeight()));
        params.put("lid",lid);
        return params;
    }
    private void dealNiSuc(HttpResponse response){
        SDKConfigModel sdk = null;
        String data = new String(response.entity());
        if (data==null){
            return;
        }

        sdk = ConfigParser.parseConfig(data);

        if (sdk == null){
            return;
        }
        sdk.setUpdateTime(System.currentTimeMillis());

        CommonUtils.writeParcel(mContext,MConstant.CONFIG_FILE_NAME,sdk);

        long writeTime = (long) SP.getParam(SP.CONFIG, mContext, SP.FOS, 0l);


        SharedPreferences.Editor editor=mContext.getSharedPreferences(SP.CONFIG,Context.MODE_PRIVATE).edit();
        editor.putLong("time0",sdk.getTime0()*1000);//应用外
        editor.putLong("time1",sdk.getTime1()*1000);//解锁
        editor.putLong("time2",sdk.getTime2()*1000);//安装
        editor.putLong("time3",sdk.getTime3()*1000);//卸载
        editor.putLong("time4",sdk.getTime4()*1000);//网络切换
        editor.putLong("timeComm",sdk.getTimeComm()*1000);//公共冷却时间
        editor.putInt("ce",sdk.getCe());
        editor.commit();

        long currentTime = System.currentTimeMillis();
        if (DateCompare(writeTime) || writeTime == 0l){
            SP.setParam(SP.CONFIG, mContext, SP.FOT, 0);
            SP.setParam(SP.CONFIG, mContext, SP.FOS, currentTime);
        }

        if (mSender != null && isUpdate){
            MessageObjects messageObjects = MessageObjects.obtain();
            messageObjects.obj0 = sdk;
            mSender.sendMessage(mSender.obtainMessage(MConstant.what.update_config, messageObjects));
        }

    }



    //是否过了今天
    private boolean DateCompare(long when){
        try {
            Date date=new Date(System.currentTimeMillis());
            SimpleDateFormat yFormat = new SimpleDateFormat("y"); //打印年份
            SimpleDateFormat mFormat = new SimpleDateFormat("M"); //打印月份
            SimpleDateFormat dFormat = new SimpleDateFormat("d"); //打印日份
            int yearNow=Integer.parseInt(yFormat.format(date));
            int monthNow=Integer.parseInt(mFormat.format(date));
            int dayNow=Integer.parseInt(dFormat.format(date));
            date.setTime(when);
            int year=Integer.parseInt(yFormat.format(date));
            int month=Integer.parseInt(mFormat.format(date));
            int day=Integer.parseInt(dFormat.format(date));
            if (yearNow > year||monthNow > month||dayNow > day){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getHbUrl(){
        StringBuilder sb = new StringBuilder();
        String str ="";
//        if (MConstant.isTest){
//          str ="OUNFNzUzMDM3Q0NGMkY4ODFFOTFCQzI2Q0ZBNEY4QTBBM0RFMDk4ODg0MEY2NkFFMERDNjA0MzAzODgwOTdGREE5RDlBREQxRkMzMjVERDIwODZFODMwMDkzNDQzMTU5";
//          sb.append(MiiLocalStrEncrypt.deCodeStringToString(str,LocalKeyConstants.LOCAL_KEY_URL));
//        } else {
//
//        }
        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST,LocalKeyConstants.LOCAL_KEY_DOMAINS));
        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.SUFFIX_HB,LocalKeyConstants.LOCAL_KEY_ACTIONS));
        return sb.toString();
    }

    public String getParams(@NonNull String Action, int type, int tiggleSence){
        if (mDeviceInfo == null){
            mDeviceInfo = CommonUtils.readParcel(mContext,MConstant.DEVICE_FILE_NAME);
            return null;
        }
        String url = RequestModel.getRequestParams(Action,mDeviceInfo, type, tiggleSence,mContext);
        return url;
    }

    public String getUA(){
        return mDeviceInfo == null ? null : mDeviceInfo.getUserAgent();
    }

    public synchronized void requestPing(final String host, final onPingListener pingListener){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }
        HttpUtils httpUtils = new HttpUtils(mContext);
        String url = host + MiiLocalStrEncrypt.deCodeStringToString(MConstant.SUFFIX,LocalKeyConstants.LOCAL_KEY_ACTIONS) +"/" + P;

        httpUtils.get(url, new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                if (!TextUtils.isEmpty(response.entity())){
                    if ("1".equals(response.entity())){
                        if (pingListener != null){
                            pingListener.pingSuc(host);
                        }
                    }
                }
                if (Integer.valueOf(response.entity()) == 1){
                }
            }
            @Override
            public void onFail(Exception e) {
                if (pingListener != null){
                    VGD(hosts,++index);
                }
            }
        });

    }

    /**
     * 请求新域名
     */
    public void requestVGD(){

        if (!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }
        HttpUtils httpUtils = new HttpUtils(mContext);
        httpUtils.get(MiiLocalStrEncrypt.deCodeStringToString(MConstant.VGD, LocalKeyConstants.LOCAL_VSGD), new HttpListener() {
            @Override
            public void onSuccess(HttpResponse response) {
                String result;
                result = MiiBase64.decode(response.entity());
                try {
                    JSONArray array = new JSONArray(result);
                    VGD(array,0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Exception e) {
                LogUtils.e(new AdError(AdError.ERROR_CODE_INVALID_REQUEST) + e.toString());
            }
        });

    }

    public void VGD(final JSONArray hosts, int i){
        this.index = i;
        this.hosts = hosts;
        if (i >= hosts.length()){
            return;
        }
        requestPing(hosts.optString(i), new onPingListener() {
            @Override
            public void pingSuc(String url) {

                MConstant.HB_HOST = url;

            }
        });

    }

    public interface onPingListener{
        void pingSuc(String url);
    }

    /**
     * 数据上报
     * @param adModel
     * @param eventType
     */
    public static void reportEvent(AdModel adModel, int eventType, Context mContext){

      try {
          if (adModel == null) {
              return;
          }
          if (!CommonUtils.isNetworkAvailable(mContext)) {
              LogUtils.e(new AdError(AdError.ERROR_CODE_NETWORK_ERROR));
              return;
          }
          HttpUtils httpUtils = new HttpUtils(mContext);
          String urls[] = null;

          switch (eventType) {
              case AdReport.EVENT_CLICK:

                  urls = adModel.getReportBean().getUrlClick();
                  break;

              case AdReport.EVENT_DOWNLOAD_COMPLETE:

                  urls = adModel.getReportBean().getUrlDownloadComplete();
                  break;

              case AdReport.EVENT_DOWNLOAD_START:

                  urls = adModel.getReportBean().getUrlDownloadStart();
                  break;

              case AdReport.EVENT_INSTALL_COMLETE:

                  urls = adModel.getReportBean().getUrlInstallComplete();
                  break;

              case AdReport.EVENT_OPEN:

                  urls = adModel.getReportBean().getUrlOpen();
                  break;

              case AdReport.EVENT_SHOW:

                  urls = adModel.getReportBean().getUrlShow();
                  break;
              case AdReport.EVENT_INSTALL_START:

                  urls = adModel.getReportBean().getUrlInstallStart();
                  break;
          }
          if (urls == null) {
              return;
          }

          for (String str : urls){
              if (!TextUtils.isEmpty(str)){

                  if (adModel.getType() == 4){

                      httpUtils.get(str,null);

                  } else {

                      httpUtils.get(replaceReportEventUrl(adModel,str,mContext),null);

                  }
              }
          }
      }
      catch (Exception e){
          e.printStackTrace();
      }
    }
    //替换点击上报
    private static String replaceReportEventUrl(AdModel adModel,String originUrl,Context context){

        LoactionHelper.LocModel locModel=LoactionHelper.GetUserLocation(context);
        if (originUrl.contains("%%LON%%")){
            originUrl = originUrl.replace("%%LON%%",String.valueOf(locModel.lon));
        }
        if (originUrl.contains("%%LAT%%")){
            originUrl = originUrl.replace("%%LAT%%",String.valueOf(locModel.lat));
        }
        if (originUrl.contains("%%DOWNX%%")){
            originUrl = originUrl.replace("%%DOWNX%%",adModel.getDownx());
        }
        if (originUrl.contains("%%DOWNY%%")){
            originUrl = originUrl.replace("%%DOWNY%%",adModel.getDowny());
        }
        if (originUrl.contains("%%UPX%%")){
            originUrl = originUrl.replace("%%UPX%%",adModel.getUpx());
        }
        if (originUrl.contains("%%UPY%%")){
            originUrl = originUrl.replace("%%UPY%%",adModel.getUpy());
        }
        if (originUrl.contains("%%CLICKID%%")){
            originUrl = originUrl.replace("%%CLICKID%%",adModel.getClickid());
        }
        return originUrl;
    }
}
