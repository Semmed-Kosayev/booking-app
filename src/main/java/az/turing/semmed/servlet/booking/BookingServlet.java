package az.turing.semmed.servlet.booking;

import az.turing.semmed.controller.BookingController;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;

public abstract class BookingServlet  extends HttpServlet {

    protected BookingController bookingController;
    protected ObjectMapper objectMapper;
}
