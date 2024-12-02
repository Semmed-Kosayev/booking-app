package az.turing.semmed.controller;

import az.turing.semmed.model.dto.BookingDto;
import az.turing.semmed.model.dto.CreateBookingRequest;
import az.turing.semmed.service.BookingService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public BookingDto createBooking(@NotNull CreateBookingRequest createBookingRequest) {
        long flightId = createBookingRequest.flightId();
        String name = createBookingRequest.bookerName();
        String surname = createBookingRequest.bookerSurname();

        if (flightId <= 0) {
            throw new IllegalArgumentException("ID of the flight can not be negative or zero");
        }

        if (name.isEmpty() || surname.isEmpty()) {
            throw new IllegalArgumentException("name or surname cannot be empty");
        }

        return bookingService.createBooking(createBookingRequest);
    }

    public boolean cancelBooking(long bookingId) {
        if (bookingId <= 0) {
            throw new IllegalArgumentException("ID of the booking can not be negative or zero");
        }

        return bookingService.cancelBooking(bookingId);
    }

    public List<BookingDto> findAllBookingsByPassenger(@NotEmpty String fullName) {
        return bookingService.findAllBookingByPassenger(fullName);
    }

    public BookingDto getBookingDetails(long bookingId) {
        if (bookingId <= 0) {
            throw new IllegalArgumentException("ID of the booking can not be negative or zero");
        }

        return bookingService.getBookingDetails(bookingId);
    }

    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
