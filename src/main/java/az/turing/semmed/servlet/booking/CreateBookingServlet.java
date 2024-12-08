package az.turing.semmed.servlet.booking;

import az.turing.semmed.controller.BookingController;
import az.turing.semmed.exception.FlightNotFoundException;
import az.turing.semmed.model.dto.BookingDto;
import az.turing.semmed.model.dto.CreateBookingRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateBookingServlet extends BookingServlet {

    public CreateBookingServlet(BookingController bookingController, ObjectMapper objectMapper) {
        this.bookingController = bookingController;
        this.objectMapper = objectMapper;
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

            if (
                    createBookingRequest.bookerName() == null ||
                    createBookingRequest.bookerSurname() == null ||
                    createBookingRequest.flightId() == 0
            ) {
                throw new IllegalArgumentException("Invalid object structure: Missing object fields");
            }

            BookingDto createdBooking = bookingController.createBooking(createBookingRequest);
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