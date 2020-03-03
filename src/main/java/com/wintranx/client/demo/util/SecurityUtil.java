package com.wintranx.client.demo.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Slf4j
@Configuration
@EnableCaching
public class SecurityUtil {
    private static String SIGN = "tf_sign";
    private static String SIGN_TYPE = "sign_type";
    public static final String CHARSET = "UTF-8";
    public static final String STAR_CHAR = "******";

    /**
     * 加密卡号
     * 前6后 4
     *
     * @param card
     * @return
     */
    public String warpPayCard(String card) {
        return warpStr(card, 6, 4);
    }

    /**
     * cvv加密
     * ****
     *
     * @param card
     * @return
     */
    public String warpPayCvv(String cvv) {
        String cvvEn = "***";
        if (cvv.length() == 4) {
            cvvEn = "****";
        }
        return cvvEn;
    }

    /**
     * 加密手机
     * 前3后4
     *
     * @param phone
     * @return
     */
    public String warpPhone(String phone) {
        return warpStr(phone, 3, 4);
    }

    /**
     * 身份证
     * 前1 后1
     *
     * @param idCard
     * @return
     */
    public String warpIDCard(String idCard) {
        return warpStr(idCard, 1, 1);
    }

    /**
     * 加密邮箱
     * 前3后@....
     *
     * @param email
     * @return
     */
    public String warpEmail(String email) {
        return warpEmailStr(email, 3);
    }

    /**
     * 加密名
     * 前3后@....
     *
     * @param info
     * @return
     */
    public String warpName(String name) {
        return warpStr(name, 1, 0);
    }

    public String warpStr(String info, int preIndex, int endIndex) {
        return info.substring(0, preIndex) + STAR_CHAR + info.substring(info.length() - endIndex);
    }

    public String warpEmailStr(String info, int preIndex) {
        return info.substring(0, preIndex) + STAR_CHAR + info.substring(info.indexOf('@'), info.length());
    }

    public static Boolean rsaHexVerifyMap(Map params, String publicKey) throws Exception {
        String sign = (String) params.get(SIGN);
        if (StringUtil.isEmpty(sign)) {
            return false;
        }
        String data = createLink(params, null);
        return RSAEncryptCoder.verifyFromHexAscii(sign, publicKey, data, CHARSET);
    }


    /**
     * 除去数组中的空值和签名参数
     *
     * @param oriMap 签名参数
     * @return 去掉空值与签名参数后的新签名参数组
     */
    private static Map paraFilter(Map oriMap, String[] rmeList) {
        Map result = new HashMap();
        if (oriMap == null || oriMap.size() <= 0) {
            return result;
        }
        for (Object key : oriMap.keySet()) {
            Object value = oriMap.get(key);
            if (value == null || value.equals("") || value.equals("null")
                    || key.equals(SIGN_TYPE) || key.equals(SIGN)
                    || (rmeList != null && Arrays.asList(rmeList).contains(key))
            ) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }


    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String createLink(Map params, String[] rmeList) {
        if (params == null || params.size() <= 0) {
            return null;
        }
        params = paraFilter(params, rmeList);
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuffer prestr = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr.append(key + "=" + value);
            } else {
                prestr.append(key + "=" + value + "&");
            }
        }
        log.info("createLink,{}", prestr.toString());
        return prestr.toString();
    }
}
