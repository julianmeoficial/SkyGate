package com.skygate.backend.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class DateUtil {

    private DateUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalTime currentTime() {
        return LocalTime.now();
    }

    public static LocalDateTime addMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusMinutes(minutes);
    }

    public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusHours(hours);
    }

    public static LocalDateTime subtractMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusMinutes(minutes);
    }

    public static LocalDateTime subtractHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusHours(hours);
    }

    public static long getMinutesBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(start, end);
    }

    public static long getHoursBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(start, end);
    }

    public static Duration getDuration(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return Duration.ZERO;
        }
        return Duration.between(start, end);
    }

    public static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isAfter(dateTime2);
    }

    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isBefore(dateTime2);
    }

    public static boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isEqual(dateTime2);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DateTimeFormats.DATETIME_FORMAT);
        return dateTime.format(formatter);
    }

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DateTimeFormats.DATE_FORMAT);
        return date.format(formatter);
    }

    public static String formatTime(LocalTime time) {
        if (time == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DateTimeFormats.TIME_FORMAT);
        return time.format(formatter);
    }

    public static String formatWithPattern(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || pattern == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DateTimeFormats.DATETIME_FORMAT);
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDateTime parseDateTimeWithPattern(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || pattern == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static ZonedDateTime convertToZone(LocalDateTime dateTime, String zoneId) {
        if (dateTime == null || zoneId == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.of(zoneId));
    }

    public static boolean isBetween(LocalDateTime target, LocalDateTime start, LocalDateTime end) {
        if (target == null || start == null || end == null) {
            return false;
        }
        return (target.isEqual(start) || target.isAfter(start)) && (target.isEqual(end) || target.isBefore(end));
    }

    public static LocalDateTime getStartOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay();
    }

    public static LocalDateTime getEndOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(23, 59, 59);
    }

    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isEqual(LocalDate.now());
    }

    public static boolean isPast(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isBefore(LocalDateTime.now());
    }

    public static boolean isFuture(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isAfter(LocalDateTime.now());
    }
}
