package com.wintranx.client.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class InputReader {

    public static String MD5_KEY= "config_md5Key";
    public static String POST_URL= "config_postUrl";
    private static void removeConfigKey(Map<String, Object> returnMap) {
        String md5 = (String) returnMap.get(MD5_KEY);
        returnMap.remove(MD5_KEY);
        String postUrl = (String) returnMap.get(POST_URL);
        returnMap.remove(POST_URL);

    }
    public static String readToJson(String accountId, String md5Key, Map<String,String[]> parmater) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        for(Map.Entry<String, String[]> entry : parmater.entrySet()){
            String mapKey = entry.getKey();
            String[] mapkeys = mapKey.split("\\.");
            String[] mapValue = entry.getValue();
            int lenth = mapkeys.length;
            if (mapValue[0] != null && mapValue[0].length() >0) {
                if (lenth == 2) {
                    returnMap.put(mapkeys[lenth -1], mapValue[0]);
                } else {
                    if (returnMap.get(mapkeys[1]) == null) {
                        returnMap.put(mapkeys[1],  new HashMap<String, HashMap>());
                    }
                    Map<String, Object>  value = (Map<String, Object>) returnMap.get(mapkeys[1]);
                    value.put(mapkeys[2], mapValue[0]);
                }
            }
        }
        Object basket = returnMap.get("basket");
        if (basket != null && basket.toString().length() != 0) {
            JSONArray jsonArray = JSONArray.parseArray(basket.toString());
            returnMap.put("basket", jsonArray);
        }
        returnMap.put("accountId", accountId);
        String md5 = md5Key;
        removeConfigKey(returnMap);
        Map signMap =   JSONObject.parseObject(JSON.toJSONString(returnMap), Map.class);
        System.out.println(JSON.toJSONString(signMap));
        signMap =  MD5signUtils.addSign(signMap, md5);
        System.out.println(JSON.toJSONString(returnMap));
        return JSON.toJSONString(signMap);
    }
    public static String readMapToJson(Map<String,String> parmater) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        for(Map.Entry<String, String> entry : parmater.entrySet()){
            String mapKey = entry.getKey();
            String[] mapkeys = mapKey.split("\\.");
            String mapValue = entry.getValue();
            int lenth = mapkeys.length;
            if (lenth == 2) {
                returnMap.put(mapkeys[lenth -1], mapValue);
            } else {
                if (returnMap.get(mapkeys[1]) == null) {
                    returnMap.put(mapkeys[1],  new HashMap<String, HashMap>());
                }
                Map<String, Object>  value = (Map<String, Object>) returnMap.get(mapkeys[1]);
                value.put(mapkeys[2], mapValue);
            }
        }
        Object basket = returnMap.get("basket");
        if (basket != null && basket.toString().length() != 0) {
            JSONArray jsonArray = JSONArray.parseArray(basket.toString());
            returnMap.put("basket", jsonArray);
        }
        String md5 = (String) returnMap.get(MD5_KEY);
        removeConfigKey(returnMap);
        Map signMap =   JSONObject.parseObject(JSON.toJSONString(returnMap), Map.class);
        signMap =  MD5signUtils.addSign(signMap, md5);
        return JSON.toJSONString(signMap);
    }
}
