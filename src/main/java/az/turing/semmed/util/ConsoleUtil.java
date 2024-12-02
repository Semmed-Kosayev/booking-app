package az.turing.semmed.util;

import az.turing.semmed.controller.BookingController;
import az.turing.semmed.controller.FlightController;
import az.turing.semmed.domain.dao.BookingDao;
import az.turing.semmed.domain.dao.FlightDao;
import az.turing.semmed.domain.dao.impl.BookingDatabaseDao;
import az.turing.semmed.domain.dao.impl.FlightDatabaseDao;
import az.turing.semmed.domain.entity.FlightEntity;
import az.turing.semmed.exception.NotFoundException;
import az.turing.semmed.mapper.BookingMapper;
import az.turing.semmed.mapper.FlightMapper;
import az.turing.semmed.model.dto.BookingDto;
import az.turing.semmed.model.dto.CreateBookingRequest;
import az.turing.semmed.model.dto.FlightDto;
import az.turing.semmed.service.BookingService;
import az.turing.semmed.service.FlightService;
import az.turing.semmed.service.impl.BookingServiceImpl;
import az.turing.semmed.service.impl.FlightServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;


public class ConsoleUtil {

    private final FlightDao flightDao = new FlightDatabaseDao();
    private final FlightMapper flightMapper = new FlightMapper();
    private final FlightService flightService = new FlightServiceImpl(flightDao, flightMapper);
    private final FlightController flightController = new FlightController(flightService);

    private final BookingDao bookingDao = new BookingDatabaseDao();
    private final BookingMapper bookingMapper = new BookingMapper();
    private final BookingService bookingService = new BookingServiceImpl(bookingDao, flightDao, bookingMapper);
    private final BookingController bookingController = new BookingController(bookingService);

    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        insertMockFlighIfDatabaseIsEmpty();

        boolean canLoop = true;
        while (canLoop) {
            displayMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> flightController.getAllFlightsWithin24Hours().forEach(System.out::println);
                    case 2 -> showFlightInformation();
                    case 3 -> searchAndBookFlight();
                    case 4 -> cancelBooking();
                    case 5 -> showMyFlights();
                    case 6 -> canLoop = exitProgram();
                    default -> System.out.println("Wrong choice. Try Again");
                }

            } catch (NotFoundException | IllegalArgumentException e) {
                System.err.println(e.getMessage() + "\nrolling back to menu.");
            } catch (Exception e) {
                System.out.println("Something went wrong. " + e.getMessage() + "\nrolling back to menu.");
            }
        }
    }

    private void insertMockFlighIfDatabaseIsEmpty() {
        if (flightDao.getAll().isEmpty()) {
            flightDao.create(new FlightEntity(
                    "New York",
                    "Kiev",
                    LocalDateTime.now().plusHours(1),
                    40
            ));
        }
    }

    public void displayMenu() {
        System.out.print("""
                \nMake your choice:
                1. Online-Board
                2. Show flight information
                3. Search and book a flight
                4. Cancel the booking
                5. My Flights
                6. Exit
                """);
    }

    public void displayMenuOfFlightSearch() {
        System.out.print("""
                Make your choice:
                0. Return back to main menu
                1. Select one of the flights
                """);
    }

    private boolean isValidNameAndSurname(String name, String surname) {
        return name.matches("[a-zA-Z]+") && surname.matches("[a-zA-Z]+");
    }

    private void showFlightInformation() {
        System.out.print("Please enter flight ID: ");
        long flightId = Long.parseLong(scanner.nextLine().trim());
        System.out.println(flightController.getFlightById(flightId));
    }

    private void searchAndBookFlight() {
        System.out.print("Please enter destination: ");
        String destination = scanner.nextLine().trim();

        System.out.print("Please enter date\n");
        System.out.print("Day (1-31): ");
        int day = Integer.parseInt(scanner.nextLine().trim());
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day must be between 1 and 31");
        }

        System.out.print("Month (1-12): ");
        int month = Integer.parseInt(scanner.nextLine().trim());
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        }

        System.out.print("Year (YYYY): ");
        int year = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Please enter number of people (how many tickets): ");
        int ticketCount = Integer.parseInt(scanner.nextLine().trim());

        List<FlightDto> flights =
                flightController.searchFlights(destination, LocalDate.of(year, month, day), ticketCount);
        if (flights.isEmpty()) {
            throw new NotFoundException("Flights with specified conditions couldn't be found.");
        }
        flights.forEach(System.out::println);
        displayMenuOfFlightSearch();
        int choiceOfFlightSearchMenu = Integer.parseInt(scanner.nextLine().trim());
        switch (choiceOfFlightSearchMenu) {
            case 1 -> {
                System.out.print("Please enter flight ID: ");
                long flightId = Long.parseLong(scanner.nextLine().trim());

                for (int i = 1; i <= ticketCount; i++) {
                    System.out.printf("Please enter passenger %d name: ", i);
                    String passengerName = scanner.nextLine().trim();
                    System.out.printf("Please enter passenger %d surname: ", i);
                    String passengerSurname = scanner.nextLine().trim();
                    if (!isValidNameAndSurname(passengerName, passengerSurname)) {
                        System.out.println("Name and surname must only contain alphabetic characters." +
                                " Please try again.");
                        i--;
                        continue;
                    }

                    bookingController.createBooking(
                            new CreateBookingRequest(flightId, passengerName, passengerSurname)
                    );
                }
            }
            case 0 -> System.out.println("Returning back to main menu...");
        }
    }

    private void cancelBooking() {
        System.out.print("Please enter booking ID: ");
        long bookingId = Long.parseLong(scanner.nextLine().trim());
        bookingController.cancelBooking(bookingId);
    }

    private void showMyFlights() {
        System.out.print("Please enter your full name(name surname): ");
        String fullname = scanner.nextLine().trim();
        List<BookingDto> allBookingsByPassenger = bookingController.findAllBookingsByPassenger(fullname);
        if (allBookingsByPassenger.isEmpty()) {
            System.err.println(fullname + " hasn't booked a flight yet.");
        } else {
            allBookingsByPassenger.forEach(System.out::println);
        }
    }

    private boolean exitProgram() {
        System.out.println("Exiting....");
        return false;
    }
}

