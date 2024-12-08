package az.turing.semmed.servlet.flight;

import az.turing.semmed.controller.FlightController;
import az.turing.semmed.model.dto.FlightDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static az.turing.semmed.util.ConsoleInputValidator.isAlphabeticOnly;

public class SearchFlightServlet extends FlightServlet {

    public SearchFlightServlet(FlightController flightController, ObjectMapper objectMapper) {
        this.flightController = flightController;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletOutputStream outputStream = resp.getOutputStream();
        resp.setContentType("application/json");

        String destination = req.getParameter("destination");
        String date = req.getParameter("date");
        String numberOfPeople = req.getParameter("numberOfPeople");

        if (destination == null || date == null || numberOfPeople == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes("Parameters cannot be null"));
            return;
        }

        if (destination.isEmpty() || date.isEmpty() || numberOfPeople.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes("Parameters cannot be empty"));
            return;
        }

        LocalDate parsedDate;
        int parsedNumberOfPeople;
        try {
            parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            parsedNumberOfPeople = Integer.parseInt(numberOfPeople);
            List<FlightDto> searchedFlights = flightController.searchFlights(destination, parsedDate, parsedNumberOfPeople);
            outputStream.write(objectMapper.writeValueAsBytes(searchedFlights));
        } catch (DateTimeParseException | NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes("Invalid parameters"));
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes(e.getMessage()));
        } finally {
            outputStream.close();
        }

    }
}
