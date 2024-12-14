package az.turing.semmed.util;

import az.turing.semmed.service.BookingService;
import az.turing.semmed.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DependencyInjector {
    private static FlightService flightService;
    private static BookingService bookingService;
    private static ObjectMapper objectMapper;

    public static void setFlightService(FlightService flightService) {
        DependencyInjector.flightService = flightService;
    }

    public static void setBookingService(BookingService bookingService) {
        DependencyInjector.bookingService = bookingService;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        DependencyInjector.objectMapper = objectMapper;
    }

    public static FlightService getFlightService() {
        return flightService;
    }

    public static BookingService getBookingService() {
        return bookingService;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}