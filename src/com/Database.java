package com;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<User> users;
    private List<Flight> flights;
    private List<Reservation> reservations;
    private static Database instance;

    private Database() {
        users = new ArrayList<>();
        flights = new ArrayList<>();
        reservations = new ArrayList<>();
        initializeFlights();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private void initializeFlights() {
        flights.add(new Flight("AI101", "Delhi", "Mumbai", "10:00", "12:00", 150, 5000));
        flights.add(new Flight("AI102", "Mumbai", "Delhi", "14:00", "16:00", 150, 5000));
        flights.add(new Flight("AI201", "Delhi", "Bangalore", "08:00", "11:00", 200, 7000));
        flights.add(new Flight("AI202", "Bangalore", "Delhi", "17:00", "20:00", 200, 7000));
    }

    // User methods
    public void addUser(User user) { users.add(user); }
    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // Flight methods
    public List<Flight> getAvailableFlights() { return flights; }
    public Flight getFlight(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    // Reservation methods
    public void addReservation(Reservation reservation) { reservations.add(reservation); }
    public List<Reservation> getReservations() { return reservations; }  // Added this method
    public Reservation getReservation(String reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId().equals(reservationId)) {
                return reservation;
            }
        }
        return null;
    }
    public void cancelReservation(Reservation reservation) { reservations.remove(reservation); }
}