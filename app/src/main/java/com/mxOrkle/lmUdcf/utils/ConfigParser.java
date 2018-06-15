package com.mxOrkle.lmUdcf.utils;


import com.mxOrkle.lmUdcf.model.AdPercentage;
import com.mxOrkle.lmUdcf.model.SDKConfigModel;
import com.mxOrkle.lmUdcf.lwNm.MConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ConfigParser {
//    public static String jsontest = "{\n" +
//            "resultCode: 1,\n" +
//            "msg: \"\",\n" +
//            "data: {\n" +
//            "o: \"2\",\n" +
//            "l: \"com.tencent.mm|com.tencent.mobileqq|com.dotamax.app\",\n" +
//            "n: \"100\",\n" +
//            "c: {\n" +
//            "o: \"1\",\n" +
//            "p: \"100\",\n" +
//            "kt: \"6\",\n" +
//            "ct: \"6\",\n" +
//            "bt: \"1000\",\n" +
//            "cl: \"3\",\n" +
//            "sk: \"1\",\n" +
//            "bu: \"0\",\n" +
//            "at: \"800\",\n" +
//            "al: \"30\",\n" +
//            "wp: {\n" +
//            "1: \"0\",\n" +
//            "2: \"0\",\n" +
//            "3: \"100\"\n" +
//            "},\n" +
//            "op: {\n" +
//            "1: 2,\n" +
//            "2: 3,\n" +
//            "3: 3,\n" +
//            "4: 3\n" +
//            "}\n" +
//            "}\n" +
//            "}\n" +
//            "}";

    public static final String RESULTCODE = "resultCode";
    public static final String MSG = "msg";
    public static final String DATA = "data";

    private static final String CONFIG = "c";
    private static final String LIST_TYPE = "o";
    private static final String LIST = "l";
    private static final String NEXT = "n";
    private static final String AUTOPERCENTAGE = "cr";

    private static final String ADSHOW = "o";
    private static final String PERCENTAGE = "p";
    private static final String SPLASH_TIME = "kt";
    private static final String INTERSTITIAL_TIME = "ct";
    private static final String BANNER_TIME = "bt";
    private static final String INTERSTITIAL_DELAY_TIME = "cl";
    private static final String JUMP = "sk";
    private static final String JUMP_FUNCTION = "bu";
    private static final String SHOW_SUM = "at";
    private static final String INTERVAL = "al";
    private static final String WP = "wp";
    private static final String OP = "op";

    public static SDKConfigModel parseConfig(String result) {

        LogUtils.i(MConstant.TAG, "parse Config = " + result);
        SDKConfigModel sdk = null;
        JSONObject object = null;
        JSONObject object_ad = null;
        try {
            object = new JSONObject(result);
            if (object.getInt(RESULTCODE) == MConstant.SUC_CODE) {
                sdk = new SDKConfigModel();
                object_ad = object.optJSONObject(DATA);

                int next = object_ad.optInt(String.valueOf("n"));
                int adshow = object_ad.optInt(String.valueOf("o"));
                int showPercentage = object_ad.optInt("p") ;
                int banner_time = object_ad.optInt("bt") ;
                int interstitial_time = object_ad.optInt("ct");
                int splash_time = object_ad.optInt("kt");
                int delay_time =object_ad.optInt("cl");
                int jump = object_ad.optInt("sk");
                int jump_function = object_ad.optInt("bu");
                int show_sum = object_ad.optInt("at");
                String bp = object_ad.optString("bp");
                String list  = object_ad.optString("nl");
                int listType = object_ad.optInt("nf");
                int timeComm = object_ad.optInt("ptl");
                int auto = object_ad.optInt("cr");
                JSONObject wp = new JSONObject(object_ad.optString("wp"));

                AdPercentage adPercentage = new AdPercentage();
                adPercentage.setBanner_p(Integer.parseInt(wp.optString("1")));
                adPercentage.setSplash_p(Integer.parseInt(wp.optString("2")));
                adPercentage.setInterstitial_p(Integer.parseInt(wp.optString("3")));
                sdk.setAdShow(adshow == SDKConfigModel.AD_OPEN);
                sdk.setShow_percentage(showPercentage);
                sdk.setJump(jump == SDKConfigModel.JUMP_SHOW);
                sdk.setJump_function(jump_function);
                sdk.setInterstitial_delay_time(delay_time);
                sdk.setBanner_time(banner_time);
                sdk.setSplash_time(splash_time);
                sdk.setInterstitial_time(interstitial_time);
                sdk.setNext(next);
                sdk.setAuto_show_percentage(auto);
                sdk.setList(list);
                sdk.setListType(listType);
                sdk.setShow_sum(show_sum);
                sdk.setTime0(timeComm);
                sdk.setTime1(timeComm);
                sdk.setTime2(timeComm);
                sdk.setTime3(timeComm);
                sdk.setTime4(timeComm);
                sdk.setTimeComm(timeComm);
                sdk.setBp(bp);
                sdk.setPercentage(adPercentage);

            } else {
                sdk = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            sdk = null;
        }
        return sdk;
    }

    public static String[] parserToArray(JSONArray array) {
        String[] strings = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            try {
                strings[i] = array.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return strings;
    }
}
