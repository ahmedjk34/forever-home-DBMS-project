/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Application;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;

public class ApplicationItemController implements Initializable {

    @FXML
    private ImageView animalImage;
    @FXML
    private Label applicationStatusLabel;
    @FXML
    private Label applicationDateLabel;
    @FXML
    private Label adopterNameLabel;
    @FXML
    private Label adopterSSNLabel;
    @FXML
    private Label adopterOccupationLabel;
    @FXML
    private Label adopterNumOfKidsLabel;
    @FXML
    private Label adopterNumOfPetsLabel;
    @FXML
    private Label adopterYearlyIncomeLabel;
    @FXML
    private TextField animalIDField;
    @FXML
    private TextField animalNameField;
    @FXML
    private TextField animalGenderField;
    @FXML
    private TextField animalAgeField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic if needed
        
    }

    public void setApplicationData(Application application) {
        // Set animal details
        animalIDField.setText(String.valueOf(application.getAnimalId()));
        animalNameField.setText(application.getAnimalName());
        animalGenderField.setText(application.getGender());
        animalAgeField.setText(application.getAge());

        // Set adopter details
        adopterNameLabel.setText(application.getAdopterName());
        adopterSSNLabel.setText(application.getAdopterSSN());
        adopterOccupationLabel.setText(application.getOccupation());
        adopterNumOfKidsLabel.setText(String.valueOf(application.getNumberOfKids()));
        adopterNumOfPetsLabel.setText(String.valueOf(application.getNumberOfPets()));
        adopterYearlyIncomeLabel.setText(String.valueOf(application.getYearlyIncome() + "$"));

        // Set application status and date
        applicationStatusLabel.setText(application.getAdoptionStatus());
        applicationDateLabel.setText(application.getAdoptionDate() != null ? application.getAdoptionDate().toString() : "N/A");

        // Set the animal image
        if (application.getAnimalImage() != null && !application.getAnimalImage().isEmpty()) {
            animalImage.setImage(new Image(application.getAnimalImage()));
        } else {
            // Set a default image if none is provided
            animalImage.setImage(new Image("path/to/default/image.png")); // replace with actual default image path
        }
    }
}
