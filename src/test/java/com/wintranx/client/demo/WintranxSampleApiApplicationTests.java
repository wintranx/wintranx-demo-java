package com.wintranx.client.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wintranx.client.demo.util.HttpClientUtil;
import com.wintranx.client.demo.util.InputReader;
import com.wintranx.client.demo.util.KeyUtils;
import com.wintranx.client.demo.util.MD5signUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 这里是API 交易最精简的接口
 */
@SpringBootTest
class WintranxSampleApiApplicationTests {
    @Value("${api.url}")
    private String API_URL;
    @Value("${api.url.domain}")
    private String API_URL_DOMAIN;
    @Value("${api.url.js}")
    private String API_URL_JS;
    @Value("${api.md5}")
    private String MD5;
    @Value("${api.accountId}")
    private String ACCOUNT_ID;
    @Value("${api.transaction.authorize}")
    private String AUTHORIZE;
    @Value("${api.transaction.capture}")
    private String CAPTURE;
    @Value("${api.transaction.sale}")
    private String SALE;
    @Value("${api.transaction.refund}")
    private String REFUND;
    @Value("${api.transaction.void}")
    private String VOID;
    @Value("${api.transaction.pagepay}")
    private String PAYPAGE;
    @Value("${api.transaction.sessionpay}")
    private String SESSION_PAY;
    @Value("${api.pubKey}")
    private String PUB_KEY;
    private String REQ_POST_URL_KEY= "req."+ InputReader.POST_URL;
    private String REQ_MD5_KEY= "req."+InputReader.MD5_KEY;
    String PRE_IDX= "wintranx.";
    @Test
    void sale() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PRE_IDX + "accountId", ACCOUNT_ID);
        paramMap.put(PRE_IDX + InputReader.MD5_KEY, MD5);
        paramMap.put(PRE_IDX + "merOrderId", KeyUtils.genUniqueKey());
        paramMap.put(PRE_IDX + "merTradeId", KeyUtils.genUniqueKey());
        paramMap.put(PRE_IDX + "amount.value", "100.00");
        paramMap.put(PRE_IDX + "amount.currency", "USD");
        paramMap.put(PRE_IDX + "card.number", "4111111111111111");
        paramMap.put(PRE_IDX + "card.expiryMonth", "02");
        paramMap.put(PRE_IDX + "card.expiryYear", "2021");
        paramMap.put(PRE_IDX + "card.cvc", "123");
        paramMap.put(PRE_IDX + "shopperIP", "192.168.10.17");
        paramMap.put(PRE_IDX + "notifyUrl", "http://192.168.10.17:8551/notice/success");
        paramMap.put(PRE_IDX + "shopperIP", "192.168.10.17");
        paramMap.put(PRE_IDX + "version", "1.0");
        String  param = InputReader.readMapToJson(paramMap);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest() + SALE, param);
        if (MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            System.out.println("the response's md5 check passed");
        } else {
            System.out.println("the response's md5 check failed");
        }
        System.out.println(result);
    }
    @Test
    void auth() {
        Map<String, Object> resMap = auth("100.00", "USD",
                "4111111111111111", "02",
                "2021", "123");
        if (resMap.get("resultCode") != null && "1000".equals(resMap.get("resultCode").toString())) {
            // 交易成功
            String trdId = resMap.get("tradeId").toString();
        }
    }
    @Test
    void authWithCapture() {
        String amount = "100.00";
        String ccy = "USD";
        Map<String, Object> resMap = auth("100.00", "USD",
                "4111111111111111", "02",
                "2021", "123");
        if (resMap.get("resultCode") != null && "1000".equals(resMap.get("resultCode").toString())) {
            String trdId = resMap.get("tradeId").toString();
            capture(trdId, amount, ccy);
        }
    }
    @Test
    void capture() {
        String amount = "100.00";
        String ccy = "USD";
        String trdId = ""; //auth教育返回的tradeId
        capture(trdId, amount, ccy);
    }
    @Test
    void refund() {
        String amount = "100.00";
        String ccy = "USD";
        String trdId = ""; // sale 交易或者capture返回的 tradeId
        refund(trdId, amount, ccy);
    }
    @Test
    void _void() {
        String amount = "100.00";
        String ccy = "USD";
        String trdId = ""; //auth,capture,sale,refund交易返回的tradeId, 其中 capture,sale,refund 的void只能当天执行
        capture(trdId, amount, ccy);
    }
    private  Map<String, Object> auth(String amount,
                                      String ccy ,
                                      String cardNumber,
                                      String expiryMonth,
                                      String expiryYear,
                                      String cvc) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PRE_IDX + "accountId", ACCOUNT_ID);
        paramMap.put(PRE_IDX + InputReader.MD5_KEY, MD5);
        paramMap.put(PRE_IDX + "merOrderId", KeyUtils.genUniqueKey());
        paramMap.put(PRE_IDX + "merTradeId", KeyUtils.genUniqueKey());
        paramMap.put(PRE_IDX + "amount.value", amount);
        paramMap.put(PRE_IDX + "amount.currency", ccy);
        paramMap.put(PRE_IDX + "card.number", cardNumber);
        paramMap.put(PRE_IDX + "card.expiryMonth", expiryMonth);
        paramMap.put(PRE_IDX + "card.expiryYear", expiryYear);
        paramMap.put(PRE_IDX + "card.cvc", cvc);
        paramMap.put(PRE_IDX + "shopperIP", "192.168.10.17");//購物著ip
        paramMap.put(PRE_IDX + "notifyUrl", "http://192.168.10.17:8551/notice/success");
        String  param = InputReader.readMapToJson(paramMap);
        System.out.println("postparam" + param);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest() + AUTHORIZE, param);
        if (MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            System.out.println("the response's md5 check passed");
        } else {
            System.out.println("the response's md5 check failed");
        }
        Map<String, Object> resMap = this.jsonToMap(result);
        return resMap;
    }
    private Map<String, Object>  capture(String orgId, String amount, String ccy) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PRE_IDX + "accountId", ACCOUNT_ID);
        paramMap.put(PRE_IDX + InputReader.MD5_KEY, MD5);
        paramMap.put(PRE_IDX + "merTradeId", KeyUtils.genUniqueKey());
        paramMap.put(PRE_IDX + "orgTradeId", orgId);
        paramMap.put(PRE_IDX + "modificationAmount.value", amount);
        paramMap.put(PRE_IDX + "modificationAmount.currency", ccy);
        String  param = InputReader.readMapToJson(paramMap);
        System.out.println("postparam:" + param);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest() + CAPTURE, param);
        if (MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            System.out.println("the response's md5 check passed");
        } else {
            System.out.println("the response's md5 check failed");
        }
        Map<String, Object> resMap = this.jsonToMap(result);
        return resMap;
    }
    private Map<String, Object>  refund(String orgId, String amount, String ccy) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PRE_IDX + "accountId", ACCOUNT_ID);
        paramMap.put(PRE_IDX + InputReader.MD5_KEY, MD5);
        paramMap.put(PRE_IDX + "merTradeId", KeyUtils.genUniqueKey());
        paramMap.put(PRE_IDX + "orgTradeId", orgId);
        paramMap.put(PRE_IDX + "modificationAmount.value", amount);
        paramMap.put(PRE_IDX + "modificationAmount.currency", ccy);
        String  param = InputReader.readMapToJson(paramMap);
        System.out.println("postparam:" + param);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest() + REFUND, param);
        if (MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            System.out.println("the response's md5 check passed");
        } else {
            System.out.println("the response's md5 check failed");
        }
        Map<String, Object> resMap = this.jsonToMap(result);
        return resMap;
    }
    private Map<String, Object> voids(String orgId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PRE_IDX + "accountId", ACCOUNT_ID);
        paramMap.put(PRE_IDX + InputReader.MD5_KEY, MD5);
        paramMap.put(PRE_IDX + "merTradeId", KeyUtils.genUniqueKey());
        paramMap.put(PRE_IDX + "orgTradeId", orgId);
        String  param = InputReader.readMapToJson(paramMap);
        System.out.println("postparam:" + param);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest() + VOID, param);
        if (MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            System.out.println("the response's md5 check passed");
        } else {
            System.out.println("the response's md5 check failed");
        }
        Map<String, Object> resMap = this.jsonToMap(result);
        return resMap;
    }
    private String getPostUrlFromRequest() {
        return API_URL;
    }
    public static Map<String, Object> jsonToMap(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        Map<String, Object> extMap = JSON.parseObject(jsonStr,
                new TypeReference<Map<String, Object>>() {
                });
        return extMap;
    }
}
