package com.wintranx.client.demo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WintranxContrant {
    public static Map<String, Integer> CCY = new HashMap<>();
    public static List<String> CCYlIST = new ArrayList<>();
    static {
        CCYlIST.add("AUD");
        CCYlIST.add("CAD");
        CCYlIST.add("CHF");
        CCYlIST.add("CNY");
        CCYlIST.add("EUR");
        CCYlIST.add("GBP");
        CCYlIST.add("HKD");
        CCYlIST.add("IDR");
        CCYlIST.add("JPY");
        CCYlIST.add("KRW");
        CCYlIST.add("MYR");
        CCYlIST.add("NZD");
        CCYlIST.add("SGD");
        CCYlIST.add("THB");
        CCYlIST.add("USD");

        CCY.put("AUD",2);
        CCY.put("CAD",2);
        CCY.put("CHF",2);
        CCY.put("CNY",2);
        CCY.put("EUR",2);
        CCY.put("GBP",2);
        CCY.put("HKD",2);
        CCY.put("IDR",0);
        CCY.put("JPY",0);
        CCY.put("KRW",0);
        CCY.put("MYR",2);
        CCY.put("NZD",2);
        CCY.put("SGD",2);
        CCY.put("THB",2);
        CCY.put("USD",2);

    }
}
