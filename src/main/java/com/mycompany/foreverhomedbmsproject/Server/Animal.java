package com.mycompany.foreverhomedbmsproject.Server;

public class Animal {
    private int animalId;
    private String name;
    private String age;
    private String gender;
    private String breed;
    private String size;
    private String adoptionStatus; // Changed from adoption_status to adoptionStatus

    // Constructor
    public Animal(int animalId, String name, String age, String gender, String breed, String size, String adoptionStatus) {
        this.animalId = animalId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
        this.size = size;
        this.adoptionStatus = adoptionStatus; // Match constructor parameter with field
    }

    // Getters and Setters
    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAdoptionStatus() { // Ensure this getter matches
        return adoptionStatus; // Reflect the updated field name
    }

    public void setAdoptionStatus(String adoptionStatus) { // Ensure this setter matches
        this.adoptionStatus = adoptionStatus; // Reflect the updated field name
    }

    @Override
    public String toString() {
        return "Animal{" +
               "animalId=" + animalId +
               ", name='" + name + '\'' +
               ", age='" + age + '\'' +
               ", gender='" + gender + '\'' +
               ", breed='" + breed + '\'' +
               ", size='" + size + '\'' +
               ", adoptionStatus='" + adoptionStatus + '\'' + // Update here too
               '}';
    }
}
