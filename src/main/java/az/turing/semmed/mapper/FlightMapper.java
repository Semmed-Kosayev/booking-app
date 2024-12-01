package az.turing.semmed.mapper;

import az.turing.semmed.domain.entity.FlightEntity;
import az.turing.semmed.model.dto.FlightDto;

public class FlightMapper implements EntityMapper<FlightEntity, FlightDto> {
    @Override
    public FlightEntity toEntity(FlightDto flightDto) {
        return new FlightEntity(
                flightDto.flightId(),
                flightDto.destination(),
                flightDto.from(),
                flightDto.departureTime(),
                flightDto.availableSeats()
        );
    }

    @Override
    public FlightDto toDto(FlightEntity flightEntity) {
        return new FlightDto(
                flightEntity.getFlightId(),
                flightEntity.getDestination(),
                flightEntity.getFrom(),
                flightEntity.getDepartureTime(),
                flightEntity.getAvailableSeats()
        );
    }
}
