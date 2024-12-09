package az.turing.semmed.util;

import az.turing.semmed.controller.BookingController;
import az.turing.semmed.controller.FlightController;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DependencyInjector {
    private static FlightController flightController;
    private static BookingController bookingController;
    private static ObjectMapper objectMapper;

    public static void setFlightController(FlightController controller) {
        flightController = controller;
    }

    public static void setBookingController(BookingController controller) {
        bookingController = controller;
    }

    public static void setObjectMapper(ObjectMapper mapper) {
        objectMapper = mapper;
    }

    public static FlightController getFlightController() {
        return flightController;
    }

    public static BookingController getBookingController() {
        return bookingController;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}