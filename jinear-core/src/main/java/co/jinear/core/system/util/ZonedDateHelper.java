package co.jinear.core.system.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@UtilityClass
public class ZonedDateHelper {

    public static final String DATE_TIME_FORMAT_1 = "(HH:mm dd.MM.yyyy)";
    public static final String DATE_TIME_FORMAT_2 = "dd.MM.yyyy";
    public static final String DATE_TIME_FORMAT_3 = "dd.MM.yyyy HH:mm";

    public static String format(ZonedDateTime zonedDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }

    public static String formatWithDateTimeFormat1(ZonedDateTime zonedDateTime) {
        return format(zonedDateTime, DATE_TIME_FORMAT_1);
    }

    public static String formatWithDateTimeFormat2(ZonedDateTime zonedDateTime) {
        return format(zonedDateTime, DATE_TIME_FORMAT_2);
    }

    public static String formatWithDateTimeFormat3(ZonedDateTime zonedDateTime) {
        return format(zonedDateTime, DATE_TIME_FORMAT_3);
    }
}
