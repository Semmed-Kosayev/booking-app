package az.turing.semmed.servlet.flight;

import az.turing.semmed.model.dto.FlightDto;
import az.turing.semmed.util.DependencyInjector;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "getAllFlightServlet", urlPatterns = "/flights")
public class GetAllFlightServlet extends FlightServlet {

    public GetAllFlightServlet() {
        this.flightController = DependencyInjector.getFlightController();
        this.objectMapper = DependencyInjector.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletOutputStream outputStream = resp.getOutputStream();
        try {
            Object isWithin24hours = req.getHeader("within24hours");
            resp.setContentType("application/json");

            if (isWithin24hours != null && String.valueOf(isWithin24hours).equalsIgnoreCase("true")) {
                List<FlightDto> allFlightsWithin24Hours = flightController.getAllFlightsWithin24Hours();
                outputStream.write(objectMapper.writeValueAsBytes(allFlightsWithin24Hours));
                return;
            }

            List<FlightDto> allFlights = flightController.getAllFlights();
            outputStream.write(objectMapper.writeValueAsBytes(allFlights));

        } catch (ClassCastException e) {
            List<FlightDto> allFlights = flightController.getAllFlights();
            outputStream.write(objectMapper.writeValueAsBytes(allFlights));
        } finally {
            outputStream.close();
        }
    }
}
