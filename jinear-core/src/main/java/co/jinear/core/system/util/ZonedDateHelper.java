package co.jinear.core.system.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRules;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@UtilityClass
public class ZonedDateHelper {

    public static final String DATE_TIME_FORMAT_1 = "(HH:mm dd.MM.yyyy)";
    public static final String DATE_TIME_FORMAT_2 = "dd.MM.yyyy";
    public static final String DATE_TIME_FORMAT_3 = "dd.MM.yyyy HH:mm";
    public static final String DATE_TIME_FORMAT_4 = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT_5 = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private static final String UTC_ZONE_ID = "UTC";
    private static final String ETC_GMT_ZONE_PREFIX = "Etc/GMT";
    private static final int SECONDS_IN_HOUR = 3600;

    public static ZonedDateTime atTimeZone(ZonedDateTime zonedDateTime, String timeZone) {
        return Optional.ofNullable(timeZone)
                .map(ZoneId::of)
                .map(zonedDateTime::withZoneSameInstant)
                .orElseGet(() -> zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()));
    }

    public static ZoneId zoneIdOrUtc(String timeZone) {
        if (Objects.isNull(timeZone) || timeZone.isBlank()) {
            return ZoneOffset.UTC;
        }
        try {
            return ZoneId.of(timeZone);
        } catch (DateTimeException exception) {
            log.warn("Unreadable time zone, falling back to UTC. timeZone: {}", timeZone);
            return ZoneOffset.UTC;
        }
    }

    public static String ianaZoneIdOf(ZoneId zoneId) {
        ZoneRules rules = zoneId.getRules();
        if (!rules.isFixedOffset()) {
            return zoneId.getId();
        }
        ZoneOffset offset = rules.getOffset(Instant.EPOCH);
        String etcZoneId = etcZoneIdOf(offset);
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        if (availableZoneIds.contains(etcZoneId)) {
            return etcZoneId;
        }
        return UTC_ZONE_ID;
    }

    private static String etcZoneIdOf(ZoneOffset offset) {
        int totalSeconds = offset.getTotalSeconds();
        if (totalSeconds == 0 || totalSeconds % SECONDS_IN_HOUR != 0) {
            return UTC_ZONE_ID;
        }
        int hours = totalSeconds / SECONDS_IN_HOUR;
        // Etc/GMT zones use the POSIX sign, so UTC+3 is Etc/GMT-3.
        String posixSign = hours > 0 ? "-" : "+";
        return ETC_GMT_ZONE_PREFIX + posixSign + Math.abs(hours);
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
