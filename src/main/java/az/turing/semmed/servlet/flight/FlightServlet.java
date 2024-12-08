package az.turing.semmed.servlet.flight;

import az.turing.semmed.controller.FlightController;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;

public abstract class FlightServlet extends HttpServlet {

    protected FlightController flightController;
    protected ObjectMapper objectMapper;
}
