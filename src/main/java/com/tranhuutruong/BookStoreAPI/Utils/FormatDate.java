package com.tranhuutruong.BookStoreAPI.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDate {
    public static Date formatDate(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = simpleDateFormat.parse(s);
        return date;
    }

    public static java.sql.Date formatDateMySql(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = simpleDateFormat.parse(s);
        return new java.sql.Date(date.getTime());
    }
}
