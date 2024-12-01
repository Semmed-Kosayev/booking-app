package az.turing.semmed.domain.dao;

import az.turing.semmed.domain.entity.FlightEntity;

import java.util.List;

public abstract class FlightDao implements Dao<FlightEntity, Long> {

    public abstract FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount);

    public abstract List<FlightEntity> getAllFlightsWithin24Hours();
}
