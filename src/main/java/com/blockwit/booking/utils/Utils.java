package com.blockwit.booking.utils;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getUploadPath(String uploadPath) {

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        Date dateNow = new Date();
        String dateForNow = (new SimpleDateFormat("yyyy.MM")).format(dateNow);

        String uploadPathPerMonth=uploadPath + "/" + dateForNow;
        File uploadDirPerMonth=new File(uploadPathPerMonth);
        if(!uploadDirPerMonth.exists()) {
            uploadDirPerMonth.mkdir();
        }

        return uploadPathPerMonth;
    }

}
