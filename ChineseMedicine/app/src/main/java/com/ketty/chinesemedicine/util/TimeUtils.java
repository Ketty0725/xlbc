package com.ketty.chinesemedicine.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    public static final int SEC  = 1000;
    public static final int MIN  = 60000;
    public static final int HOUR = 3600000;

    public static String getRecentTimeSpanByNow(final long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;

        Calendar from = Calendar.getInstance();
        from.setTime(new Date(millis));
        Calendar to = Calendar.getInstance();
        to.setTime(new Date(now));

        int fromYear = from.get(Calendar.YEAR);
        int fromDay = from.get(Calendar.DAY_OF_MONTH);

        int toYear = to.get(Calendar.YEAR);
        int toDay = to.get(Calendar.DAY_OF_MONTH);
        int year = toYear - fromYear;
        int day = toDay - fromDay;

        if (year < 1) {
            if (day < 1) {
                if (span < 1000) {
                    return "刚刚";
                } else if (span < MIN) {
                    return String.format(Locale.getDefault(), "%d秒前", span / SEC);
                } else if (span < HOUR) {
                    return String.format(Locale.getDefault(), "%d分钟前", span / MIN);
                } else {
                    return String.format(Locale.getDefault(), "%d小时前", span / HOUR);
                }
            } else if (day < 2) {
                return "昨天";
            } else if (day < 6) {
                return day + "天前";
            } else {
                return new SimpleDateFormat("MM-dd").format(millis);
            }
        } else {
            return new SimpleDateFormat("yyyy-MM-dd").format(millis);
        }
    }


}
