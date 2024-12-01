package az.turing.semmed.service;

import az.turing.semmed.model.dto.BookingDto;
import az.turing.semmed.model.dto.CreateBookingRequest;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(CreateBookingRequest createBookingRequest);

    boolean cancelBooking(long bookingId);

    List<BookingDto> findAllBookingByPassenger(String fullName);

    BookingDto getBookingDetails(long bookingId);

    List<BookingDto> getAllBookings();
}
