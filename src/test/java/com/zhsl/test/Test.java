package com.zhsl.test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class Test {

    @org.junit.Test
    public void test() {

        BigDecimal product = new BigDecimal(505915);
        BigDecimal market = new BigDecimal(425120);

        System.out.println(product.subtract(market).divide(product, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        System.out.println(calendar.get(Calendar.MINUTE));

    }
}
