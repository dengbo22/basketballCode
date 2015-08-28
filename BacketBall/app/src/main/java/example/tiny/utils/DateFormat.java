package example.tiny.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tiny on 15-8-27.
 */
public class DateFormat {
    static SimpleDateFormat format = null;
    private static final int SECOND_PER_MIN = 60;
    private static final int SECOND_PER_HOUR = 60 * 60;
    private static final int SECOND_PER_DAY = 24 * 60 * 60;

    public DateFormat() {
        super();
    }

    public static Date UtcToDate(String utc) {
        if(format == null)
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = format.parse(utc);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String TimeElapse(Date beforeDate) {
        Date date = new Date();
        int second = (int) (date.getTime()/1000);
        int beforeSecond = (int) (beforeDate.getTime()/1000);
        int timeElapse = second - beforeSecond;
        if(timeElapse % SECOND_PER_DAY != 0)
            return timeElapse/SECOND_PER_DAY + "天前";
        else if(timeElapse % SECOND_PER_HOUR != 0)
            return timeElapse/SECOND_PER_HOUR + "小时前";
        else if(timeElapse % SECOND_PER_MIN != 0)
            return timeElapse / SECOND_PER_MIN +"分钟前";
        else
            return "异常" + timeElapse;
    }

}
