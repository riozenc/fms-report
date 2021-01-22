/**
 * Author : czy
 * Date : 2019年4月24日 下午8:10:13
 * Title : com.riozenc.cfs.common.MonUtils.java
 **/
package org.fms.report.common.util;

import com.riozenc.titanTool.common.date.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonUtils {
    public static String getMon() {
        return DateUtil.getDate("yyyyMM");
    }

    public static String getMon(String date) {
        if (date == null) {
            return DateUtil.getDate("yyyyMM");
        }
        return date;
    }

    public static String getDay() {
        return DateUtil.getDate("yyyy-MM-dd");
    }

    public static String getLastMon(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(4)) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -1);
        return DateUtil.getDate(calendar.getTime(), "yyyyMM");
    }

    public static String getNextMon(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(4)) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        return DateUtil.getDate(calendar.getTime(), "yyyyMM");
    }

    public static List<Integer> getMons(int startMon, int endMon, List<Integer> mons) {
        int nextMon = Integer.parseInt(MonUtils.getNextMon(String.valueOf(startMon)));
        mons.add(nextMon);
        if (nextMon != endMon) {
            return getMons(nextMon, endMon, mons);
        }
        return mons;
    }

}
