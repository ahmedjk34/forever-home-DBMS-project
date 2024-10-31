/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class AdopterDashboardController implements Initializable {

    private Adopter adopter;
    
    @FXML
    private AnchorPane contentPane ; // Reference to the inner AnchorPane for content

    @FXML
    private Label ssnLabel, fullNameLabel, genderLabel, phoneNumberLabel, emailLabel, socialStatusLabel, addressLabel, occupationLabel, numberOfPetsLabel, numberOfChildrenLabel, yearlyIncomeLabel, dobLabel, ageLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code here
    }    

    public void setAdopter(Adopter adopter) {
        this.adopter = adopter;
        updateFields();
        
    }

    private void updateFields() {
        ssnLabel.setText(adopter.getSsn());
        fullNameLabel.setText(adopter.getFullName());
        genderLabel.setText(adopter.getGender());
        phoneNumberLabel.setText(adopter.getPhoneNumber());
        emailLabel.setText(adopter.getEmail());
        socialStatusLabel.setText(adopter.getSocialStatus());
        addressLabel.setText(adopter.getAddress());
        occupationLabel.setText(adopter.getOccupation());
        numberOfPetsLabel.setText(String.valueOf(adopter.getNumberOfPetsOwned()));
        numberOfChildrenLabel.setText(String.valueOf(adopter.getNumberOfChildren()));
        yearlyIncomeLabel.setText(String.format("$%.2f", adopter.getYearlyIncome()));
        dobLabel.setText(adopter.getDateOfBirth().toString());
        ageLabel.setText(String.valueOf(adopter.getAge())); 
    }

    // Button action methods
    @FXML
    private void handleApplicationsAction() {
        // Code to handle Applications button
        System.out.println("Applications button clicked");
    }

    @FXML
private void handleAnimalsAction() throws IOException {
    // Load the new content
    FXMLLoader loader = new FXMLLoader(getClass().getResource("AnimalExplorer.fxml"));
    AnchorPane animalPane = loader.load();
    
    // Clear the existing content and set the new one
    contentPane.getChildren().setAll(animalPane);
}

    @FXML
    private void handleContactUsAction() {
        // Code to handle Contact Us button
        System.out.println("Contact Us button clicked");
    }

    @FXML
    private void handleFeedbackAction() {
        // Code to handle Feedback button
        System.out.println("Feedback button clicked");
    }

    @FXML
    private void handleMedicalRecordsAction() {
        // Code to handle Medical Records button
        System.out.println("Medical Records button clicked");
    }
}
