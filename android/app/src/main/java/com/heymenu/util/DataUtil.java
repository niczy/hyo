package com.heymenu.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by nicholaszhao on 4/20/14.
 */
public class DataUtil {
    public static String calMoney(double a) {
        final DecimalFormat formater = new DecimalFormat("$###,###.00");
        return formater.format(a);
    }
}
