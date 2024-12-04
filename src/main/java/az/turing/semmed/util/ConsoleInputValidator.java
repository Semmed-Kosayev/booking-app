package az.turing.semmed.util;

import java.time.Year;
import java.util.Scanner;

public class ConsoleInputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    public static boolean isAlphabeticOnly(String input) {
        return input != null && input.matches("[a-zA-Z]+");
    }

    public static String getValidDestination() {
        while (true) {
            System.out.print("Please enter destination: ");
            String destination = scanner.nextLine().trim();
            if (isAlphabeticOnly(destination)) {
                return destination;
            } else {
                System.err.printf(
                        "%s\n%s",
                        "Destination must only contain alphabetic characters.",
                        "Please try again."
                );
            }
        }
    }

    public static String getValidPassengerName(int passengerTurn) {
        while (true) {
            System.out.printf("Please enter passenger %d name: ", passengerTurn);
            String passengerName = scanner.nextLine().trim();
            if (isAlphabeticOnly(passengerName)) {
                return passengerName;
            } else {
                System.err.printf(
                        "%s\n%s",
                        "Name must only contain alphabetic characters.",
                        "Please try again."
                );
            }
        }
    }

    public static int getValidDay() {
        while (true) {
            System.out.print("Day (1-31): ");
            int day = Integer.parseInt(scanner.nextLine().trim());
            if (day >= 1 && day <= 31) {
                return day;
            } else {
                System.err.printf(
                        "%s\n%s",
                        "Day must be between 1 and 31.",
                        "Please try again."
                );
            }
        }
    }

    public static int getValidMonth() {
        while (true) {
            System.out.print("Month (1-12): ");
            int month = Integer.parseInt(scanner.nextLine().trim());
            if (month < 1 || month > 12) {
                return month;
            } else {
                System.err.printf(
                        "%s\n%s",
                        "Month must be between 1 and 12.",
                        "Please try again."
                );
            }
        }
    }

    public static int getValidYear() {
        while (true) {
            System.out.print("Year (YYYY): ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            int currentYear = Year.now().getValue();
            if (year >= currentYear) {
                return year;
            } else {
                System.err.printf(
                        "%s\n%s",
                        "Year must not be in past.",
                        "Please try again."
                );
            }
        }
    }

    public static String getValidPassengerSurname(int passengerTurn) {
        while (true) {
            System.out.printf("Please enter passenger %d surname: ", passengerTurn);
            String passengerSurname = scanner.nextLine().trim();
            if (isAlphabeticOnly(passengerSurname)) {
                return passengerSurname;
            } else {
                System.err.printf(
                        "%s\n%s",
                        "Surname must only contain alphabetic characters.",
                        "Please try again."
                );
            }
        }
    }

    public static int getValidTicketCount() {
        while (true) {
            System.out.print("Please enter number of people (how many tickets): ");
            int ticketCount = Integer.parseInt(scanner.nextLine().trim());
            if (ticketCount > 0) {
                return ticketCount;
            } else {
                System.err.printf(
                        "%s\n%s",
                        "Ticket count must be positive.",
                        "Please try again."
                );
            }
        }
    }
}
