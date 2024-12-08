package az.turing.semmed.servlet.flight;

import az.turing.semmed.controller.FlightController;
import az.turing.semmed.model.dto.FlightDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GetByIdFlightServlet extends FlightServlet {

    public GetByIdFlightServlet(FlightController flightController, ObjectMapper objectMapper) {
        this.flightController = flightController;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ID is missing in the path");
            return;
        }
        ServletOutputStream outputStream = resp.getOutputStream();
        try {
            long id = Long.parseLong(path.substring(1));
            FlightDto flightById = flightController.getFlightById(id);
            resp.setContentType("application/json");
            outputStream.write(objectMapper.writeValueAsBytes(flightById));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes("Invalid ID format"));
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes(e.getMessage()));
        } finally {
            outputStream.close();
        }
    }
}