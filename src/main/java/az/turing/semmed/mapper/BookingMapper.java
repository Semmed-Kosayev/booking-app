package az.turing.semmed.mapper;

import az.turing.semmed.domain.entity.BookingEntity;
import az.turing.semmed.model.dto.BookingDto;

public class BookingMapper implements EntityMapper<BookingEntity, BookingDto>{
    @Override
    public BookingEntity toEntity(BookingDto bookingDto) {
        return new BookingEntity(
                bookingDto.bookingId(),
                bookingDto.bookerName(),
                bookingDto.bookerSurname(),
                bookingDto.flight()
        );
    }

    @Override
    public BookingDto toDto(BookingEntity bookingEntity) {
        return new BookingDto(
                bookingEntity.getBookingId(),
                bookingEntity.getBookerName(),
                bookingEntity.getBookerSurname(),
                bookingEntity.getFlight()
        );
    }
}
