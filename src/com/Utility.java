package com;
import java.util.Scanner;
import java.util.UUID;

public class Utility {
    private static Scanner scanner = new Scanner(System.in);

    public static String generateReservationId() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }

    public static String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
