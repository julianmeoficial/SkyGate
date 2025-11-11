package com.skygate.backend.util;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final class AutomataConstants {
        public static final String INITIAL_STATE = "S0";
        public static final String FINAL_STATE_PARKED = "S5";
        public static final String CONFLICT_STATE = "S6";
        public static final int MAX_TRANSITION_ATTEMPTS = 3;
        public static final long TRANSITION_TIMEOUT_MS = 5000L;

        private AutomataConstants() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    public static final class GateConstants {
        public static final int MIN_GATE_NUMBER = 1;
        public static final int MAX_GATE_NUMBER = 999;
        public static final String DEFAULT_TERMINAL = "A";
        public static final int LED_ACTIVATION_DELAY_MS = 1000;
        public static final int GATE_OCCUPANCY_TIMEOUT_MINUTES = 180;

        private GateConstants() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    public static final class FlightConstants {
        public static final int FLIGHT_NUMBER_MIN_LENGTH = 4;
        public static final int FLIGHT_NUMBER_MAX_LENGTH = 10;
        public static final int DEFAULT_ARRIVAL_BUFFER_MINUTES = 15;
        public static final int DEFAULT_DEPARTURE_BUFFER_MINUTES = 30;
        public static final int MAX_FLIGHT_DELAY_HOURS = 24;

        private FlightConstants() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    public static final class AircraftConstants {
        public static final double JUMBO_MIN_WINGSPAN = 70.0;
        public static final double WIDE_BODY_MIN_WINGSPAN = 50.0;
        public static final double NARROW_BODY_MAX_WINGSPAN = 50.0;
        public static final int JUMBO_MIN_PASSENGERS = 400;
        public static final int WIDE_BODY_MIN_PASSENGERS = 200;

        private AircraftConstants() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    public static final class MqttConstants {
        public static final String TOPIC_GATE_ASSIGNMENT = "skygate/assignment";
        public static final String TOPIC_LED_CONTROL = "skygate/led/control";
        public static final String TOPIC_SENSOR_DETECTION = "skygate/sensor/detection";
        public static final String TOPIC_GATE_STATUS = "skygate/gate/status";
        public static final int QOS_LEVEL = 1;
        public static final int CONNECTION_TIMEOUT = 10;
        public static final int KEEP_ALIVE_INTERVAL = 60;

        private MqttConstants() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    public static final class ApiConstants {
        public static final String API_VERSION = "v1";
        public static final String API_BASE_PATH = "/api/" + API_VERSION;
        public static final String GATE_ENDPOINT = API_BASE_PATH + "/gates";
        public static final String FLIGHT_ENDPOINT = API_BASE_PATH + "/flights";
        public static final String ASSIGNMENT_ENDPOINT = API_BASE_PATH + "/assignments";
        public static final String AIRCRAFT_ENDPOINT = API_BASE_PATH + "/aircraft";
        public static final String AUTOMATA_ENDPOINT = API_BASE_PATH + "/automata";

        private ApiConstants() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    public static final class ValidationMessages {
        public static final String GATE_NOT_FOUND = "Gate not found";
        public static final String FLIGHT_NOT_FOUND = "Flight not found";
        public static final String AIRCRAFT_NOT_FOUND = "Aircraft not found";
        public static final String ASSIGNMENT_NOT_FOUND = "Assignment not found";
        public static final String INVALID_STATE_TRANSITION = "Invalid state transition";
        public static final String NO_AVAILABLE_GATE = "No available gate for this aircraft type";
        public static final String GATE_ALREADY_OCCUPIED = "Gate is already occupied";
        public static final String INVALID_FLIGHT_NUMBER = "Invalid flight number format";

        private ValidationMessages() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    public static final class DateTimeFormats {
        public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String TIME_FORMAT = "HH:mm:ss";
        public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";

        private DateTimeFormats() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }

    public static final class LedColors {
        public static final String GREEN = "GREEN";
        public static final String RED = "RED";
        public static final String YELLOW = "YELLOW";
        public static final String BLUE = "BLUE";
        public static final String ORANGE = "ORANGE";
        public static final String OFF = "OFF";

        private LedColors() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
    }
}
