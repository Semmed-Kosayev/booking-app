package az.turing.semmed.domain.dao.impl;

import az.turing.semmed.domain.dao.FlightDao;
import az.turing.semmed.domain.entity.FlightEntity;
import az.turing.semmed.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static az.turing.semmed.config.DatabaseConfig.getConnection;

public class FlightDatabaseDao extends FlightDao {

    public FlightDatabaseDao() {
        createFlightTableIfNotExists();
    }

    @Override
    public List<FlightEntity> getAll() {
        List<FlightEntity> flights = new ArrayList<>();
        String query = "SELECT * FROM flights";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                FlightEntity flight = mapRowToFlightEntity(resultSet);
                flights.add(flight);
            }

        } catch (SQLException e) {
            System.err.println("Something has gone wrong in getAll method of FlightDatabaseDao");
            e.printStackTrace();
        }

        return flights;
    }

    @Override
    public Optional<FlightEntity> getById(final Long id) {
        String query = "SELECT * FROM flights WHERE flight_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    FlightEntity flight = mapRowToFlightEntity(resultSet);
                    return Optional.of(flight);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public FlightEntity create(final FlightEntity flightToCreate) {
        final String insertFlightQuery = """
                INSERT INTO flights (destination, from_location, departure_time, available_seats)
                VALUES (?, ?, ?, ?)
                RETURNING flight_id
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertFlightQuery)) {

            statement.setString(1, flightToCreate.getDestination());
            statement.setString(2, flightToCreate.getFrom());
            statement.setTimestamp(3, Timestamp.valueOf(flightToCreate.getDepartureTime()));
            statement.setInt(4, flightToCreate.getAvailableSeats());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new DataAccessException("Failed to save flight - no ID returned.");
                }

                int generatedId = resultSet.getInt("flight_id");
                flightToCreate.setFlightId(generatedId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flightToCreate;
    }

    @Override
    public boolean deleteById(final Long id) {
        String query = "DELETE FROM flights WHERE flight_id = ?";

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

    private FlightEntity mapRowToFlightEntity(ResultSet resultSet) throws SQLException {
        long flightId = resultSet.getLong("flight_id");
        String destination = resultSet.getString("destination");
        String fromLocation = resultSet.getString("from_location");
        LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
        int availableSeats = resultSet.getInt("available_seats");

        return new FlightEntity(flightId, destination, fromLocation, departureTime, availableSeats);
    }

    @Override
    public FlightEntity updateAvailableSeats(final long flightId, final int newAvailableSeatCount) {
        final String updateSeatsQuery = "UPDATE flights SET available_seats = ? WHERE flight_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSeatsQuery)) {

            statement.setInt(1, newAvailableSeatCount);
            statement.setLong(2, flightId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("No flight found with ID: " + flightId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return getById(flightId)
                .orElseThrow(() -> new DataAccessException(
                        "Flight with ID %d not found after update.".formatted(flightId))
                );
    }

    @Override
    public List<FlightEntity> getAllFlightsWithin24Hours() {
        List<FlightEntity> flights = new ArrayList<>();
        String query = """
                SELECT flight_id, destination, from_location, departure_time, available_seats
                FROM flights
                WHERE departure_time BETWEEN NOW() AND NOW() + INTERVAL '1 DAY'
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                FlightEntity flight = mapRowToFlightEntity(resultSet);
                flights.add(flight);
            }

        } catch (SQLException e) {
            System.err.println("Database error while fetching flights within 24 hours: " + e.getMessage());
        }

        return flights;
    }


    private boolean createFlightTableIfNotExists() {
        boolean result = false;
        String query = """
                CREATE TABLE IF NOT EXISTS flights (
                    flight_id SERIAL PRIMARY KEY,
                    destination VARCHAR(255) NOT NULL,
                    from_location VARCHAR(255) NOT NULL,
                    departure_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                    available_seats INTEGER NOT NULL CHECK (available_seats >= 0)
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
