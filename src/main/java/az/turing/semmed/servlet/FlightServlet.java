package az.turing.semmed.servlet;

import az.turing.semmed.controller.FlightController;
import az.turing.semmed.model.dto.FlightDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

public class FlightServlet extends HttpServlet {
    private final FlightController flightController;
    private final ObjectMapper objectMapper;

    public FlightServlet(FlightController flightController, ObjectMapper objectMapper) {
        this.flightController = flightController;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<FlightDto> allFlights = flightController.getAllFlights();

        ServletOutputStream outputStream = resp.getOutputStream();

        resp.setContentType(MediaType.APPLICATION_JSON);
        outputStream.write(objectMapper.writeValueAsBytes(allFlights));
        outputStream.close();
    }
}
