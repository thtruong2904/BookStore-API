package com.tranhuutruong.BookStoreAPI.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Format {
    public static String normalPhone(String phone){
        return phone.replaceAll("[^a-zA-Z0-9]+","");
    }

    public static String formatMoney(Long money)
    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("#,###.##");

        String moneyFormat = decimalFormat.format(money);
        return moneyFormat;
    }
}
