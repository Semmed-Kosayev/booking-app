package az.turing.semmed.model.dto;

import az.turing.semmed.domain.entity.FlightEntity;

public record BookingDto(
        long bookingId,
        String bookerName,
        String bookerSurname,
        FlightEntity flight
) {
}
