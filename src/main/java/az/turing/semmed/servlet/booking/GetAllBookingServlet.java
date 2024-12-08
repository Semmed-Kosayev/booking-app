package az.turing.semmed.servlet.booking;

import az.turing.semmed.controller.BookingController;
import az.turing.semmed.model.dto.BookingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class GetAllBookingServlet extends BookingServlet {

    public GetAllBookingServlet(BookingController bookingController, ObjectMapper objectMapper) {
        this.bookingController = bookingController;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            resp.setContentType("application/json");
            List<BookingDto> allBookings = bookingController.getAllBookings();
            outputStream.write(objectMapper.writeValueAsBytes(allBookings));
        }
    }
}
