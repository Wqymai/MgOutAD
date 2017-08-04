package com.mgoutad.utils;


import com.mgoutad.model.SDKConfigModel;
import com.mgoutad.ooa.MConstant;

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
        JSONObject object_config;
        try {
            object = new JSONObject(result);
            if (object.getInt(RESULTCODE) == MConstant.SUC_CODE) {
                sdk = new SDKConfigModel();
                object_ad = object.optJSONObject(DATA);

                object_config = object_ad.optJSONObject("c");
                int next = object_ad.optInt(String.valueOf("n"));
                int adshow = object_ad.optInt(String.valueOf("o"));



                int showPercentage = 100;//object_config.optInt(String.valueOf(PERCENTAGE));
                int banner_time = 5;//object_config.optInt(String.valueOf(BANNER_TIME));
                int interstitial_time = 5;//object_config.optInt(String.valueOf(INTERSTITIAL_TIME));
                int splash_time = object_config.optInt(String.valueOf("kt"));
                int delay_time =3;// object_config.optInt(String.valueOf(INTERSTITIAL_DELAY_TIME));
                int jump = 1;//object_config.optInt(String.valueOf(JUMP));
                int jump_function = 1;//object_config.optInt(JUMP_FUNCTION);
                int show_sum = object_config.optInt(String.valueOf("at"));
                String bp = "1";//object_config.optString("bp");

                int timeComm = 20;//object_config.optInt("ptl");
//                JSONArray code_obj = new JSONArray(object_config.optString("al"));

                Long time0 = 20l;//Long.parseLong(code_obj.get(0).toString());
                Long time1 = 20l;//Long.parseLong(code_obj.get(1).toString());
                Long time2 = 20l;//Long.parseLong(code_obj.get(1).toString());
                Long time3 = 20l;//Long.parseLong(code_obj.get(1).toString());
                Long time4 = 20l;//Long.parseLong(code_obj.get(1).toString());

                int ce = 0;//object_config.optInt("ce");

//                AdPercentage adPercentage = new AdPercentage();
//                JSONObject object_wp = object_config.optJSONObject(WP);
//
//
//                adPercentage.setBanner_p(object_wp.optInt(String.valueOf(AdPercentage.BANNER)));
//                adPercentage.setInterstitial_p(object_wp.optInt(String.valueOf(AdPercentage
//                        .INTERSTITIAL)));
//                adPercentage.setSplash_p(object_wp.optInt(String.valueOf(AdPercentage.SPLASH)));


//                AdSence adSence = new AdSence();
//                JSONObject object_op = object_config.optJSONObject(OP);
//                adSence.setSence_screenOn(object_op.optInt(String.valueOf(AdSence.SCREEN_ON)));
//                adSence.setSence_install(object_op.optInt(String.valueOf(AdSence.INSTALL)));
//                adSence.setSence_uninstall(object_op.optInt(String.valueOf(AdSence.UNINSTALL)));
//                adSence.setSence_connectChange(object_op.optInt(String.valueOf(AdSence
//                        .CONNECTCHANGE)));
//                adSence.setSence_battery(object_op.optInt(String.valueOf(AdSence.BATTERY)));


                sdk.setAdShow(adshow == SDKConfigModel.AD_OPEN);
                sdk.setShow_percentage(showPercentage);
                sdk.setJump(jump == SDKConfigModel.JUMP_SHOW);
                sdk.setJump_function(jump_function);
                sdk.setInterstitial_delay_time(delay_time);
                sdk.setBanner_time(banner_time);
                sdk.setSplash_time(splash_time);
                sdk.setInterstitial_time(interstitial_time);
                sdk.setNext(next);
                sdk.setAuto_show_percentage(0);
                sdk.setList("com.wqy.youle");
                sdk.setListType(1);
                sdk.setShow_sum(show_sum);
                //设置个场景冷却时间和点击动作
                sdk.setCe(ce);
                sdk.setTime0(time0);
                sdk.setTime1(time1);
                sdk.setTime2(time2);
                sdk.setTime3(time3);
                sdk.setTime4(time4);
                sdk.setTimeComm(timeComm);
                sdk.setBp(bp);
                sdk.setSence(null);
                sdk.setPercentage(null);


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
