package com.mycompany.foreverhomedbmsproject.Popups;

import com.mycompany.foreverhomedbmsproject.Server.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class EditEmployeePopupController implements Initializable {

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
    private Button editButton;

    private Staff currentEmployee;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> socialStatusItems = FXCollections.observableArrayList("Single", "Divorced", "Widowed", "Married");
        socialStatusComboBox.setItems(socialStatusItems);

        ObservableList<String> genderItems = FXCollections.observableArrayList("Male", "Female", "Other");
        genderComboBox.setItems(genderItems);
    }

    // Method to set current employee details
    public void setEmployee(Staff employee) {
        this.currentEmployee = employee;
        // Set TextField values
        idTextField.setText(employee.getSsn());
        fnameTextField.setText(employee.getFName());
        lnameTextField.setText(employee.getLName());
        emailTextField.setText(employee.getEmail());
        phoneNumberTextField.setText(employee.getPhoneNumber());

        // Convert LocalDate fields to string format (yyyy-MM-dd) for TextFields
        hireDateTextField.setText(employee.getHireDate() != null ? employee.getHireDate().toString() : "");
        dobTextField.setText(employee.getDateOfBirth() != null ? employee.getDateOfBirth().toString() : "");

        expertiseTextField.setText(employee.getExpertise());
        addressTextField.setText(employee.getAddress());
        System.out.println(employee.getAddress());
        socialStatusComboBox.setValue(employee.getSocialStatus());
        genderComboBox.setValue(employee.getGender());
        roleTextField.setText(employee.getRole());
        salaryTextField.setText(String.valueOf(employee.getSalary()));
        passwordField.setText(employee.getPassword());
    }

    @FXML
    private void handleEditButtonClick() {
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

        String updatePersonQuery = "UPDATE Person SET Password=?, FName=?, LName=?, Address=?, Social_Status=?, Email=?, Phone_Number=?, Date_of_Birth=?, Gender=? WHERE SSN=?";
        String updateStaffQuery = "UPDATE Staff SET Hire_Date=?, Expertise=?, Role=?, Salary=? WHERE SSN=?";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, DBpassword)) {
            try (PreparedStatement psPerson = conn.prepareStatement(updatePersonQuery)) {
                psPerson.setString(1, password);
                psPerson.setString(2, firstName);
                psPerson.setString(3, lastName);
                psPerson.setString(4, address);
                psPerson.setString(5, socialStatus);
                psPerson.setString(6, email);
                psPerson.setString(7, phone);
                psPerson.setDate(8, java.sql.Date.valueOf(dob));
                psPerson.setString(9, gender);
                psPerson.setString(10, id);
                psPerson.executeUpdate();
            }

            try (PreparedStatement psStaff = conn.prepareStatement(updateStaffQuery)) {
                psStaff.setDate(1, java.sql.Date.valueOf(hireDate));
                psStaff.setString(2, expertise);
                psStaff.setString(3, role);
                psStaff.setDouble(4, salary);
                psStaff.setString(5, id);
                psStaff.executeUpdate();
            }

            showAlert("Success", "Employee updated successfully.", AlertType.INFORMATION);
            ((javafx.stage.Stage) idTextField.getScene().getWindow()).close();


        } catch (Exception e) {
            showAlert("Error", "Database error: " + e.getMessage(), AlertType.ERROR);
        }
    }

    // Helper methods
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

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
