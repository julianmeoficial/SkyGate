package com.skygate.backend.util;

import com.skygate.backend.model.enums.AircraftType;
import com.skygate.backend.model.enums.GateType;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public final class ValidationUtil {

    private static final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("^[A-Z]{2}\\d{3,4}$");
    private static final Pattern GATE_NUMBER_PATTERN = Pattern.compile("^[A-Z]\\d{1,3}$");

    private ValidationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean isValidFlightNumber(String flightNumber) {
        if (isNullOrEmpty(flightNumber)) {
            return false;
        }
        return FLIGHT_NUMBER_PATTERN.matcher(flightNumber.trim().toUpperCase()).matches();
    }

    public static boolean isValidGateNumber(String gateNumber) {
        if (isNullOrEmpty(gateNumber)) {
            return false;
        }
        return GATE_NUMBER_PATTERN.matcher(gateNumber.trim().toUpperCase()).matches();
    }

    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    public static boolean isPositiveNumber(Number number) {
        if (number == null) {
            return false;
        }
        return number.doubleValue() > 0;
    }

    public static boolean isNonNegativeNumber(Number number) {
        if (number == null) {
            return false;
        }
        return number.doubleValue() >= 0;
    }

    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean isValidDateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end) || start.isEqual(end);
    }

    public static boolean isDateInFuture(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isAfter(LocalDateTime.now());
    }

    public static boolean isDateInPast(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isBefore(LocalDateTime.now());
    }

    public static boolean isAircraftCompatibleWithGate(AircraftType aircraftType, GateType gateType) {
        if (aircraftType == null || gateType == null) {
            return false;
        }
        return aircraftType.isCompatibleWith(gateType);
    }

    public static boolean hasMinLength(String str, int minLength) {
        if (str == null) {
            return false;
        }
        return str.length() >= minLength;
    }

    public static boolean hasMaxLength(String str, int maxLength) {
        if (str == null) {
            return false;
        }
        return str.length() <= maxLength;
    }

    public static boolean isLengthBetween(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }

    public static boolean isValidWingspan(Double wingspan, AircraftType aircraftType) {
        if (wingspan == null || aircraftType == null) {
            return false;
        }
        switch (aircraftType) {
            case JUMBO:
                return wingspan >= Constants.AircraftConstants.JUMBO_MIN_WINGSPAN;
            case WIDE_BODY:
                return wingspan >= Constants.AircraftConstants.WIDE_BODY_MIN_WINGSPAN
                        && wingspan < Constants.AircraftConstants.JUMBO_MIN_WINGSPAN;
            case NARROW_BODY:
                return wingspan <= Constants.AircraftConstants.NARROW_BODY_MAX_WINGSPAN;
            default:
                return false;
        }
    }

    public static boolean isValidPassengerCapacity(Integer capacity, AircraftType aircraftType) {
        if (capacity == null || aircraftType == null || capacity <= 0) {
            return false;
        }
        switch (aircraftType) {
            case JUMBO:
                return capacity >= Constants.AircraftConstants.JUMBO_MIN_PASSENGERS;
            case WIDE_BODY:
                return capacity >= Constants.AircraftConstants.WIDE_BODY_MIN_PASSENGERS
                        && capacity < Constants.AircraftConstants.JUMBO_MIN_PASSENGERS;
            case NARROW_BODY:
                return capacity < Constants.AircraftConstants.WIDE_BODY_MIN_PASSENGERS;
            default:
                return false;
        }
    }

    public static String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().replaceAll("[^a-zA-Z0-9\\s-]", "");
    }

    public static boolean containsOnlyDigits(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return str.matches("\\d+");
    }

    public static boolean containsOnlyLetters(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }

    public static boolean isAlphanumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z0-9]+");
    }
}
