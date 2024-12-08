package az.turing.semmed.servlet.booking;

import az.turing.semmed.controller.BookingController;
import az.turing.semmed.exception.BookingNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class CancelBookingServlet extends BookingServlet {

    public CancelBookingServlet(BookingController bookingController, ObjectMapper objectMapper) {
        this.bookingController = bookingController;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json");

        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write("ID is missing in the path");
            writer.close();
            return;
        }

        ServletOutputStream outputStream = resp.getOutputStream();
        try {
            long id = Long.parseLong(path.substring(1));
            bookingController.cancelBooking(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes("Invalid ID format"));
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes(e.getMessage()));
        } catch (BookingNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            outputStream.write(objectMapper.writeValueAsBytes(e.getMessage()));
        } finally {
            outputStream.close();
        }
    }
}