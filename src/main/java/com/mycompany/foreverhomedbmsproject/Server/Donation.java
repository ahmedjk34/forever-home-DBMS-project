/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject.Server;

import java.time.LocalDate;

public class Donation {

    private int donationId;
    private String donerName;
    private String donationType;
    private String purpose;
    private LocalDate dateOfDonation;
    private double amount;
    private int eventId;

    // Constructor
    public Donation(int donationId, String donerName, String donationType, String purpose,
            LocalDate dateOfDonation, double amount, int eventId) {
        this.donationId = donationId;
        this.donerName = donerName;
        this.donationType = donationType;
        this.purpose = purpose;
        this.dateOfDonation = dateOfDonation;
        this.amount = amount;
        this.eventId = eventId;
    }

    // Getters and Setters
    public int getDonationId() {
        return donationId;
    }

    public void setDonationId(int donationId) {
        this.donationId = donationId;
    }

    public String getDonerName() {
        return donerName;
    }

    public void setDonerName(String donerName) {
        this.donerName = donerName;
    }

    public String getDonationType() {
        return donationType;
    }

    public void setDonationType(String donationType) {
        if (donationType.equals("Cash") || donationType.equals("Check") || donationType.equals("Goods") || donationType.equals("Sponsor")) {
            this.donationType = donationType;
        } else {
            throw new IllegalArgumentException("Invalid donation type");
        }
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        if (purpose.equals("Animal Goods") || purpose.equals("Medical Care") || purpose.equals("Rescue Missions")) {
            this.purpose = purpose;
        } else {
            throw new IllegalArgumentException("Invalid purpose");
        }
    }

    public LocalDate getDateOfDonation() {
        return dateOfDonation;
    }

    public void setDateOfDonation(LocalDate dateOfDonation) {
        this.dateOfDonation = dateOfDonation;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount >= 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    // toString Method for easy printing of Donation objects
    @Override
    public String toString() {
        return "Donation{"
                + "donationId=" + donationId
                + ", donerName='" + donerName + '\''
                + ", donationType='" + donationType + '\''
                + ", purpose='" + purpose + '\''
                + ", dateOfDonation=" + dateOfDonation
                + ", amount=" + amount
                + ", eventId=" + eventId
                + '}';
    }
}
