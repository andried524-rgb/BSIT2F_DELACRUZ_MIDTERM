package com.hotel.ui;

import com.hotel.dao.DaoException;
import com.hotel.dao.ScenarioDao;
import com.hotel.model.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Console menu loop. Talks to the database only through ScenarioDao. */
public class ConsoleUI {
    private static final String LINE = "=".repeat(52);

    private final ScenarioDao dao;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(ScenarioDao dao) {
        this.dao = dao;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Enter choice: ");
            switch (choice) {
                case "1" -> addReservation();
                case "2" -> viewReservations();
                case "3" -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nERROR: Invalid choice. Please enter 1, 2, or 3.\n");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println(LINE);
        System.out.println("          HOTEL ROOM RESERVATION SYSTEM");
        System.out.println(LINE);
        System.out.println("[1] Add New Reservation");
        System.out.println("[2] View All Reservations");
        System.out.println("[3] Exit System");
        System.out.println(LINE);
    }

    private void addReservation() {
        System.out.println("\n--- ADD NEW RESERVATION ---");
        String guest = readRequired("Enter Guest Name: ");
        String roomType = readOption("Enter Room Type (SINGLE / DOUBLE / SUITE): ",
                "SINGLE", "DOUBLE", "SUITE");
        double rate = readRate("Enter Nightly Rate: ");
        String payment = readOption("Enter Payment Status (PAID / PENDING): ", "PAID", "PENDING");

        try {
            dao.insert(new Reservation(guest, roomType, rate, payment));
            System.out.println("\nSUCCESS: Reservation recorded in database!\n");
        } catch (DaoException e) {
            System.out.println("\nERROR: " + e.getMessage() + "\n");
        }
    }

    private void viewReservations() {
        List<Reservation> list;
        try {
            list = dao.findAll();
        } catch (DaoException e) {
            System.out.println("\nERROR: " + e.getMessage() + "\n");
            return;
        }

        if (list.isEmpty()) {
            System.out.println("\nNo records found.\n");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (Reservation r : list) {
            rows.add(new String[] {String.valueOf(r.getId()), r.getGuestName(), r.getRoomType(),
                    String.format(Locale.US, "%.2f", r.getNightlyRate()), r.getPayment()});
        }
        printTable("ALL ROOM RESERVATIONS",
                new String[] {"ID", "GUEST NAME", "ROOM TYPE", "NIGHTLY RATE", "PAYMENT"},
                new int[] {2, 19, 10, 12, 14}, rows);
    }

    /** Prints an ASCII table whose columns grow to fit the data. */
    private void printTable(String title, String[] headers, int[] minWidths, List<String[]> rows) {
        int[] w = minWidths.clone();
        for (int i = 0; i < headers.length; i++) {
            w[i] = Math.max(w[i], headers[i].length());
            for (String[] r : rows) {
                w[i] = Math.max(w[i], r[i].length());
            }
        }

        StringBuilder border = new StringBuilder("+");
        StringBuilder format = new StringBuilder("|");
        for (int width : w) {
            border.append("-".repeat(width + 2)).append("+");
            format.append(" %-").append(width).append("s |");
        }
        format.append("%n");

        int total = border.length();
        String equals = "=".repeat(total);
        int pad = Math.max(0, (total - title.length()) / 2);

        System.out.println();
        System.out.println(equals);
        System.out.println(" ".repeat(pad) + title);
        System.out.println(equals);
        System.out.println(border);
        System.out.printf(format.toString(), (Object[]) headers);
        System.out.println(border);
        for (String[] r : rows) {
            System.out.printf(format.toString(), (Object[]) r);
        }
        System.out.println(border);
        System.out.println("Total Records Displayed: " + rows.size() + "\n");
    }

    /** Reads one full line (avoids the nextInt/nextLine newline problem). */
    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "3"; // input closed: exit cleanly
        }
        return scanner.nextLine().trim();
    }

    private String readRequired(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("ERROR: This field cannot be empty.");
        }
    }

    private String readOption(String prompt, String... options) {
        while (true) {
            String value = readLine(prompt).toUpperCase();
            for (String option : options) {
                if (value.equals(option)) {
                    return value;
                }
            }
            System.out.println("ERROR: Enter one of: " + String.join(", ", options) + ".");
        }
    }

    private double readRate(String prompt) {
        while (true) {
            String raw = readLine(prompt).replace(",", "");
            try {
                double value = Double.parseDouble(raw);
                if (Double.isFinite(value) && value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // fall through to the error message
            }
            System.out.println("ERROR: Enter a valid positive number (e.g., 2500.50).");
        }
    }
}
