package az.turing.semmed.controller;

import az.turing.semmed.model.dto.FlightDto;
import az.turing.semmed.service.FlightService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    public FlightDto getFlightById(long flightId) {
        if (flightId <= 0) {
            throw new IllegalArgumentException("ID of the flight can not be negative or zero");
        }

        return flightService.getFlightById(flightId);
    }

    public List<FlightDto> getAllFlights() {
        return flightService.getAllFlights();
    }

    public List<FlightDto> getAllFlightsWithin24Hours() {
        return flightService.getAllFlightsWithin24Hours();
    }

    public List<FlightDto> searchFlights(@NotEmpty String destination, @NotNull LocalDate date, int numberOfPeople) {
        if (numberOfPeople <= 0) {
            throw new IllegalArgumentException("Number of people cannot be negative or zero");
        }

        return flightService.findFlights(destination, date, numberOfPeople);
    }
}
