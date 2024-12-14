package az.turing.semmed.servlet.flight;

import az.turing.semmed.exception.FlightNotFoundException;
import az.turing.semmed.model.dto.FlightDto;
import az.turing.semmed.util.DependencyInjector;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getByIdFlightServlet", urlPatterns = "/flights/*")
public class GetByIdFlightServlet extends FlightServlet {

    public GetByIdFlightServlet() {
        this.flightService = DependencyInjector.getFlightService();
        this.objectMapper = DependencyInjector.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json");

        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter writer = resp.getWriter();
            writer.write("ID is missing in the path");
            writer.close();
            return;
        }
        ServletOutputStream outputStream = resp.getOutputStream();
        try {
            long id = Long.parseLong(path.substring(1));
            if (id <= 0) {
                throw new IllegalArgumentException("ID of the flight can not be negative or zero");
            }
            FlightDto flightById = flightService.getFlightById(id);
            outputStream.write(objectMapper.writeValueAsBytes(flightById));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes("Invalid ID format"));
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outputStream.write(objectMapper.writeValueAsBytes(e.getMessage()));
        } catch (FlightNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            outputStream.write(objectMapper.writeValueAsBytes(e.getMessage()));
        } finally {
            outputStream.close();
        }
    }
}