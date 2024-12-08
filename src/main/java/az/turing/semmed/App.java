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

        Tomcat server = new Tomcat();
        Connector connector = server.getConnector();
        connector.setPort(8081);

        Context context = server.addContext("", null);

        Tomcat.addServlet(
                context,
                "getAllFlightServlet",
                new GetAllFlightServlet(flightController, objectMapper)
        );
        context.addServletMappingDecoded("/flights", "getAllFlightServlet");

        Tomcat.addServlet(
                context,
                "getByIdFlightServlet",
                new GetByIdFlightServlet(flightController, objectMapper)
        );
        context.addServletMappingDecoded("/flights/*", "getByIdFlightServlet");

        Tomcat.addServlet(
                context,
                "searchFlightServlet",
                new SearchFlightServlet(flightController, objectMapper)
        );
        context.addServletMappingDecoded("/flights/search", "searchFlightServlet");

        Tomcat.addServlet(
                context,
                "getAllBookingServlet",
                new GetAllBookingServlet(bookingController, objectMapper)
        );
        context.addServletMappingDecoded("/bookings", "getAllBookingServlet");

        Tomcat.addServlet(
                context,
                "getByIdBookingServlet",
                new GetByIdBookingServlet(bookingController, objectMapper)
        );
        context.addServletMappingDecoded("/bookings/*", "getByIdBookingServlet");

        Tomcat.addServlet(
                context,
                "getAllByPassengerBookingServlet",
                new GetAllByPassengerBookingServlet(bookingController, objectMapper)
        );
        context.addServletMappingDecoded("/bookings/by-passenger-name/*", "getAllByPassengerBookingServlet");

        Tomcat.addServlet(
                context,
                "createBookingServlet",
                new CreateBookingServlet(bookingController, objectMapper)
        );
        context.addServletMappingDecoded("/bookings/create", "createBookingServlet");

        Tomcat.addServlet(
                context,
                "cancelBookingServlet",
                new CancelBookingServlet(bookingController, objectMapper)
        );
        context.addServletMappingDecoded("/bookings/*", "cancelBookingServlet");

        server.start();
    }
}
