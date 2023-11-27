package com.timnhatro1.asus.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class StringUtils {
    private static final String NUMBER_FORMAT_2 = "#,###,###";
    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }
    public static String formatPriceNumber(String price){
        try {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
            otherSymbols.setGroupingSeparator('.');
            return new DecimalFormat(NUMBER_FORMAT_2).format(Double.valueOf(price)).replace(",",".");
        } catch (Exception e){
            return price + "";
        }

    }

}
