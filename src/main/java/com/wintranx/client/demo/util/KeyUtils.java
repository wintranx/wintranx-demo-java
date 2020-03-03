package com.wintranx.client.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class KeyUtils {

    /**
     * 生成唯一的主键
     * 格式：[时间+随机数]
     *
     * @return
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();

        /*六位随机数*/
        Integer number = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);
    }

    /**
     * 生成唯一的主键
     * 格式：十四位[时间+随机数]
     *
     * @return
     */
    public static synchronized String genSealKey() {
        Random random = new Random();
        SimpleDateFormat sft = new SimpleDateFormat("yyyyMMdd");

        /*六位随机数*/
        Integer number = random.nextInt(900000) + 100000;

        return sft.format(new Date()) + String.valueOf(number);
    }

    /**
     * 生成唯一的主键
     * 格式：[时间+随机数]
     *
     * @return
     */
    public static synchronized String genUniqueKey7() {
        Random random = new Random();

        /*六位随机数*/
        Integer number = random.nextInt(900000) + 100000;

        return String.valueOf(number);
    }
}
