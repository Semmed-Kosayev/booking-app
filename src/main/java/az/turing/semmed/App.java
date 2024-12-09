package az.turing.semmed;

import az.turing.semmed.controller.BookingController;
import az.turing.semmed.controller.FlightController;
import az.turing.semmed.domain.dao.BookingDao;
import az.turing.semmed.domain.dao.FlightDao;
import az.turing.semmed.domain.dao.impl.BookingDatabaseDao;
import az.turing.semmed.domain.dao.impl.FlightDatabaseDao;
import az.turing.semmed.mapper.BookingMapper;
import az.turing.semmed.mapper.FlightMapper;
import az.turing.semmed.service.BookingService;
import az.turing.semmed.service.FlightService;
import az.turing.semmed.service.impl.BookingServiceImpl;
import az.turing.semmed.service.impl.FlightServiceImpl;
import az.turing.semmed.servlet.booking.CancelBookingServlet;
import az.turing.semmed.servlet.booking.CreateBookingServlet;
import az.turing.semmed.servlet.booking.GetAllBookingServlet;
import az.turing.semmed.servlet.booking.GetAllByPassengerBookingServlet;
import az.turing.semmed.servlet.booking.GetByIdBookingServlet;
import az.turing.semmed.servlet.flight.GetAllFlightServlet;
import az.turing.semmed.servlet.flight.GetByIdFlightServlet;
import az.turing.semmed.servlet.flight.SearchFlightServlet;
import az.turing.semmed.util.DependencyInjector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class App {

    public static void main(String[] args) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        final FlightDao flightDao = new FlightDatabaseDao();
        final FlightMapper flightMapper = new FlightMapper();
        final FlightService flightService = new FlightServiceImpl(flightDao, flightMapper);
        final FlightController flightController = new FlightController(flightService);

        final BookingDao bookingDao = new BookingDatabaseDao();
        final BookingMapper bookingMapper = new BookingMapper();
        final BookingService bookingService = new BookingServiceImpl(bookingDao, flightDao, bookingMapper);
        final BookingController bookingController = new BookingController(bookingService);

        DependencyInjector.setFlightController(flightController);
        DependencyInjector.setBookingController(bookingController);
        DependencyInjector.setObjectMapper(objectMapper);

        Tomcat server = new Tomcat();
        Connector connector = server.getConnector();
        connector.setPort(8081);

        Context context = server.addContext("", null);

        server.start();
    }
}
