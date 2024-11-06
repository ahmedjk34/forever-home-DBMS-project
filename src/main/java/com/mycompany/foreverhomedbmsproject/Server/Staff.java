/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject.Server;

import java.time.LocalDate;

public class Staff extends Person {

    private LocalDate hireDate;
    private String expertise;
    private String role;
    private double salary;

    // Constructor to match the query
    public Staff(String ssn, String password, String gender, String fName, String lName, String address,
            String socialStatus, String email, String phoneNumber, LocalDate dateOfBirth,
            LocalDate hireDate, String expertise, String role, double salary) {
        super(ssn, password, gender, fName, lName, address, socialStatus, email, phoneNumber, dateOfBirth);
        this.hireDate = hireDate;
        this.expertise = expertise;
        this.role = role;
        this.salary = salary;
    }

    // Getters and Setters
    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    // toString() Method to provide a readable string for the Staff object
    @Override
    public String toString() {
        return "Staff{"
                + "hireDate=" + hireDate
                + ", expertise='" + expertise + '\''
                + ", role='" + role + '\''
                + ", salary=" + salary
                + ", fullName='" + getFullName() + '\''
                + "} " + super.toString();
    }
}
