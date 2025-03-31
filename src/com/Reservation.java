package com;

public class Reservation {
    private String reservationId;
    private User user;
    private Flight flight;
    private String reservationDate;
    private int numberOfPassengers;
    private double totalFare;

    public Reservation(String reservationId, User user, Flight flight, 
                       String reservationDate, int numberOfPassengers) {
        this.reservationId = reservationId;
        this.user = user;
        this.flight = flight;
        this.reservationDate = reservationDate;
        this.numberOfPassengers = numberOfPassengers;
        this.totalFare = flight.getFare() * numberOfPassengers;
    }

    // Getters
    public String getReservationId() { return reservationId; }
    public User getUser() { return user; }
    public Flight getFlight() { return flight; }
    public String getReservationDate() { return reservationDate; }
    public int getNumberOfPassengers() { return numberOfPassengers; }
    public double getTotalFare() { return totalFare; }
}
