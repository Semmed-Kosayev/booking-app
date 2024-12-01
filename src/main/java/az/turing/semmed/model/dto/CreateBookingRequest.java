package az.turing.semmed.model.dto;

public record CreateBookingRequest(
        long flightId,
        String bookerName,
        String bookerSurname
) {
}
