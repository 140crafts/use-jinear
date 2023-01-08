package co.jinear.core.system.util;


import co.jinear.core.model.enumtype.DayType;
import com.hubspot.horizon.shaded.com.google.common.base.Enums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
public class DateHelper {

    private static final Logger logger = LoggerFactory.getLogger(DateHelper.class);
    public static final String MYSQL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String FILE_DATE_PATTERN = "yyyy_MM_dd_HH_mm_ss";
    public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DAY_OF_MONTH_PATTERN = "dd";
    static final int MONDAY = 2;
    static final int SUNDAY = 1;

    private DateHelper() {
    }

    public static String getFileDate(Date date) {
        return formatDate(FILE_DATE_PATTERN, date);
    }

    public static Long getDateStringToMillis(String date) {
        Long time = System.currentTimeMillis();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            time = sdf.parse(date).getTime();
        } catch (Exception e) {
            log.error("Error on date str to millis.", e);
        }
        return time;
    }

    public static String getMillisToDateString(Long millis) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Baghdad"));
        return df.format(millis);
    }

    public static String getTodayDate(String gmtOffset) {
        if (gmtOffset == null) {
            gmtOffset = "+0300";
        }
        TimeZone tz = TimeZone.getTimeZone("GMT" + gmtOffset);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // yyyy-MM-dd'T'HH:mm'Z'
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static Date now() {
        return new Date();
    }

    public static String formatDate(String pattern, Date date) {
        return Objects.isNull(date) ? null : (new SimpleDateFormat(pattern)).format(date);
    }

    public static String toMySQLDateFormat(Date date) {
        return formatDate(MYSQL_DATE_PATTERN, date);
    }

    public static Date parseAsMySQLDate(String dateStr) {
        return StringUtils.isBlank(dateStr) ? null : parseDate("yyyy-MM-dd HH:mm:ss", dateStr);
    }

    public static Date parseDate(String pattern, String dateStr) {
        try {
            return (new SimpleDateFormat(pattern)).parse(dateStr);
        } catch (ParseException var3) {
            logger.error("Caught exception while parsing date", var3);
            return null;
        }
    }

    public static Date setHourMinuteSecond(Date date, int hour, int minute, int second) {
        if (!Objects.isNull(date)) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.set(11, hour);
            cal.set(12, minute);
            cal.set(13, second);
            cal.set(14, 0);
            return cal.getTime();
        } else {
            return null;
        }
    }

    public static Date addDays(Date target, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(target);
        cal.add(5, days);
        return cal.getTime();
    }

    public static Date substractDays(Date target, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(target);
        cal.add(5, -days);
        return cal.getTime();
    }

    public static Date addHours(Date target, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(target);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }

    public static Date addMinutes(Date target, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(target);
        cal.add(12, minutes);
        return cal.getTime();
    }

    public static Date addSeconds(Date target, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(target);
        cal.add(13, seconds);
        return cal.getTime();
    }

    public static Date substractMinutes(Date target, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(target);
        cal.add(12, -minutes);
        return cal.getTime();
    }

    public static Date substractSeconds(Date target, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(target);
        cal.add(13, -seconds);
        return cal.getTime();
    }

    public static int getHour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(11);
    }

    public static int getMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(12);
    }

    public static int getSecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(13);
    }

    public static Date getDateWithCurrentTime(Date date) {
        return setHourMinuteSecond(date, getHour(now()), getMinute(now()), getSecond(now()));
    }

    public static int dayDiff(Date start, Date end) {
        return (int) ChronoUnit.DAYS.between(convertDateToLocalDateTime(start), convertDateToLocalDateTime(end));
    }

    public static long millisDiff(Date start, Date end) {
        return ChronoUnit.MILLIS.between(convertDateToLocalDateTime(start), convertDateToLocalDateTime(end));
    }

    public static boolean hasSameDayNumber(Date first, Date second) {
        return !Objects.isNull(first) && !Objects.isNull(second) && Period.between(convertDateToLocalDate(first), convertDateToLocalDate(second)).getDays() == NumberUtils.INTEGER_ZERO;
    }

    public static boolean isSameDay(Date first, Date second) {
        return !Objects.isNull(first) && !Objects.isNull(second) && DateUtils.isSameDay(first, second);
    }

    private static LocalDateTime convertDateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    private static LocalDate convertDateToLocalDate(Date date) {
        return convertDateToLocalDateTime(date).toLocalDate();
    }

    public static Date getPreviousWeekDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(7);
        if (dayOfWeek == 1) {
            return substractDays(date, 2);
        } else {
            return dayOfWeek == 2 ? substractDays(date, 3) : substractDays(date, 1);
        }
    }

    public static Integer getPastDayCountOfCurrentMonth() {
        return Integer.valueOf(formatDate("dd", now())) - 1;
    }

    public static Date firstDayOfNextMonth(Date date) {
        Date startDate = DateUtils.setDays(DateUtils.addMonths(date, 1), 1);
        return setHourMinuteSecond(startDate, 0, 0, 0);
    }

    public static Date firstDayOfNextMonth() {
        return firstDayOfNextMonth(now());
    }

    public static Date firstDayOfPreviousMonth(Date date) {
        Date startDate = DateUtils.setDays(DateUtils.addMonths(date, -1), 1);
        return setHourMinuteSecond(startDate, 0, 0, 0);
    }

    public static Date firstDayOfPreviousMonth() {
        return firstDayOfPreviousMonth(now());
    }

    public static Date lastDayOfPreviousMonth(Date date) {
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(DateUtils.setDays(DateUtils.addMonths(date, -1), 1));
        endCal.set(5, endCal.getActualMaximum(5));
        Date endDate = endCal.getTime();
        return setHourMinuteSecond(endDate, 23, 59, 59);
    }

    public static Date lastDayOfPreviousMonth() {
        return lastDayOfPreviousMonth(now());
    }

    public static Date firstDayOfThisMonth(Date date) {
        Date startDate = DateUtils.setDays(date, 1);
        return setHourMinuteSecond(startDate, 0, 0, 0);
    }

    public static Date firstDayOfThisMonth() {
        return firstDayOfThisMonth(now());
    }

    public static Date lastDayOfThisMonth(Date date) {
        Date endDate = firstDayOfNextMonth(date);
        endDate = DateUtils.addDays(endDate, -1);
        return setHourMinuteSecond(endDate, 23, 59, 59);
    }

    public static Date lastDayOfThisMonth() {
        return lastDayOfThisMonth(now());
    }

    public static Date getBeginningOfCurrentDay() {
        return setHourMinuteSecond(now(), 0, 0, 0);
    }

    public static Date getEndOfCurrentDay() {
        return setHourMinuteSecond(now(), 23, 59, 59);
    }

    public static Date getBeginningOfDay(Date date) {
        return !Objects.isNull(date) ? setHourMinuteSecond(date, 0, 0, 0) : null;
    }

    public static Date getEndOfDay(Date date) {
        return !Objects.isNull(date) ? setHourMinuteSecond(date, 23, 59, 59) : null;
    }

    public static int getDayNumber(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDayString(Date date) {
        DateFormat formatter = new SimpleDateFormat("EEEE");
        return formatter.format(date).toUpperCase(Locale.ROOT);
    }

    public static DayType getDayOfWeek(Date date) {
        return Enums.getIfPresent(DayType.class, getDayString(date))
                .toJavaUtil()
                .orElseThrow(RuntimeException::new);
    }

    public static Date getBeginningOfWeek() {
        return getBeginningOfWeek(now());
    }

    public static Date getBeginningOfWeek(Date date) {
        int dayNo = getDayNumber(date);
        return setHourMinuteSecond(addDays(date, 2 - dayNo), 0, 0, 0);
    }

    public static Date getEndOfWeek() {
        return getEndOfWeek(now());
    }

    public static Date getEndOfWeek(Date date) {
        return setHourMinuteSecond(addDays(getBeginningOfWeek(date), 7), 23, 59, 59);
    }

    public static Integer getTotalMinutes(Time time) {
        return time.getMinutes() + (time.getHours() * 60);
    }

    public static ZonedDateTime toZonedDateTime(Date date) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        log.info("Converting date: {} to zonedDateTime: {}", date, zonedDateTime);
        return zonedDateTime;
    }

    public static String formatIsoDatePattern(ZonedDateTime dateTime) {
        return Optional.ofNullable(dateTime)
                .map(ZonedDateTime::toOffsetDateTime)
                .map(OffsetDateTime::toString)
                .orElse(null);
    }
}
