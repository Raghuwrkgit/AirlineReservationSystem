package com;

public class Main {
    private static Database db = Database.getInstance();
    private static User currentUser = null;

    public static void main(String[] args) {
        showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n===== Airline Reservation System =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = Utility.getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.out.println("Thank you for using our system. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerUser() {
        System.out.println("\n===== User Registration =====");
        String username = Utility.getInput("Enter username: ");
        
        if (db.getUser(username) != null) {
            System.out.println("Username already exists. Please try another one.");
            return;
        }

        String password = Utility.getInput("Enter password: ");
        String name = Utility.getInput("Enter your full name: ");
        String email = Utility.getInput("Enter your email: ");

        User newUser = new User(username, password, name, email);
        db.addUser(newUser);
        System.out.println("Registration successful! Please login to continue.");
    }

    private static void loginUser() {
        System.out.println("\n===== User Login =====");
        String username = Utility.getInput("Enter username: ");
        String password = Utility.getInput("Enter password: ");

        User user = db.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + user.getName() + "!");
            showUserMenu();
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void showUserMenu() {
        while (currentUser != null) {
            System.out.println("\n===== User Menu =====");
            System.out.println("1. Search Flights");
            System.out.println("2. Make Reservation");
            System.out.println("3. View Reservations");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Logout");
            int choice = Utility.getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    searchFlights();
                    break;
                case 2:
                    makeReservation();
                    break;
                case 3:
                    viewReservations();
                    break;
                case 4:
                    cancelReservation();
                    break;
                case 5:
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void searchFlights() {
        System.out.println("\n===== Available Flights =====");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s\n", 
                          "Flight No", "From", "To", "Departure", "Arrival", "Seats", "Fare");
        
        for (Flight flight : db.getAvailableFlights()) {
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10d %-10.2f\n", 
                             flight.getFlightNumber(), 
                             flight.getOrigin(), 
                             flight.getDestination(),
                             flight.getDepartureTime(), 
                             flight.getArrivalTime(), 
                             flight.getAvailableSeats(), 
                             flight.getFare());
        }
    }

    private static void makeReservation() {
        searchFlights();
        String flightNumber = Utility.getInput("\nEnter flight number to book: ");
        Flight flight = db.getFlight(flightNumber);
        
        if (flight == null) {
            System.out.println("Invalid flight number.");
            return;
        }
        
        if (flight.getAvailableSeats() <= 0) {
            System.out.println("Sorry, no seats available on this flight.");
            return;
        }
        
        int passengers = Utility.getIntInput("Enter number of passengers: ");
        if (passengers <= 0 || passengers > flight.getAvailableSeats()) {
            System.out.println("Invalid number of passengers.");
            return;
        }
        
        // Book the seats
        for (int i = 0; i < passengers; i++) {
            flight.bookSeat();
        }
        
        String reservationId = Utility.generateReservationId();
        String reservationDate = Utility.getCurrentDate();
        
        Reservation reservation = new Reservation(reservationId, currentUser, flight, 
                                               reservationDate, passengers);
        db.addReservation(reservation);
        
        System.out.println("\nReservation successful!");
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Flight: " + flight.getFlightNumber() + " from " + 
                          flight.getOrigin() + " to " + flight.getDestination());
        System.out.println("Total Fare: " + reservation.getTotalFare());
    }

    private static void viewReservations() {
        System.out.println("\n===== Your Reservations =====");
        boolean found = false;
        
        for (Reservation reservation : db.getReservations()) {
            if (reservation.getUser().getUsername().equals(currentUser.getUsername())) {
                found = true;
                System.out.println("Reservation ID: " + reservation.getReservationId());
                System.out.println("Flight: " + reservation.getFlight().getFlightNumber() + 
                                 " from " + reservation.getFlight().getOrigin() + 
                                 " to " + reservation.getFlight().getDestination());
                System.out.println("Date: " + reservation.getReservationDate());
                System.out.println("Passengers: " + reservation.getNumberOfPassengers());
                System.out.println("Total Fare: " + reservation.getTotalFare());
                System.out.println("-------------------------");
            }
        }
        
        if (!found) {
            System.out.println("No reservations found.");
        }
    }

    private static void cancelReservation() {
        viewReservations();
        String reservationId = Utility.getInput("\nEnter reservation ID to cancel: ");
        Reservation reservation = db.getReservation(reservationId);
        
        if (reservation == null || 
            !reservation.getUser().getUsername().equals(currentUser.getUsername())) {
            System.out.println("Invalid reservation ID or not your reservation.");
            return;
        }
        
        // Release the seats
        for (int i = 0; i < reservation.getNumberOfPassengers(); i++) {
            reservation.getFlight().cancelSeat();
        }
        
        db.cancelReservation(reservation);
        System.out.println("Reservation cancelled successfully.");
    }
}