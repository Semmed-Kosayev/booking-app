package az.turing.semmed.servlet.flight;

import az.turing.semmed.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;

public abstract class FlightServlet extends HttpServlet {

    protected FlightService flightService;
    protected ObjectMapper objectMapper;
}
