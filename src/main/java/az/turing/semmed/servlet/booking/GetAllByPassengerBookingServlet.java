package az.turing.semmed.servlet.booking;

import az.turing.semmed.controller.BookingController;
import az.turing.semmed.model.dto.BookingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GetAllByPassengerBookingServlet extends BookingServlet {

    public GetAllByPassengerBookingServlet(BookingController bookingController, ObjectMapper objectMapper) {
        this.bookingController = bookingController;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json");

        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write("Passenger fullname is missing in the path");
            writer.close();
            return;
        }

        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            String fullname = path.substring(1);

            if (fullname.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                outputStream.write(objectMapper.writeValueAsBytes("Parameter cannot be empty"));
                return;
            }

            List<BookingDto> allBookingsByPassenger = bookingController.findAllBookingsByPassenger(fullname);
            outputStream.write(objectMapper.writeValueAsBytes(allBookingsByPassenger));
        }
    }
}
