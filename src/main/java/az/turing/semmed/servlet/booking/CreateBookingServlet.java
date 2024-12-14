package az.turing.semmed.servlet.booking;

import az.turing.semmed.exception.FlightNotFoundException;
import az.turing.semmed.model.dto.BookingDto;
import az.turing.semmed.model.dto.CreateBookingRequest;
import az.turing.semmed.util.DependencyInjector;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "createBookingServlet", urlPatterns = "/bookings/create")
public class CreateBookingServlet extends BookingServlet {

    public CreateBookingServlet() {
        this.bookingService = DependencyInjector.getBookingService();
        this.objectMapper = DependencyInjector.getObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        String bookingToCreate = requestBody.toString();

        if (bookingToCreate.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter writer = resp.getWriter();
            writer.write("Method body must contain the object");
            writer.close();
            return;
        }

        ServletOutputStream outputStream = resp.getOutputStream();
        try {
            CreateBookingRequest createBookingRequest = objectMapper.readValue(bookingToCreate, CreateBookingRequest.class);

            final long flightId = createBookingRequest.flightId();
            final String name = createBookingRequest.bookerName();
            final String surname = createBookingRequest.bookerSurname();

            if (
                    name == null ||
                            surname == null ||
                            flightId == 0
            ) {
                throw new IllegalArgumentException("Invalid object structure: Missing object fields");
            }

            if (flightId <= 0) {
                throw new IllegalArgumentException("ID of the flight can not be negative or zero");
            }

            if (name.isEmpty() || surname.isEmpty()) {
                throw new IllegalArgumentException("name or surname cannot be empty");
            }

            BookingDto createdBooking = bookingService.createBooking(createBookingRequest);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            outputStream.write(objectMapper.writeValueAsBytes(createdBooking));
        } catch (FlightNotFoundException | IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes(e.getMessage()));
        } catch (JsonMappingException | JsonParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes("Invalid object structure"));
        } finally {
            outputStream.close();
        }
    }
}
