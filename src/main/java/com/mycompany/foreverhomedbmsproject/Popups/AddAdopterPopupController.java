package com.mycompany.foreverhomedbmsproject.Popups;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;

public class AddAdopterPopupController {

    @FXML
    private TextField idTextField;
    @FXML
    private TextField fnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField lnameTextField;
    @FXML
    private TextField OccupationTextField;
    @FXML
    private TextField NoPetsTextField;
    @FXML
    private TextField dobTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private ComboBox<String> socialStatusComboBox;
    @FXML
    private TextField NoChildrenTextField;
    @FXML
    private TextField YearlyIncomeTextField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private PasswordField passwordField;

    // Initializes the ComboBoxes with predefined options
    public void initialize() {
        // Sample ComboBox values, replace with your actual options
        socialStatusComboBox.getItems().addAll("Single", "Married", "Divorced", "Widowed");
        genderComboBox.getItems().addAll("Male", "Female", "Other");
    }

    @FXML
    private void addAdopter() {
        // Retrieve and sanitize data from input fields
        String adopterID = idTextField.getText().trim();
        String firstName = fnameTextField.getText().trim();
        String lastName = lnameTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String phoneNumber = phoneNumberTextField.getText().trim();
        String occupation = OccupationTextField.getText().trim();
        String noPetsStr = NoPetsTextField.getText().trim();
        String dobStr = dobTextField.getText().trim();
        String address = addressTextField.getText().trim();
        String socialStatus = socialStatusComboBox.getValue();
        String noChildrenStr = NoChildrenTextField.getText().trim();
        String yearlyIncomeStr = YearlyIncomeTextField.getText().trim();
        String gender = genderComboBox.getValue();
        String password = passwordField.getText().trim();

        // Validate required fields
        if (adopterID.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                || phoneNumber.isEmpty() || dobStr.isEmpty() || address.isEmpty()
                || socialStatus == null || noPetsStr.isEmpty() || noChildrenStr.isEmpty()
                || yearlyIncomeStr.isEmpty() || gender == null || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all required fields.");
            return;
        }


        // Validate date format and convert to LocalDate
        LocalDate dob;
        try {
            dob = LocalDate.parse(dobStr);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid date of birth (YYYY-MM-DD).");
            return;
        }

        // Validate numeric fields
        int noPets, noChildren;
        double yearlyIncome;
        try {
            noPets = Integer.parseInt(noPetsStr);
            noChildren = Integer.parseInt(noChildrenStr);
            yearlyIncome = Double.parseDouble(yearlyIncomeStr);
            if (noPets < 0 || noChildren < 0 || yearlyIncome < 0) {
                throw new NumberFormatException("Negative values are not allowed.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter valid non-negative numbers for pets, children, and income.");
            return;
        }

        // Prepare SQL queries
        String personQuery = "INSERT INTO Person (SSN, Password, FName, LName, Address, Social_Status, Email, Phone_Number, Date_of_Birth, Gender) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String adopterQuery = "INSERT INTO Adopter (SSN, Occupation, Number_of_Pets_Owned, Number_of_Children, Yearly_Income) "
                + "VALUES (?, ?, ?, ?, ?)";

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String dbPassword = "ahm@212005";

        try (Connection conn = DriverManager.getConnection(url, user, dbPassword)) {
            // Start a transaction
            conn.setAutoCommit(false);

            // Insert into Person table
            try (PreparedStatement psPerson = conn.prepareStatement(personQuery)) {
                psPerson.setString(1, adopterID);
                psPerson.setString(2, password);
                psPerson.setString(3, firstName);
                psPerson.setString(4, lastName);
                psPerson.setString(5, address);
                psPerson.setString(6, socialStatus);
                psPerson.setString(7, email);
                psPerson.setString(8, phoneNumber);
                psPerson.setDate(9, java.sql.Date.valueOf(dob));  // assuming dob is in 'yyyy-mm-dd' format
                psPerson.setString(10, gender);
                psPerson.executeUpdate();
            }

            // Insert into Adopter table
            try (PreparedStatement psAdopter = conn.prepareStatement(adopterQuery)) {
                psAdopter.setString(1, adopterID);
                psAdopter.setString(2, occupation);
                psAdopter.setInt(3, noPets);  // assuming noPets is a number
                psAdopter.setInt(4, noChildren);  // assuming noChildren is a number
                psAdopter.setBigDecimal(5, new java.math.BigDecimal(yearlyIncome));  // assuming yearlyIncome is a decimal
                psAdopter.executeUpdate();
            }

            // Commit the transaction
            conn.commit();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Adopter added successfully!");

            // Close the current window
            ((javafx.stage.Stage) idTextField.getScene().getWindow()).close();

        } catch (Exception e) {
            // Rollback if there is an error
            try (Connection conn = DriverManager.getConnection(url, user, dbPassword)) {
                conn.rollback();
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to rollback transaction: " + ex.getMessage());
            }
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add adopter: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
