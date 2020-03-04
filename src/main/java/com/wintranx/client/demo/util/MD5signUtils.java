package com.wintranx.client.demo.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;


@Slf4j
public class MD5signUtils {

    /* 默认字符集*/
    private static final String DEF_CHAR_SET = "UTF-8";

    /* 默认签名域*/
    private static final String DEF_SIGN_FD = "tf_sign";

    /**
     * MD5签名加密
     *
     * @param orgMap  参数域集
     * @param signFd  签名域名称
     * @param addMap  增加域集 隐式增加
     * @param rmeList 删除域集 隐式删除
     * @return
     */
    public static Map<String, Object> createMD5Map(Map<String, Object> orgMap, String signFd,
                                                   Map<String, Object> addMap, List<String> rmeList) {
        return createMD5Map(orgMap, signFd, addMap, rmeList, DEF_CHAR_SET);
    }

    /**
     * MD5签名加密
     *
     * @param orgMap  参数域集
     * @param addMap  增加域集 隐式增加
     * @param rmeList 删除域集 隐式删除
     * @param charSet 字符集
     * @return
     */
    public static Map<String, Object> createMD5Map(Map<String, Object> orgMap,
                                                   Map<String, Object> addMap, List<String> rmeList, String charSet) {
        return createMD5Map(orgMap, DEF_SIGN_FD, addMap, rmeList, charSet);
    }

    /**
     * MD5签名加密
     *
     * @param orgMap  参数域集
     * @param addMap  增加域集 隐式增加
     * @param rmeList 删除域集 隐式删除
     * @return
     */
    public static Map<String, Object> createMD5Map(Map<String, Object> orgMap,
                                                   Map<String, Object> addMap, List<String> rmeList) {
        return createMD5Map(orgMap, DEF_SIGN_FD, addMap, rmeList, DEF_CHAR_SET);
    }

