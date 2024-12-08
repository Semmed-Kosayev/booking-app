package az.turing.semmed;

import az.turing.semmed.controller.FlightController;
import az.turing.semmed.domain.dao.FlightDao;
import az.turing.semmed.domain.dao.impl.FlightDatabaseDao;
import az.turing.semmed.mapper.FlightMapper;
import az.turing.semmed.service.FlightService;
import az.turing.semmed.service.impl.FlightServiceImpl;
import az.turing.semmed.servlet.flight.GetAllFlightServlet;
import az.turing.semmed.servlet.flight.GetByIdFlightServlet;
import az.turing.semmed.servlet.flight.SearchFlightServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class App {

    public static void main(String[] args) throws Exception {
        final FlightDao flightDao = new FlightDatabaseDao();
        final FlightMapper flightMapper = new FlightMapper();
        final FlightService flightService = new FlightServiceImpl(flightDao, flightMapper);
        final FlightController flightController = new FlightController(flightService);
        final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        Tomcat server = new Tomcat();
        Connector connector = server.getConnector();
        connector.setPort(8081);

        Context context = server.addContext("", null);

        Tomcat.addServlet(context, "getAllFlightServlet", new GetAllFlightServlet(flightController, objectMapper));
        context.addServletMappingDecoded("/flights", "getAllFlightServlet");

        Tomcat.addServlet(context, "getByIdFlightServlet", new GetByIdFlightServlet(flightController, objectMapper));
        context.addServletMappingDecoded("/flights/*", "getByIdFlightServlet");

        Tomcat.addServlet(context, "searchFlightServlet", new SearchFlightServlet(flightController, objectMapper));
        context.addServletMappingDecoded("/flights/search", "searchFlightServlet");

        server.start();
    }
}
