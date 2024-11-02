/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject.Server;

/**
 *
 * @author lenovo
 */
import java.util.Date;

public class Application {
    private int animalId;
    private String animalName;
    private String age;
    private String gender;
    private String breed;
    private String adopterSSN;
    private String adopterName;
    private String occupation;
    private int adopterAge;
    private int numberOfKids;
    private int numberOfPets;
    private double yearlyIncome;
    private String adoptionStatus;
    private Date adoptionDate;
    private String animalImage;

    // Constructor
    public Application(int animalId, String animalName, String age, String gender, String breed,
                      String adopterSSN, String adopterName, String occupation, int adopterAge,
                      int numberOfKids, int numberOfPets, double yearlyIncome,
                      String adoptionStatus, Date adoptionDate , String animalImage) {
        this.animalId = animalId;
        this.animalName = animalName;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
        this.adopterSSN = adopterSSN;
        this.adopterName = adopterName;
        this.occupation = occupation;
        this.adopterAge = adopterAge;
        this.numberOfKids = numberOfKids;
        this.numberOfPets = numberOfPets;
        this.yearlyIncome = yearlyIncome;
        this.adoptionStatus = adoptionStatus;
        this.adoptionDate = adoptionDate;
        this.animalImage = animalImage;
    }

    // Getters and Setters
    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAdopterSSN() {
        return adopterSSN;
    }

    public void setAdopterSSN(String adopterSSN) {
        this.adopterSSN = adopterSSN;
    }

    public String getAdopterName() {
        return adopterName;
    }

    public void setAdopterName(String adopterName) {
        this.adopterName = adopterName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getAdopterAge() {
        return adopterAge;
    }

    public void setAdopterAge(int adopterAge) {
        this.adopterAge = adopterAge;
    }

    public int getNumberOfKids() {
        return numberOfKids;
    }

    public void setNumberOfKids(int numberOfKids) {
        this.numberOfKids = numberOfKids;
    }

    public int getNumberOfPets() {
        return numberOfPets;
    }

    public void setNumberOfPets(int numberOfPets) {
        this.numberOfPets = numberOfPets;
    }

    public double getYearlyIncome() {
        return yearlyIncome;
    }

    public void setYearlyIncome(double yearlyIncome) {
        this.yearlyIncome = yearlyIncome;
    }

    public String getAdoptionStatus() {
        return adoptionStatus;
    }

    public void setAdoptionStatus(String adoptionStatus) {
        this.adoptionStatus = adoptionStatus;
    }

    public Date getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(Date adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    @Override
    public String toString() {
        return "Application{" +
               "animalId=" + animalId +
               ", animalName='" + animalName + '\'' +
               ", age='" + age + '\'' +
               ", gender='" + gender + '\'' +
               ", breed='" + breed + '\'' +
               ", adopterSSN='" + adopterSSN + '\'' +
               ", adopterName='" + adopterName + '\'' +
               ", occupation='" + occupation + '\'' +
               ", adopterAge=" + adopterAge +
               ", numberOfKids=" + numberOfKids +
               ", numberOfPets=" + numberOfPets +
               ", yearlyIncome=" + yearlyIncome +
               ", adoptionStatus='" + adoptionStatus + '\'' +
               ", adoptionDate=" + adoptionDate +
               ", animalImage =" + animalImage +
               '}';
    }
}
