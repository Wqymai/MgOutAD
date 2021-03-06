package com.mxOrkle.lmUdcf.model;


import android.content.Context;
import android.content.res.Configuration;
import android.util.Base64;

import com.mxOrkle.lmUdcf.lwNm.MConstant;
import com.mxOrkle.lmUdcf.utils.CommonUtils;
import com.mxOrkle.lmUdcf.utils.LocalKeyConstants;
import com.mxOrkle.lmUdcf.utils.LogUtils;
import com.mxOrkle.lmUdcf.utils.MiiLocalStrEncrypt;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * 请求内容
 */
public class RequestModel {
    private String is;
    private String dt;
    private String dtv;
    private String ic;
    private String w;
    private String h;
    private String brand;
    private String mod;
    private String ov;
    private String sdkVersion;
    private String mcc;
    private String mnc;
    private String lac;
    private String cid;
    private int nt;
    private String mac;
    private String pl;
    private String adrid;
    private int adnum;
    private String ua;
    private String dip;
    private String sign;
    private int pt;
    private int st;
    private String action;
    private String appid;
    private String ver;
    private String tp;

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDtv() {
        return dtv;
    }

    public void setDtv(String dtv) {
        this.dtv = dtv;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public String getIs() {
        return is;
    }

    public void setIs(String is) {
        this.is = is;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getOv() {
        return ov;
    }

    public void setOv(String ov) {
        this.ov = ov;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public int getNt() {
        return nt;
    }

    public void setNt(int nt) {
        this.nt = nt;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getAdrid() {
        return adrid;
    }

    public void setAdrid(String adrid) {
        this.adrid = adrid;
    }

    public int getAdnum() {
        return adnum;
    }

    public void setAdnum(int adnum) {
        this.adnum = adnum;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getDip() {
        return dip;
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    private static RequestModel getRequestModel(String Action, DeviceInfo mDeviceInfo, int type, int tiggleSence, Context mContext){
        String key = MConstant.APPID;
        long currentTime = System.currentTimeMillis();
        if (mDeviceInfo == null){
            return null;
        }
        RequestModel requestModel = new RequestModel();
        requestModel.setAction(Action);
        requestModel.setAppid(key);
        requestModel.setVer(MConstant.MSDK_VERSION);
        requestModel.setTp(String.valueOf(currentTime));
        switch (Action) {
            case MConstant.request_type.ra:
                requestModel.setIs(mDeviceInfo.getImsi());
                requestModel.setDt("1");
                requestModel.setDtv(mDeviceInfo.getImei());
                requestModel.setIc(mDeviceInfo.getIccid());
                requestModel.setW(String.valueOf(mDeviceInfo.getScreenWidth()));
                requestModel.setH(String.valueOf(mDeviceInfo.getScreenHeight()));
                requestModel.setBrand(mDeviceInfo.getProductBrand());
                requestModel.setMod(mDeviceInfo.getProductModel());
                requestModel.setOv(mDeviceInfo.getOsVersionName());
                requestModel.setSdkVersion(String.valueOf(mDeviceInfo.getOsVersion()));
                requestModel.setMcc(String.valueOf(mDeviceInfo.getMcc()));
                requestModel.setMnc(String.valueOf(mDeviceInfo.getMnc()));
                requestModel.setLac(String.valueOf(mDeviceInfo.getLac()));
                requestModel.setCid(String.valueOf(mDeviceInfo.getCid()));
                requestModel.setNt(CommonUtils.getNetworkSubType(mContext));
                requestModel.setMac(mDeviceInfo.getMac());
                //设置请求广告类型
                requestModel.setPt(type);
                requestModel.setSt(tiggleSence);
                requestModel.setPl(CommonUtils.getInstalledSafeWare(mContext));
                requestModel.setAdrid(mDeviceInfo.getAndroidId());
                requestModel.setAdnum(1);
                requestModel.setUa(mDeviceInfo.getUserAgent());
                requestModel.setDip(String.valueOf(mDeviceInfo.getDip()));
                requestModel.setSign(CommonUtils.hashSign(Action+key+ MConstant.MSDK_VERSION
                        + currentTime+"1"+mDeviceInfo.getImei() + mDeviceInfo.getScreenWidth() + mDeviceInfo.getScreenHeight()));
                break;

            case MConstant.request_type.ni:
                requestModel.setDt("1");
                requestModel.setDtv(mDeviceInfo.getImei());
                requestModel.setSign(CommonUtils.hashSign(Action+key+ MConstant.MSDK_VERSION
                        + currentTime+"1"+mDeviceInfo.getImei()));
                break;

            case MConstant.request_type.p:

                break;

            case MConstant.request_type.vgd:

                break;
        }
        return requestModel;
    }

    public static String getRequestParams(String Action, DeviceInfo deviceInfo,int type,int tiggleSence, Context mContext){
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        RequestModel requestModel = getRequestModel(Action, deviceInfo, type, tiggleSence, mContext);
        params.put("action",requestModel.getAction());
        params.put("appid", requestModel.getAppid());
        params.put("ver", requestModel.getVer());
        params.put("tp", requestModel.getTp());
        boolean isImeiTest = false;
        switch(Action){
            case MConstant.request_type.hb:
                isImeiTest = false;
                params.put("is",requestModel.getIs());
                params.put("dt",requestModel.getDt());
                params.put("dtv",requestModel.getDtv());
                params.put("ic",requestModel.getIc());
                params.put("w",requestModel.getW());
                params.put("h",requestModel.getH());
                params.put("brand",requestModel.getBrand().trim().replace(" ",""));
                params.put("mod",requestModel.getMod().trim().replace(" ",""));
                params.put("os","android");
                params.put("ov",requestModel.getOv());
                params.put("sdkVersion",requestModel.getSdkVersion());
                params.put("mcc", requestModel.getMcc());
                params.put("mnc", requestModel.getMnc());
                params.put("lac",requestModel.getLac());
                params.put("cid",requestModel.getCid());
                params.put("nt",String.valueOf(requestModel.getNt()));
                params.put("mac", requestModel.getMac());
                params.put("pt", String.valueOf(requestModel.getPt()));
                params.put("st",String.valueOf(tiggleSence));
                params.put("pl",requestModel.getPl());
                params.put("adrid",requestModel.getAdrid());
                params.put("adnum",String.valueOf(requestModel.getAdnum()));
                try {
                    params.put("ua", URLEncoder.encode(requestModel.getUa(),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                params.put("dip",requestModel.getDip());
                params.put("sign",requestModel.getSign());
                break;

            case MConstant.request_type.ni:
                params.put("dt",requestModel.getDt());
                params.put("dtv",requestModel.getDtv());
                params.put("sign",requestModel.getSign());
                break;
        }
        String url = CommonUtils.MapToString(params);
        String url_base64 = Base64.encodeToString(CommonUtils.MapToString(params).getBytes(),Base64.NO_WRAP);
        StringBuffer sb = new StringBuffer();
        LogUtils.i(MConstant.TAG,"HOST:"+ MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST,
                LocalKeyConstants.LOCAL_KEY_DOMAINS)+" HB_HOST:"+MConstant.HB_HOST+" Action:"+Action);
        if (Action.equals(MConstant.request_type.ni)){
            sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS));
        }
        if (Action.equals(MConstant.request_type.hb)){
            if (MConstant.HB_HOST.equals("")){
                sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.HOST, LocalKeyConstants.LOCAL_KEY_DOMAINS));
            }
            else {
                sb.append(MConstant.HB_HOST);
            }
        }
        sb.append(MiiLocalStrEncrypt.deCodeStringToString(MConstant.SUFFIX,LocalKeyConstants.LOCAL_KEY_ACTIONS));
        sb.append("/");
        sb.append(Action);
        sb.append("?");
        sb.append(MConstant.GET_KEY);
        sb.append(isImeiTest ? url : url_base64);
        return sb.toString();
    }

    public static Map<String,String> getRequestParams2(DeviceInfo deviceInfo, int pt,String appid,String lid, Context mContext){
        if (deviceInfo == null){
            return null;
        }
        Map<String,String> params = new HashMap<>();
        try {
            long currTime = System.currentTimeMillis();
            params.put("action","tra");
            params.put("appid", appid);
            params.put("ver", MConstant.MSDK_VERSION);
            params.put("tp", String.valueOf(currTime));
            params.put("is",deviceInfo.getImsi());
            params.put("dt","1");
            params.put("dtv",deviceInfo.getImei());
            params.put("ic",deviceInfo.getIccid());
            params.put("w",String.valueOf(deviceInfo.getScreenWidth()));
            params.put("h",String.valueOf(deviceInfo.getScreenHeight()));
            params.put("brand",deviceInfo.getProductBrand());
            params.put("mod",deviceInfo.getProductModel());
            params.put("os","android");
            params.put("ov",deviceInfo.getOsVersionName());
            params.put("sdkVersion",String.valueOf(deviceInfo.getOsVersion()));
            params.put("mcc", String.valueOf(deviceInfo.getMcc()));
            params.put("mnc", String.valueOf(deviceInfo.getMnc()));
            params.put("lac",String.valueOf(deviceInfo.getLac()));
            params.put("cid",String.valueOf(deviceInfo.getCid()));
            params.put("nt", String.valueOf(CommonUtils.getNetworkSubType(mContext)));
            params.put("mac", deviceInfo.getMac());
            params.put("pt", String.valueOf(pt));
            params.put("pl",CommonUtils.getInstalledSafeWare(mContext));
            params.put("adrid",deviceInfo.getAndroidId());
            params.put("adnum", "1");
            try {
                params.put("ua", URLEncoder.encode(deviceInfo.getUserAgent(),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put("dip",String.valueOf(deviceInfo.getDip()));
            params.put("aaid",deviceInfo.getAdvertisingId());
            params.put("lon",deviceInfo.getLon());
            params.put("lat",deviceInfo.getLat());
            params.put("density",deviceInfo.getDensity());
            params.put("bssid",deviceInfo.getBssid());
            params.put("brk",String.valueOf(isRootSystem()));
            params.put("dl","1");
            params.put("sign",CommonUtils.hashSign("tra"+appid+ MConstant.MSDK_VERSION
                    + currTime+"1"+deviceInfo.getImei() + deviceInfo.getScreenWidth() + deviceInfo.getScreenHeight()));
            params.put("lid",lid);
            params.put("orientation",String.valueOf(getOri(mContext)));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return params;

    }

    public static int isRootSystem() {
        if(isRootSystem1()||isRootSystem2()){
            return 1;
        }else{
            return 0;
        }
    }
    private static boolean isRootSystem1() {
        File f = null;
        final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/" };
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()&&f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
    private static boolean isRootSystem2() {
        List<String> pros = getPath();
        File f = null;
        try {
            for (int i = 0; i < pros.size(); i++) {
                f = new File(pros.get(i),"su");
                if (f != null && f.exists()&&f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
    private static List<String> getPath() {
        return Arrays.asList(System.getenv("PATH").split(":"));
    }
    public static int getOri(Context context){
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation ; //获取屏幕方向
        int i = 0;
        if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            i = 0;

        } else if (ori == mConfiguration.ORIENTATION_LANDSCAPE){
            i = 1;
        }
        return i;
    }


}
