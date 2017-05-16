package csci571.zhiqinliao.hw9.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Util class supply basic digital and string opertaion
 */

public class Util {
    public static String formateDate(String date) {
        try {
            DateFormat oriFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return String.format("%tb", oriFormat.parse(date)) + " " + String.format("%td", oriFormat.parse(date)) + "," + String.format("%tY", oriFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String firstCharUpper(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
