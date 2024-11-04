package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;

public class MainInfoAdopterController implements Initializable {

    private Adopter adopter;

    @FXML
    private TextField ssnTextField, fullNameTextField, genderTextField, phoneNumberTextField, emailTextField, socialStatusTextField, addressTextField, occupationTextField, numberOfPetsTextField, numberOfChildrenTextField, yearlyIncomeTextField, dobTextField, ageTextField;

    @FXML
    private Button editInfoButton;

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

    @FXML
    private void editInformation() {
        if (editInfoButton.getText().equals("Edit Information")) {
            toggleFieldsEditable(true);
            editInfoButton.setText("Save Information");
        } else {
            try {
                String ssn = ssnTextField.getText();
                String fullName = fullNameTextField.getText();
                String[] nameParts = fullName.split(" ", 2);
                if (nameParts.length < 2) {
                    showAlert("Invalid Input", "Full Name must contain at least a first name and a last name.");
                    return;
                }
                String firstName = nameParts[0];
                String lastName = nameParts[1];

                String address = addressTextField.getText();
                String socialStatus = socialStatusTextField.getText();
                String email = emailTextField.getText();
                String phoneNumber = phoneNumberTextField.getText();
                String dob = dobTextField.getText();
                String gender = genderTextField.getText();
                String occupation = occupationTextField.getText();
                int numberOfPets = Integer.parseInt(numberOfPetsTextField.getText());
                int numberOfChildren = Integer.parseInt(numberOfChildrenTextField.getText());

                // Remove any dollar sign or commas from the input before parsing
                String yearlyIncomeStr = yearlyIncomeTextField.getText().replaceAll("[$,]", "");
                double yearlyIncome = Double.parseDouble(yearlyIncomeStr);

                // Validate the date of birth
                if (!validateDate(dob)) {
                    return; // If the date is invalid, stop further processing
                }

                // Format socialStatus and gender for database storage
                socialStatus = capitalizeFirstLetter(socialStatus);
                gender = capitalizeFirstLetter(gender);

                if (!validateInput(socialStatus, gender, numberOfPets, numberOfChildren, yearlyIncome)) {
                    return;
                }

                String url = "jdbc:postgresql://localhost:5432/postgres";
                String dbUser = "postgres";
                String dbPassword = "ahm@212005";

                String queryPerson = "UPDATE Person SET FName = ?, LName = ?, Address = ?, Social_Status = ?, "
                        + "Email = ?, Phone_Number = ?, Date_of_Birth = ?, Gender = ? WHERE SSN = ?";
                String queryAdopter = "UPDATE Adopter SET Occupation = ?, Number_of_Pets_Owned = ?, "
                        + "Number_of_Children = ?, Yearly_Income = ? WHERE SSN = ?";

                try (Connection con = DriverManager.getConnection(url, dbUser, dbPassword)) {
                    try (PreparedStatement psPerson = con.prepareStatement(queryPerson)) {
                        psPerson.setString(1, firstName);
                        psPerson.setString(2, lastName);
                        psPerson.setString(3, address);
                        psPerson.setString(4, socialStatus);
                        psPerson.setString(5, email);
                        psPerson.setString(6, phoneNumber);
                        psPerson.setDate(7, Date.valueOf(dob));
                        psPerson.setString(8, gender);
                        psPerson.setString(9, ssn);
                        psPerson.executeUpdate();
                    }

                    try (PreparedStatement psAdopter = con.prepareStatement(queryAdopter)) {
                        psAdopter.setString(1, occupation);
                        psAdopter.setInt(2, numberOfPets);
                        psAdopter.setInt(3, numberOfChildren);
                        psAdopter.setDouble(4, yearlyIncome);
                        psAdopter.setString(5, ssn);
                        psAdopter.executeUpdate();
                    }

                    // Calculate age from dob
                    LocalDate dateOfBirth = LocalDate.parse(dob);
                    LocalDate currentDate = LocalDate.now();
                    int age = Period.between(dateOfBirth, currentDate).getYears();
                    ageTextField.setText(String.valueOf(age)); // Set age in the ageTextField

                    showAlert("Success", "Information updated successfully.");
                    toggleFieldsEditable(false);
                    editInfoButton.setText("Edit Information");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number for yearly income.");
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to save information: " + e.getMessage());
            }
        }
    }

    private boolean validateDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        try {
            LocalDate parsedDate = LocalDate.parse(date, formatter);

            // Check if the year is in the valid range
            int year = parsedDate.getYear();
            if (year < 1920) {
                showAlert("Invalid Input", "Year of Birth must not be before 1920 to be an eligible adopter.");
                return false;
            }
            if (year > 2004) {
                showAlert("Invalid Input", "Year of Birth must not be after 2004.");
                return false;
            }

            // Check if parsedDate matches the input date string, which validates logical date accuracy
            String formattedDate = parsedDate.format(formatter);
            if (!formattedDate.equals(date)) {
                showAlert("Invalid Input", "Date must represent a valid calendar date.");
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            showAlert("Invalid Input", "Date must be in format YYYY-MM-DD and represent a valid date.");
            return false;
        }
    }

    private boolean validateInput(String socialStatus, String gender, int numberOfPets, int numberOfChildren, double yearlyIncome) {
        if (!socialStatus.equals("Single") && !socialStatus.equals("Divorced")
                && !socialStatus.equals("Widowed") && !socialStatus.equals("Married")) {
            showAlert("Invalid Input", "Social Status must be one of: Single, Divorced, Widowed, Married.");
            return false;
        }
        if (!gender.equals("Male") && !gender.equals("Female")) {
            showAlert("Invalid Input", "Gender must be one of: Male, Female.");
            return false;
        }
        if (numberOfPets < 0) {
            showAlert("Invalid Input", "Number of Pets Owned must be 0 or greater.");
            return false;
        }
        if (numberOfChildren < 0) {
            showAlert("Invalid Input", "Number of Children must be 0 or greater.");
            return false;
        }
        if (yearlyIncome < 0) {
            showAlert("Invalid Input", "Yearly Income must be a positive number.");
            return false;
        }
        return true;
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void toggleFieldsEditable(boolean enable) {
        fullNameTextField.setDisable(!enable);
        fullNameTextField.setEditable(enable);

        genderTextField.setDisable(!enable);
        genderTextField.setEditable(enable);

        phoneNumberTextField.setDisable(!enable);
        phoneNumberTextField.setEditable(enable);

        emailTextField.setDisable(!enable);
        emailTextField.setEditable(enable);

        socialStatusTextField.setDisable(!enable);
        socialStatusTextField.setEditable(enable);

        addressTextField.setDisable(!enable);
        addressTextField.setEditable(enable);

        occupationTextField.setDisable(!enable);
        occupationTextField.setEditable(enable);

        numberOfPetsTextField.setDisable(!enable);
        numberOfPetsTextField.setEditable(enable);

        numberOfChildrenTextField.setDisable(!enable);
        numberOfChildrenTextField.setEditable(enable);

        yearlyIncomeTextField.setDisable(!enable);
        yearlyIncomeTextField.setEditable(enable);

        dobTextField.setDisable(!enable);
        dobTextField.setEditable(enable);

        ageTextField.setDisable(true); // Age field remains uneditable since it's calculated from DOB
    }

    private void showAlert(String title, String message) {
        int messageType = title.equalsIgnoreCase("Success") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }
}
