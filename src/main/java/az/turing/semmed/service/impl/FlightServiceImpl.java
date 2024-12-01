package az.turing.semmed.service.impl;

import az.turing.semmed.domain.dao.impl.FlightDatabaseDao;
import az.turing.semmed.exception.FlightNotFoundException;
import az.turing.semmed.mapper.FlightMapper;
import az.turing.semmed.model.dto.FlightDto;
import az.turing.semmed.service.FlightService;

import java.time.LocalDate;
import java.util.List;

public class FlightServiceImpl implements FlightService {

    private final FlightDatabaseDao flightDao;
    private final FlightMapper mapper;

    public FlightServiceImpl(FlightDatabaseDao flightDao, FlightMapper mapper) {
        this.flightDao = flightDao;
        this.mapper = mapper;
    }

    @Override
    public FlightDto getFlightById(long flightId) {
        return flightDao.getById(flightId)
                .map(mapper::toDto)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + flightId));
    }

    @Override
    public List<FlightDto> getAllFlights() {
        return flightDao.getAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<FlightDto> getAllFlightsWithin24Hours() {
        return flightDao
                .getAllFlightsWithin24Hours().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<FlightDto> findFlights(String destination, LocalDate date, int numberOfPeople) {
        return flightDao.getAll().stream()
                .filter(f -> f.getDestination().equalsIgnoreCase(destination))
                .filter(f -> f.getDepartureTime().toLocalDate().equals(date))
                .filter(f -> f.getAvailableSeats() >= numberOfPeople)
                .map(mapper::toDto)
                .toList();
    }
}
