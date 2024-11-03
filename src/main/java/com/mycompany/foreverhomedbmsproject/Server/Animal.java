package com.mycompany.foreverhomedbmsproject.Server;

public class Animal {
    private int animalId;
    private String name;
    private String age;
    private String gender;
    private String breed;
    private String size;
    private String adoptionStatus;
    private String behaviorDescription;
    private String animalImage;
    private String colors; // Comma-separated list of colors
    private String dob; // Date of Birth

    // Constructor
    public Animal(int animalId, String name, String age, String gender, String breed, String size, String adoptionStatus, String behaviorDescription, String animalImage, String colors, String dob) {
        this.animalId = animalId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
        this.size = size;
        this.adoptionStatus = adoptionStatus;
        this.behaviorDescription = behaviorDescription;
        this.animalImage = animalImage;
        this.colors = colors;
        this.dob = dob; // Initialize dob
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

    public String getAdoptionStatus() {
        return adoptionStatus;
    }

    public void setAdoptionStatus(String adoptionStatus) {
        this.adoptionStatus = adoptionStatus;
    }

    public String getBehaviorDescription() {
        return behaviorDescription;
    }

    public void setBehaviorDescription(String behaviorDescription) {
        this.behaviorDescription = behaviorDescription;
    }

    public String getAnimalImage() {
        return animalImage;
    }

    public void setAnimalImage(String animalImage) {
        this.animalImage = animalImage;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getDob() {
        return dob; // Getter for dob
    }

    public void setDob(String dob) {
        this.dob = dob; // Setter for dob
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
               ", adoptionStatus='" + adoptionStatus + '\'' +
               ", behaviorDescription='" + behaviorDescription + '\'' +
               ", animalImage='" + animalImage + '\'' +
               ", colors='" + colors + '\'' +
               ", dob='" + dob + '\'' + // Include dob in toString
               '}';
    }
}