    /**
     * MD5签名加密
     *
     * @param orgMap  参数域集
     * @param signFd  签名域名称
     * @param addMap  增加域集 隐式增加
     * @param rmeList 删除域集 隐式删除
     * @param charSet 字符集
     * @return
     */
    @SuppressWarnings("unused")
    public static Map<String, Object> createMD5Map(Map<String, Object> orgMap, String signFd,
                                                   Map<String, Object> addMap, List<String> rmeList, String charSet) {

        Map<String, Object> result = new HashMap<String, Object>(orgMap);
        /* MD5加密数据为空*/
        if (result == null) {
            return result;
        }

        /* 忽略字段域集*/
        if (rmeList != null) {
            for (String rmeStr : rmeList) {
                result.remove(rmeStr);
            }
        }
        /* 忽略签名域*/
        result.remove(signFd);

        /* 增加字段域集*/
        if (addMap != null) {
            for (Entry<String, Object> entry : addMap.entrySet()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        /*** 加密方式：
         * 1.ansc升序value+value...value
         * 2.ansc升序name=value+name=value...name=value
         * 3.ansc降序Arrays.sort(key,Collections.reverseOrder());
         * ...
         * ***/
        /* ansc码升序排列*/
        Object[] keys = result.keySet().toArray();
        Arrays.sort(keys);
        StringBuffer sb = new StringBuffer();
        StringBuffer printSb = new StringBuffer();
        int length = keys.length;
        for (int i = 0; i < length; i++) {
            Object key= keys[i];
            Object value = result.get(key);
            String addString = "";
            if (value instanceof String) {
                try {
                    if (((String) value).startsWith("{")) {
                        String innerValue = JSON.toJSONString(JSONObject.parseObject((String) value), SerializerFeature.MapSortField);
                        addString = key + "=" + innerValue;
                    } else if (((String) value).startsWith("[")) {
                        String innerValue = JSON.toJSONString(JSONArray.parseArray((String)value), SerializerFeature.MapSortField);
                        addString = key + "=" + innerValue;
                    } else {
                        addString = key + "=" + value;
                    }
                } catch (Exception e) {
                    addString = key + "=" + value;
                }

            } else {
                String innerValue = JSON.toJSONString(result.get(key), SerializerFeature.MapSortField);
                addString = key + "=" + innerValue;
            }
            sb.append(addString);
            if ("card".equals(key.toString())) {
                printSb.append("card={******}");
            } else {
                printSb.append(addString);
            }
            if (i != keys.length - 1) {// 拼接时，不包括最后一个&字符
                printSb.append('&');
                sb.append('&');
            }
        }
        log.info("--------------------md5String :" + printSb.toString());
        /* MD5加密,并加入Map中*/
        orgMap.put(signFd, generateMd5(sb.toString(), charSet));

        /* TODO 后续可加入RSA加密*/

        return orgMap;
    }

    /**
     * MD5签名效验
     *
     * @param orgMap  参数域集
     * @param signFd  签名域名称
     * @param addMap  增加域集 隐式增加
     * @param rmeList 删除域集 隐式删除
     * @return
     */
    public static boolean verifyMD5Map(Map<String, Object> orgMap, String signFd,
                                       Map<String, Object> addMap, List<String> rmeList) {
        return verifyMD5Map(orgMap, signFd, addMap, rmeList, DEF_CHAR_SET);
    }

    /**
     * MD5签名效验
     *
     * @param orgMap  参数域集
     * @param addMap  增加域集 隐式增加
     * @param rmeList 删除域集 隐式删除
     * @param charSet 字符集
     * @return
     */
    public static boolean verifyMD5Map(Map<String, Object> orgMap,
                                       Map<String, Object> addMap, List<String> rmeList, String charSet) {
        return verifyMD5Map(orgMap, DEF_SIGN_FD, addMap, rmeList, charSet);
    }

    /**
     * MD5签名效验
     *
     * @param orgMap  参数域集
     * @param addMap  增加域集 隐式增加
     * @param rmeList 删除域集 隐式删除
     * @return
     */
    public static boolean verifyMD5Map(Map<String, Object> orgMap,
                                       Map<String, Object> addMap, List<String> rmeList) {
        return verifyMD5Map(orgMap, DEF_SIGN_FD, addMap, rmeList, DEF_CHAR_SET);
    }

    /**
     * MD5签名效验
     *
     * @param orgMap  参数域集
     * @param signFd  签名域名称
     * @param addMap  增加域集 隐式增加
     * @param rmeList 删除域集 隐式删除
     * @param charSet 字符集
     * @return
     */
    public static boolean verifyMD5Map(Map<String, Object> orgMap, String signFd,
                                       Map<String, Object> addMap, List<String> rmeList, String charSet) {

        /* TODO 后续可加入RSA解密*/
        String reqMd5 = (String) orgMap.get(signFd);
        Map<String, Object> result = createMD5Map(orgMap, signFd, addMap, rmeList, charSet);
        String repMd5 = (String) result.get(signFd);
        log.info("【MD5签名校验】 reqMd5={}, repMd5 = {}", reqMd5, repMd5);

        return repMd5.equals(reqMd5);
    }

    /**
     * 获取MD5加密字符串
     *
     * @param keyString
     * @param charset
     * @return
     */
    public static String generateMd5(String keyString, String charset) {
        String result = "";
        try {
            log.debug("【MD5原始数据】:" + keyString);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(keyString.getBytes(charset));
            byte[] temp = md5.digest("".getBytes(charset));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
            /* 字符串转换大写*/
            result = result.toUpperCase();
            log.debug("【MD5生成数据】:" + result);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5签名过程中出现错误" + e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }

        return result;
    }
    
    public static Map<String, Object> addSign(Map<String ,Object > orgMap, String md5Key) {
    	Map<String,Object> keyMap = new HashMap<String, Object>();
		keyMap.put("md5Key", md5Key);
		return createMD5Map(orgMap, keyMap, null);
    }
    
    public static Boolean verifySign(Map<String ,Object > orgMap,String md5Key) {
    	Map<String,Object> keyMap = new HashMap<String, Object>();
		keyMap.put("md5Key", md5Key);
		return verifyMD5Map(orgMap, keyMap, null);
    }
    
    
    public static Map<String, Object> addSign(Object object,String md5Key) {
    	Map<String, Object> orgMap = MapUtil.object2Map(object);
    	
    	Map<String,Object> keyMap = new HashMap<String, Object>();
		keyMap.put("md5Key", md5Key);
		return createMD5Map(orgMap, keyMap, null);
    }
    
    public static Boolean verifySign(Object object,String md5Key) {
    	Map<String, Object> orgMap = MapUtil.object2Map(object);
    	Map<String,Object> keyMap = new HashMap<String, Object>();
		keyMap.put("md5Key", md5Key);
		return verifyMD5Map(orgMap, keyMap, null);
    }
    
    
    
    //public static void main(String[] args) {
	//	Map<String,Object> map = new HashMap<String, Object>();
	//	map.put("a", "abc");
	//	map.put("b", "123");
	//	map.put("c", "abcd1");
	//	
	//	Map<String,Object> addMap = new HashMap<String, Object>();
	//	addMap.put("key", "abc");
	//	Map<String, Object> md5map = createMD5Map(map, addMap, null);
	//	boolean res = verifyMD5Map(md5map, addMap, null);
	//	
	//	System.out.println(res);
	//	
	//	
	//	Map<String,Object> orgMap = new HashMap<String, Object>();
	//	orgMap.put("a", "abc");
	//	orgMap.put("b", "123");
	//	orgMap.put("c", "abcd1");
	//	Map<String, Object> signMap = addSign(orgMap, "abc");
	//	System.out.println(signMap);
	//	Boolean resu = verifySign(signMap, "abc");
	//	System.out.println(resu);
	//	System.out.println(signMap);
	//}
}
