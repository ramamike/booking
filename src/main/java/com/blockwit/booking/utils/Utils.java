package com.blockwit.booking.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {


    public static String getPathPerMonth(String path) {

        Date dateNow = new Date();
        String dateForNow = (new SimpleDateFormat("yyyy.MM")).format(dateNow);
        String uploadPathPerMonth=path + "/" + dateForNow;

        return path + "/" + dateForNow;
    }

}
