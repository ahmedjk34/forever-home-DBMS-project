package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class MainInfoAdopterController implements Initializable {

    private Adopter adopter;

    @FXML
    private TextField ssnTextField, fullNameTextField, genderTextField, phoneNumberTextField, emailTextField, socialStatusTextField, addressTextField, occupationTextField, numberOfPetsTextField, numberOfChildrenTextField, yearlyIncomeTextField, dobTextField, ageTextField;

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
            ssnTextField.setText(adopter.getSsn());
            fullNameTextField.setText(adopter.getFullName());
            genderTextField.setText(adopter.getGender());
            phoneNumberTextField.setText(adopter.getPhoneNumber());
            emailTextField.setText(adopter.getEmail());
            socialStatusTextField.setText(adopter.getSocialStatus());
            addressTextField.setText(adopter.getAddress());
            occupationTextField.setText(adopter.getOccupation());
            numberOfPetsTextField.setText(String.valueOf(adopter.getNumberOfPetsOwned()));
            numberOfChildrenTextField.setText(String.valueOf(adopter.getNumberOfChildren()));
            yearlyIncomeTextField.setText(String.format("$%.2f", adopter.getYearlyIncome()));
            dobTextField.setText(adopter.getDateOfBirth().toString());
            ageTextField.setText(String.valueOf(adopter.getAge())); 
        }
    }
}
