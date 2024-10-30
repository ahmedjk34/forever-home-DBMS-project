/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject.Server;

import java.time.LocalDate;

public class Adopter extends Person {

    private String occupation;
    private int numberOfPetsOwned;
    private int numberOfChildren;
    private double yearlyIncome;

    // Constructor
    public Adopter(String ssn, String password, String gender,  String fName, String lName, String address,
                   String socialStatus, String email, String phoneNumber, LocalDate dateOfBirth,
                   String occupation, int numberOfPetsOwned, int numberOfChildren, double yearlyIncome) {
        super(ssn, password, gender , fName, lName, address, socialStatus, email, phoneNumber, dateOfBirth);
        this.occupation = occupation;
        this.numberOfPetsOwned = numberOfPetsOwned;
        this.numberOfChildren = numberOfChildren;
        this.yearlyIncome = yearlyIncome;
    }

    // Getters and Setters
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getNumberOfPetsOwned() {
        return numberOfPetsOwned;
    }

    public void setNumberOfPetsOwned(int numberOfPetsOwned) {
        this.numberOfPetsOwned = numberOfPetsOwned;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public double getYearlyIncome() {
        return yearlyIncome;
    }

    public void setYearlyIncome(double yearlyIncome) {
        this.yearlyIncome = yearlyIncome;
    }

    // toString() Method
    @Override
    public String toString() {
        return "Adopter{" +
                "occupation='" + occupation + '\'' +
                ", numberOfPetsOwned=" + numberOfPetsOwned +
                ", numberOfChildren=" + numberOfChildren +
                ", yearlyIncome=" + yearlyIncome +
                ", fullName='" + getFullName() + '\'' +
                "} " + super.toString();
    }
}
