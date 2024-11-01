package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MainInfoAdopterController implements Initializable {

    private Adopter adopter;

    @FXML
    private Label ssnLabel, fullNameLabel, genderLabel, phoneNumberLabel, emailLabel, socialStatusLabel, addressLabel, occupationLabel, numberOfPetsLabel, numberOfChildrenLabel, yearlyIncomeLabel, dobLabel, ageLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Any initialization if needed
    }   

    public void setAdopter(Adopter adopter) {
        this.adopter = adopter;
        updateFields();
    }

    private void updateFields() {
        if (adopter != null) {
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
}
