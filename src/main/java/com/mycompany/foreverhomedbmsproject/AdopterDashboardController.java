/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AdopterDashboardController implements Initializable {

    private Adopter adopter;

    @FXML
    private Label ssnLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label socialStatusLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label occupationLabel;
    @FXML
    private Label numberOfPetsLabel;
    @FXML
    private Label numberOfChildrenLabel;
    @FXML
    private Label yearlyIncomeLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Label ageLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Leave empty if you plan to populate fields after setting adopter
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
}
