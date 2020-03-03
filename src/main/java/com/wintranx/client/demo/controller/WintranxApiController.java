package com.wintranx.client.demo.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wintranx.client.demo.util.KeyUtils;
import com.wintranx.client.demo.util.MD5signUtils;
import com.wintranx.client.demo.util.SecurityUtil;
import com.wintranx.client.demo.util.WintranxContrant;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wintranx.client.demo.util.HttpClientUtil;
import com.wintranx.client.demo.util.InputReader;

@Controller
@PropertySource("classpath:application.properties")
public class WintranxApiController {

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
    @Value("${api.transaction.querytran}")
    private String QUERY_TRAN;
    @Value("${api.pubKey}")
    private String PUB_KEY;
//    private String REQ_POST_URL_KEY= "req."+InputReader.POST_URL;
//    private String REQ_MD5_KEY= "req."+InputReader.MD5_KEY;
    private void initMerchant(HttpServletRequest request, ModelMap map) {

        RandomUtils.nextInt();
        String ccy = getRandomCcy();
        map.put("currency", ccy);
        int dig = WintranxContrant.CCY.get(ccy);
        BigDecimal amount1 = new BigDecimal(getAmount(dig));
        BigDecimal amount2 = new BigDecimal(getAmount(dig));
        BigDecimal num1 =  new BigDecimal(getNum()+"");
        BigDecimal num2 =   new BigDecimal(getNum()+"");
        BigDecimal total = amount1.multiply(num1);
        total = total.add(amount2.multiply(num2));
        String basket =  "[{\"amountPerItem\":\""+amount1+"\",\"quantity\":\""+num1+"\",\"productName\":\"goodname1\",\"productSku\":\"sku100001\"}," +
                "{\"amountPerItem\":\""+amount2+"\",\"quantity\":\""+num2+"\",\"productName\":\"goodname2\",\"productSku\":\"sku100002\"}]";
        map.put("amount", total);
        map.put("basket",basket);
    }
    private String getPostUrlFromRequest(Map<String, String[]>  parmater) {
        return API_URL;
    }
    @RequestMapping("/sale/init")
    public String sale(HttpServletRequest request, ModelMap map) {
        initMerchant(request, map);
        map.put("merTradeId", KeyUtils.genUniqueKey());
        map.put("merOrderId",  KeyUtils.genUniqueKey());
        return "sale";
}
    @RequestMapping("/sale/do")
    @ResponseBody
    public Object goPay(HttpServletRequest request, ModelMap map, HttpServletResponse response)
    {
        Map<String, String[]>  parmater = request.getParameterMap();
        String  param = InputReader.readToJson(ACCOUNT_ID, MD5, parmater);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest(parmater) + SALE, param);
        if (!MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            map.put("verifyResult", "(验签失败)");
        } else {
            map.put("verifyResult", "(验签成功)");
        }
        handResult( result, parmater , map, false);
        map.put("reqJson", param);
        map.put("resJson", result);
        //return "sale_result";
        
