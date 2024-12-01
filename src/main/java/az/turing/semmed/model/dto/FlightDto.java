package az.turing.semmed.model.dto;

import java.time.LocalDateTime;

public record FlightDto(
        long flightId,
        String destination,
        String from,
        LocalDateTime departureTime,
        int availableSeats
) {
}
