package com.hotel.model;

/** One hotel room reservation. */
public class Reservation {
    private int id;
    private String guestName;
    private String roomType;
    private double nightlyRate;
    private String payment;

    public Reservation(String guestName, String roomType, double nightlyRate, String payment) {
        this(0, guestName, roomType, nightlyRate, payment);
    }

    public Reservation(int id, String guestName, String roomType, double nightlyRate, String payment) {
        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nightlyRate = nightlyRate;
        this.payment = payment;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public double getNightlyRate() { return nightlyRate; }
    public void setNightlyRate(double nightlyRate) { this.nightlyRate = nightlyRate; }

    public String getPayment() { return payment; }
    public void setPayment(String payment) { this.payment = payment; }
}