        return resultObject(request, response, param, result);
    }
    @RequestMapping("/authorize/init")
    public String authorize(HttpServletRequest request, ModelMap map) {
        initMerchant(request, map);
        map.put("merTradeId", KeyUtils.genUniqueKey());
        map.put("merOrderId",  KeyUtils.genUniqueKey());
        return "authorize";
    }

    @RequestMapping("/authorize/do")
    @ResponseBody
    public Object goAuthorize(HttpServletRequest request, ModelMap map, HttpServletResponse response)
    {
        Map<String, String[]>  parmater = request.getParameterMap();
        String  param = InputReader.readToJson(ACCOUNT_ID, MD5, parmater);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest(parmater) + AUTHORIZE, param);
        if (!MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            map.put("verifyResult", "(验签失败)");
        } else {
            map.put("verifyResult", "(验签成功)");
        }
        handResult( result, parmater , map, false);
        map.put("reqJson", param);
        map.put("resJson", result);
        //return "authorize_result";
        
        return resultObject(request, response, param, result);
    }
    @RequestMapping("/capture/init")
    public String capture(HttpServletRequest request, ModelMap map) {
        initMerchant(request, map);
        Map<String, String[]>  parmater = request.getParameterMap();
        commonSecondInit(parmater,map);
        return "capture";
    }

    @RequestMapping("/capture/do")
    @ResponseBody
    public Object goCapture(HttpServletRequest request, ModelMap map, HttpServletResponse response)
    {
        Map<String, String[]>  parmater = request.getParameterMap();
        String  param = InputReader.readToJson(ACCOUNT_ID, MD5, parmater);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest(parmater) + CAPTURE, param);
        if (!MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            map.put("verifyResult", "(验签失败)");
        } else {
            map.put("verifyResult", "(验签成功)");
        }
        handResult(result, parmater , map, true);
        map.put("reqJson", param);
        map.put("resJson", result);
        //return "capture_result";
        
        return resultObject(request, response, param, result);
    }

    @RequestMapping("/refund/init")
    public String refund(HttpServletRequest request, ModelMap map) {
        initMerchant(request, map);
        Map<String, String[]>  parmater = request.getParameterMap();
        commonSecondInit(parmater,map);
        return "refund";
    }
    @RequestMapping("/refund/do")
    @ResponseBody
    public Object goRefund(HttpServletRequest request, ModelMap map, HttpServletResponse response)
    {
        Map<String, String[]>  parmater = request.getParameterMap();
        String  param = InputReader.readToJson(ACCOUNT_ID, MD5, parmater);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest(parmater) + REFUND, param);
        if (!MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            map.put("verifyResult", "(验签失败)");
        } else {
            map.put("verifyResult", "(验签成功)");
        }
        handResult( result,  parmater, map, true);
        map.put("reqJson", param);
        map.put("resJson", result);
        //return "refund_result";
        
        return resultObject(request, response, param, result);
    }
    @RequestMapping("/void/init")
    public String Void(HttpServletRequest request, ModelMap map) {
        initMerchant(request, map);
        Map<String, String[]>  parmater = request.getParameterMap();
        commonSecondInit(parmater,map);
        return "void";
    }
    @RequestMapping("/void/do")
    @ResponseBody
    public Object goVoid(HttpServletRequest request, ModelMap map, HttpServletResponse response)
    {
        Map<String, String[]>  parmater = request.getParameterMap();
        String  param = InputReader.readToJson(ACCOUNT_ID, MD5, parmater);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest(parmater) + VOID, param);
        if (!MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            map.put("verifyResult", "(验签失败)");
        } else {
            map.put("verifyResult", "");
        }
        handResult( result,  parmater, map, true);
        map.put("reqJson", param);
        map.put("resJson", result);
        //return "void_result";
        
        return resultObject(request, response, param, result);
    }
    @RequestMapping("/pageSale/init")
    public String paypage(HttpServletRequest request, ModelMap map) {
        initMerchant(request, map);
        map.put("merTradeId", KeyUtils.genUniqueKey());
        map.put("merOrderId",  KeyUtils.genUniqueKey());
        return "pageSale";
    }
    @RequestMapping("/pageSale/do")
    @ResponseBody
    public Object goPaypage(HttpServletRequest request, ModelMap map, HttpServletResponse response)
    {
        Map<String, String[]>  parmater = request.getParameterMap();
        String  param = InputReader.readToJson(ACCOUNT_ID, MD5, parmater);
        map.put("actionPath", getPostUrlFromRequest(parmater) + PAYPAGE);
        map.put("requestJson", param);
        //return "pageSale_result";
        
        return resultObject(request, response, param, null);
    }
    @RequestMapping("/pageSale/receivePage")
    public String goPaypageNotice(HttpServletRequest request, ModelMap map) throws Exception {
        Map<String, String[]>  parmater = request.getParameterMap();
        Map<String, String> result = new HashMap<>();
        for (String key : parmater.keySet()) {
            result.put(key, parmater.get(key)[0]);
        }
        if (!SecurityUtil.rsaHexVerifyMap(result, PUB_KEY)) {
            map.put("verifyResult", "(验签失败)");
        } else {
            map.put("verifyResult", "(验签成功)");
        }
        String  param = JSON.toJSONString(result);
        map.put("resultNotice", param);
        return "pageSale_notice";
    }
    @RequestMapping("/sessionSale/init")
    public String sessionSale(HttpServletRequest request, ModelMap map) {
        initMerchant(request, map);
        map.put("merTradeId", KeyUtils.genUniqueKey());
        map.put("merOrderId",  KeyUtils.genUniqueKey());
        map.put("apiUrlJs", API_URL_JS);
        map.put("apiUrlDomain", API_URL_DOMAIN);
        return "sessionSale";
    }
    @RequestMapping("/sessionSale/do")
    @ResponseBody
    public Object goSessionSale(HttpServletRequest request, ModelMap map, HttpServletResponse response)
    {
        Map<String, String[]>  parmater = request.getParameterMap();
        String  param = InputReader.readToJson(ACCOUNT_ID, MD5, parmater);
        Map<String, String> post = new HashMap<>();
        post.put("requestJson", param);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest(parmater) + SESSION_PAY,  param);
        if (!MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            map.put("verifyResult", "(验签失败)");
        } else {
            map.put("verifyResult", "");
        }
        handResult( result,  parmater, map, true);
        map.put("reqJson", param);
        map.put("resJson", result);
        map.put("apiUrlJs", API_URL_JS);
        map.put("apiUrlDomain", API_URL_DOMAIN);

        //return "sessionSale_result";
        
        return resultObject(request, response, param, result);
    }

    @RequestMapping("/sessionSale/receivePage")
    public String goSessionSaleNotice(HttpServletRequest request, ModelMap map) throws Exception {
        Map<String, String[]>  parmater = request.getParameterMap();
        Map<String, String> result = new HashMap<>();
        for (String key : parmater.keySet()) {
            result.put(key, parmater.get(key)[0]);
        }
        if (!SecurityUtil.rsaHexVerifyMap(result, PUB_KEY)) {
            map.put("verifyResult", "(验签失败)");
        } else {
            map.put("verifyResult", "(验签成功)");
        }
        String  param = JSON.toJSONString(result);
        map.put("resultNotice", param);
        return "sessionSale_notice";
    }
    
    @RequestMapping("/queryTran/init")
    public String queryTran(HttpServletRequest request, ModelMap map) {
        initMerchant(request, map);
        map.put("tradeId", KeyUtils.genUniqueKey());
        return "queryTran";
    }
    @RequestMapping("/queryTran/do")
    @ResponseBody
    public Object goQueryTran(HttpServletRequest request, ModelMap map, HttpServletResponse response)
    {
        Map<String, String[]>  parmater = request.getParameterMap();
        String  param = InputReader.readToJson(ACCOUNT_ID, MD5, parmater);
        Map<String, String> post = new HashMap<>();
        post.put("requestJson", param);
        String result = HttpClientUtil.doPostJson(getPostUrlFromRequest(parmater) + QUERY_TRAN,  param);
        if (!MD5signUtils.verifySign(this.jsonToMap(result), MD5)) {
            map.put("verifyResult", "(验签失败)");
        } else {
            map.put("verifyResult", "");
        }
        handResult( result,  parmater, map, true);
        map.put("reqJson", param);
        map.put("resJson", result);
        
        return resultObject(request, response, param, result);
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
    private void handResult(String result, Map<String, String[]>  parmater, ModelMap map, boolean modify) {
//        String tradeId = "";
//        Map<String, Object> returnMap = new HashMap<>();
//        if (result != null && result.length() != 0) {
//            Map<String, Object> resMap = this.jsonToMap(result);
//            if (resMap.get("resultCode") != null && "1000".equals(resMap.get("resultCode").toString())) {
//                tradeId= resMap.get("tradeId").toString();
//            }
//            if (resMap.get("resultCode") != null) {
//                returnMap.put("resultCode", resMap.get("resultCode").toString());
////                returnMap.put("accountId", parmater.get("req.accountId")[0]);
//                returnMap.put(InputReader.MD5_KEY, MD5 );
//                returnMap.put(InputReader.POST_URL, API_URL);
//                returnMap.put("tradeId", tradeId);
//                returnMap.put("merTradeId", KeyUtils.genUniqueKey());
//                Map<String, Object> modificationAmount = new HashMap<>();
//                if (resMap.get("aToken") == null) {
//                    if (modify) {
//                        modificationAmount.put("value", getRequestString(parmater ,"req.modificationAmount.value"));
//                        modificationAmount.put("currency", getRequestString(parmater ,"req.modificationAmount.currency"));
//                    } else {
//                        modificationAmount.put("value", parmater.get("req.amount.value")[0]);
//                        modificationAmount.put("currency", parmater.get("req.amount.currency")[0]);
//                    }
//                    returnMap.put("modificationAmount", modificationAmount);
//                } else {
//                    returnMap.put("oId", resMap.get("oId").toString());
//                    returnMap.put("aToken", resMap.get("aToken").toString());
//                }
//            }
//        }
//        map.put("res", returnMap);
    }
    private void commonSecondInit(Map<String, String[]>  parmater , ModelMap map) {
        Map<String, Object> returnMap = new HashMap<>();
//        returnMap.put("accountId", getRequestString(parmater ,"req.accountId") );
//        returnMap.put(InputReader.MD5_KEY, MD5 );
//        returnMap.put("originalMerchantReference", getRequestString(parmater ,"req.originalMerchantReference"));
        returnMap.put("orgTradeId", getRequestString(parmater ,"req.tradeId"));
        returnMap.put("merTradeId", getRequestString(parmater ,"req.merTradeId"));
        Map<String, Object> modificationAmount = new HashMap<>();
        modificationAmount.put("value", getRequestString(parmater ,"req.modificationAmount.value"));
        modificationAmount.put("currency", getRequestString(parmater ,"req.modificationAmount.currency"));
        returnMap.put("modificationAmount", modificationAmount);
        map.put("res", returnMap);
    }
    private static Map<String, Object> getMap(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.putAll(jsonObject);
        return valueMap;
    }

    private String getRequestString(Map<String, String[]> parmater ,String key) {
        if (parmater.get(key) == null ||
                parmater.get(key).length == 0) {
            return "";
        } else {
            return parmater.get(key)[0];
        }
    }
    private static String getRandomCcy() {
        int max= WintranxContrant.CCYlIST.size() -1;
        int min=0;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return  WintranxContrant.CCYlIST.get(s);
    }
    private static int getNum() {
        int max= 11;
        int min=1;
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
    }
    private static String getAmount(int dig) {
        int max= 100;
        int min=1;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        if (dig ==0) {
            return s+"";
        }
        return  s +".00";
    }
    
    private static Object resultObject(HttpServletRequest request, 
    		HttpServletResponse response, String bodyReq, String bodyRep) {
    	String resultReq = "";
    	Enumeration<String> headNamesReq = request.getHeaderNames();
    	while(headNamesReq.hasMoreElements()){
    		String headerName = headNamesReq.nextElement();
    		resultReq += headerName+":"+request.getHeader(headerName)+"\r\n";
    	}
		
		String resultRep = null;
    	Collection<String> headNamesRep = response.getHeaderNames();
    	Iterator<String> it = headNamesRep.iterator();
    	while(it.hasNext()){
    		resultRep += it.next()+"\r\n";
    	}
    	
    	 Map<String, String>  resultDate = new HashMap<>();
         resultDate.put("getHeader", resultReq+"body:"+bodyReq);
         resultDate.put("header", resultRep);
         resultDate.put("body", bodyRep);
         return resultDate;
    }
}
