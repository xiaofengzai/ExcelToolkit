package com.data.core.excel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class DateUtil {
    private static final Logger logger = Logger.getLogger(DateUtil.class.getSimpleName());
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date parse(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            logger.severe("时间格式异常");
            throw new DataProcessException("时间格式异常");
        }
    }

}
