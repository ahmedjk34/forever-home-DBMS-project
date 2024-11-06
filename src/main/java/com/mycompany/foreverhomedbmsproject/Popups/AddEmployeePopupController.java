package com.mycompany.foreverhomedbmsproject.Popups;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.scene.control.PasswordField;

public class AddEmployeePopupController implements Initializable {

    // FXML injected fields
    @FXML
    private TextField idTextField;
    @FXML
    private TextField fnameTextField;
    @FXML
    private TextField lnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField hireDateTextField;
    @FXML
    private TextField expertiseTextField;
    @FXML
    private TextField dobTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private ComboBox<String> socialStatusComboBox;
    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField roleTextField;
    @FXML
    private TextField salaryTextField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button addButton;

    // Initialize method for setting default values and ComboBox items
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populating ComboBox for Social Status
        ObservableList<String> socialStatusItems = FXCollections.observableArrayList("Single", "Divorced", "Widowed", "Married");
        socialStatusComboBox.setItems(socialStatusItems);

        // Populating ComboBox for Gender
        ObservableList<String> genderItems = FXCollections.observableArrayList("Male", "Female", "Other");
        genderComboBox.setItems(genderItems);
    }


// Updated handleAddButtonClick method
    @FXML
    private void handleAddButtonClick(ActionEvent event) {
        // Retrieve values from the text fields, including the new fields
        String id = idTextField.getText();
        String firstName = fnameTextField.getText();
        String lastName = lnameTextField.getText();
        String email = emailTextField.getText();
        String phone = phoneNumberTextField.getText();
        String hireDate = hireDateTextField.getText();
        String expertise = expertiseTextField.getText();
        String dob = dobTextField.getText();
        String address = addressTextField.getText();
        String socialStatus = socialStatusComboBox.getValue();
        String gender = genderComboBox.getValue();
        String role = roleTextField.getText();
        String salaryText = salaryTextField.getText();
        String password = passwordField.getText();

        // Additional validations for new fields
        if (role.isEmpty() || salaryText.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields must be filled out.", AlertType.ERROR);
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryText);
            if (salary < 0) {
                showAlert("Error", "Salary must be a positive value.", AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid salary format.", AlertType.ERROR);
            return;
        }

        if (!isValidDate(dob) || !isValidDate(hireDate)) {
            showAlert("Error", "Please enter valid date formats (yyyy-mm-dd).", AlertType.ERROR);
            return;
        }

        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String DBpassword = "ahm@212005";

        String personQuery = "INSERT INTO Person (SSN, Password, FName, LName, Address, Social_Status, Email, Phone_Number, Date_of_Birth, Gender) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String staffQuery = "INSERT INTO Staff (SSN, Hire_Date, Expertise, Role, Salary) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, DBpassword)) {
            try (PreparedStatement psPerson = conn.prepareStatement(personQuery)) {
                psPerson.setString(1, id);
                psPerson.setString(2, password);
                psPerson.setString(3, firstName);
                psPerson.setString(4, lastName);
                psPerson.setString(5, address);
                psPerson.setString(6, socialStatus);
                psPerson.setString(7, email);
                psPerson.setString(8, phone);
                psPerson.setDate(9, java.sql.Date.valueOf(dob));
                psPerson.setString(10, gender);
                psPerson.executeUpdate();
            }

            try (PreparedStatement psStaff = conn.prepareStatement(staffQuery)) {
                psStaff.setString(1, id);
                psStaff.setDate(2, java.sql.Date.valueOf(hireDate));
                psStaff.setString(3, expertise);
                psStaff.setString(4, role);
                psStaff.setDouble(5, salary);
                psStaff.executeUpdate();
            }

            showAlert("Success", "Employee added successfully.", AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Error", "Database error: " + e.getMessage(), AlertType.ERROR);
        }
    }

    // Helper method to validate date format
    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Helper method to show alert messages
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
