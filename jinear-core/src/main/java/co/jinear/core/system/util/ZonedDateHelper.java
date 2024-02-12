package co.jinear.core.system.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Slf4j
@UtilityClass
public class ZonedDateHelper {

    public static final String DATE_TIME_FORMAT_1 = "(HH:mm dd.MM.yyyy)";
    public static final String DATE_TIME_FORMAT_2 = "dd.MM.yyyy";
    public static final String DATE_TIME_FORMAT_3 = "dd.MM.yyyy HH:mm";
    public static final String DATE_TIME_FORMAT_4 = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT_5 = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static ZonedDateTime atTimeZone(ZonedDateTime zonedDateTime, String timeZone) {
        return Optional.ofNullable(timeZone)
                .map(ZoneId::of)
                .map(zonedDateTime::withZoneSameInstant)
                .orElseGet(() -> zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()));
    }

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

    public static String formatWithDateTimeFormat4(ZonedDateTime zonedDateTime) {
        return format(zonedDateTime, DATE_TIME_FORMAT_4);
    }

    public static String formatWithDateTimeFormat5(ZonedDateTime zonedDateTime) {
        return format(zonedDateTime, DATE_TIME_FORMAT_5);
    }

    public static ZonedDateTime parseIsoDateTime(String dateStr) {
        return ZonedDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static ZonedDateTime parseWithDateTimeFormat4(String dateStr, String zoneId) {
        Date date = DateHelper.parseDate(DATE_TIME_FORMAT_4, dateStr);
        return Optional.ofNullable(date)
                .map(Date::toInstant)
                .map(instant -> instant.atZone(ZoneId.of(zoneId)))
                .orElse(null);
    }

    public static void main(String[] args) {
        ZonedDateTime zonedDateTime = atTimeZone(ZonedDateTime.now(),"GMT");

        System.out.println(formatWithDateTimeFormat5(zonedDateTime) + " _____ " + zonedDateTime);
    }
}
