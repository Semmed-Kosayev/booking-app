package az.turing.semmed.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class FlightEntity {

    private long flightId;
    private String destination;
    private String from;
    private LocalDateTime departureTime;
    private int availableSeats;

    public FlightEntity(String destination, String from, LocalDateTime departureTime, int availableSeats) {
        this.destination = destination;
        this.from = from;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    public FlightEntity(long flightId, String destination, String from, LocalDateTime departureTime, int availableSeats) {
        this.flightId = flightId;
        this.destination = destination;
        this.from = from;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightEntity flight = (FlightEntity) o;
        return flightId == flight.flightId &&
                availableSeats == flight.availableSeats &&
                Objects.equals(destination, flight.destination) &&
                Objects.equals(from, flight.from) &&
                Objects.equals(departureTime, flight.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId, destination, from, departureTime, availableSeats);
    }

    @Override
    public String toString() {
        return "FlightEntity{flightId=%d, destination='%s', from='%s', departureTime=%s, availableSeats=%d}"
                .formatted(flightId, destination, from, departureTime, availableSeats);
    }
}
