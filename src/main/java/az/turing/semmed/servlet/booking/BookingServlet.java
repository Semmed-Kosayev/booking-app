package az.turing.semmed.servlet.booking;

import az.turing.semmed.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;

public abstract class BookingServlet  extends HttpServlet {

    protected BookingService bookingService;
    protected ObjectMapper objectMapper;
}
