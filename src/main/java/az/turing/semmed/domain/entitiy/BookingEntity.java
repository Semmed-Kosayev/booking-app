package az.turing.semmed.domain.entitiy;

import java.util.Objects;

public class BookingEntity {

    private long bookingId;
    private String bookerName;
    private String bookerSurname;
    private FlightEntity flight;

    public BookingEntity(long bookingId, String bookerName, String bookerSurname, FlightEntity flight) {
        this.bookingId = bookingId;
        this.bookerName = bookerName;
        this.bookerSurname = bookerSurname;
        this.flight = flight;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookerName() {
        return bookerName;
    }

    public void setBookerName(String bookerName) {
        this.bookerName = bookerName;
    }

    public String getBookerSurname() {
        return bookerSurname;
    }

    public void setBookerSurname(String bookerSurname) {
        this.bookerSurname = bookerSurname;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public String getFullName() {
        return "%s %s".formatted(bookerName, bookerSurname);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return bookingId == that.bookingId &&
                Objects.equals(bookerName, that.bookerName) &&
                Objects.equals(bookerSurname, that.bookerSurname) &&
                Objects.equals(flight, that.flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, bookerName, bookerSurname, flight);
    }

    @Override
    public String toString() {
        return "BookingEntity{bookingId=%d, bookerName='%s', bookerSurname='%s', flight=%s}"
                .formatted(bookingId, bookerName, bookerSurname, flight);
    }
}
