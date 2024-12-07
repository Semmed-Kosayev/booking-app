package az.turing.semmed;

import az.turing.semmed.controller.FlightController;
import az.turing.semmed.domain.dao.FlightDao;
import az.turing.semmed.domain.dao.impl.FlightDatabaseDao;
import az.turing.semmed.mapper.FlightMapper;
import az.turing.semmed.service.FlightService;
import az.turing.semmed.service.impl.FlightServiceImpl;
import az.turing.semmed.servlet.FlightServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class App {

    public static void main(String[] args) throws Exception {
        final FlightDao flightDao = new FlightDatabaseDao();
        final FlightMapper flightMapper = new FlightMapper();
        final FlightService flightService = new FlightServiceImpl(flightDao, flightMapper);
        final FlightController flightController = new FlightController(flightService);
        final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        Server server = new Server(8081);

        ServletContextHandler handler = new ServletContextHandler();
        handler.addServlet(
                new ServletHolder(new FlightServlet(flightController, objectMapper)), "/flights");

        server.setHandler(handler);
        server.start();
        server.join();
    }
}
