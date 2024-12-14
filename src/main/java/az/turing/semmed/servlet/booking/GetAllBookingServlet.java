package az.turing.semmed.servlet.booking;

import az.turing.semmed.model.dto.BookingDto;
import az.turing.semmed.util.DependencyInjector;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "getAllBookingServlet", urlPatterns = "/bookings")
public class GetAllBookingServlet extends BookingServlet {

    public GetAllBookingServlet() {
        this.bookingService = DependencyInjector.getBookingService();
        this.objectMapper = DependencyInjector.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            resp.setContentType("application/json");
            List<BookingDto> allBookings = bookingService.getAllBookings();
            outputStream.write(objectMapper.writeValueAsBytes(allBookings));
        }
    }
}
