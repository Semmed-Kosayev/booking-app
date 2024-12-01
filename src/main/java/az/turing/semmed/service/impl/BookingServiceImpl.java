package az.turing.semmed.service.impl;

import az.turing.semmed.domain.dao.BookingDao;
import az.turing.semmed.domain.dao.FlightDao;
import az.turing.semmed.domain.entity.BookingEntity;
import az.turing.semmed.domain.entity.FlightEntity;
import az.turing.semmed.exception.BookingNotFoundException;
import az.turing.semmed.exception.FlightNotFoundException;
import az.turing.semmed.mapper.BookingMapper;
import az.turing.semmed.model.dto.BookingDto;
import az.turing.semmed.model.dto.CreateBookingRequest;
import az.turing.semmed.service.BookingService;

import java.util.List;

public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;
    private final FlightDao flightDao;
    private final BookingMapper mapper;

    public BookingServiceImpl(BookingDao bookingDao, FlightDao flightDao, BookingMapper mapper) {
        this.bookingDao = bookingDao;
        this.flightDao = flightDao;
        this.mapper = mapper;
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingDao.getAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public BookingDto getBookingDetails(long bookingId) {
        return bookingDao.getById(bookingId).map(mapper::toDto)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));
    }

    @Override
    public List<BookingDto> findAllBookingByPassenger(String fullName) {
        return bookingDao.getAll().stream()
                .filter(b -> b.getFullName().equalsIgnoreCase(fullName))
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public BookingDto createBooking(CreateBookingRequest request) {
        long flightId = request.flightId();
        FlightEntity flight = flightDao.getById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + flightId));

        BookingEntity savedBooking = bookingDao.create(
                new BookingEntity(request.bookerName(), request.bookerSurname(), flight)
        );

        flightDao.updateAvailableSeats(flightId, flight.getAvailableSeats() - 1);

        return mapper.toDto(savedBooking);
    }

    @Override
    public boolean cancelBooking(long bookingId) {
        BookingEntity bookingEntity = bookingDao.getById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Booking not found with id: " + bookingId));
        FlightEntity flight = bookingEntity.getFlight();

        flightDao.updateAvailableSeats(flight.getFlightId(), flight.getAvailableSeats() + 1);

        return bookingDao.deleteById(bookingId);
    }
}
