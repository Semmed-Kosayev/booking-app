package az.turing.semmed.domain.dao.impl;

import az.turing.semmed.domain.dao.BookingDao;
import az.turing.semmed.domain.entity.BookingEntity;
import az.turing.semmed.domain.entity.FlightEntity;
import az.turing.semmed.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static az.turing.semmed.config.DatabaseConfig.getConnection;

public class BookingDatabaseDao extends BookingDao {

    public BookingDatabaseDao() {
        createBBookingTableIfNotExists();
    }

    @Override
    public List<BookingEntity> getAll() {
        List<BookingEntity> allBookings = new ArrayList<>();
        String query = """
                SELECT b.booking_id, b.booker_name, b.booker_surname,
                f.flight_id, f.destination, f.from_location, f.departure_time, f.available_seats
                FROM bookings b
                JOIN flights f ON b.flight_id = f.flight_id""";


        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                BookingEntity booking = mapRowToBookingEntity(resultSet);
                allBookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allBookings;
    }

    @Override
    public Optional<BookingEntity> getById(Long id) {
        String query = """
                SELECT b.booking_id, b.booker_name, b.booker_surname,
                f.flight_id, f.destination, f.from_location, f.departure_time, f.available_seats
                FROM bookings b
                JOIN flights f ON b.flight_id = f.flight_id
                WHERE b.booking_id = ?""";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    BookingEntity booking = mapRowToBookingEntity(resultSet);
                    return Optional.of(booking);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public BookingEntity create(BookingEntity bookingToCreate) {
        String insertBookingQuery = """
                INSERT INTO bookings (booker_name, booker_surname, flight_id)
                VALUES (?, ?, ?)
                RETURNING booking_id""";

        try (Connection connection = getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertBookingQuery)) {

            insertStatement.setString(1, bookingToCreate.getBookerName());
            insertStatement.setString(2, bookingToCreate.getBookerSurname());
            insertStatement.setLong(3, bookingToCreate.getFlight().getFlightId());

            try (ResultSet resultSet = insertStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new DataAccessException("Failed to save flight - no ID returned.");
                }

                int generatedId = resultSet.getInt("booking_id");
                bookingToCreate.setBookingId(generatedId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookingToCreate;
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "DELETE FROM bookings WHERE booking_id = ?";

        boolean result = false;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            result = statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private BookingEntity mapRowToBookingEntity(ResultSet resultSet) throws SQLException {
        BookingEntity bookingEntity = new BookingEntity();

        FlightEntity flight = new FlightEntity(
                resultSet.getLong("flight_id"),
                resultSet.getString("destination"),
                resultSet.getString("from_location"),
                resultSet.getTimestamp("departure_time").toLocalDateTime(),
                resultSet.getInt("available_seats")
        );
        long bookingId = resultSet.getLong("booking_id");
        String bookerName = resultSet.getString("booker_name");
        String bookerSurname = resultSet.getString("booker_surname");

        bookingEntity.setBookingId(bookingId);
        bookingEntity.setBookerName(bookerName);
        bookingEntity.setBookerSurname(bookerSurname);
        bookingEntity.setFlight(flight);

        return bookingEntity;
    }

    private boolean createBBookingTableIfNotExists() {
        boolean result = false;
        String query = """
                CREATE TABLE IF NOT EXISTS bookings (
                    booking_id SERIAL PRIMARY KEY,
                    booker_name VARCHAR(100) NOT NULL,
                    booker_surname VARCHAR(100) NOT NULL,
                    flight_id INTEGER NOT NULL,
                    CONSTRAINT bookings_flight_id_fkey FOREIGN KEY (flight_id)
                        REFERENCES flights(flight_id) ON DELETE CASCADE
                );
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            result = statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
