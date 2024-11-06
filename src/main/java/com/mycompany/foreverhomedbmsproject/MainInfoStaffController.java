package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Staff;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javax.swing.JOptionPane;

public class MainInfoStaffController implements Initializable {

    private Staff staff;

    @FXML
    private TextField ssnTextField;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField hireDateTextField;
    @FXML
    private TextField expertiseTextField;
    @FXML
    private TextField roleTextField;
    @FXML
    private TextField salaryTextField;
    @FXML
    private TextField dobTextField;
    @FXML
    private TextField ageTextField;

    @FXML
    private ComboBox<String> socialStatusComboBox;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private Button editInfoButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize ComboBox options
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female"));
        socialStatusComboBox.setItems(FXCollections.observableArrayList("Single", "Divorced", "Widowed", "Married"));
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
        updateFields();
    }

    private void updateFields() {
        if (staff != null) {
            ssnTextField.setText(staff.getSsn());
            fullNameTextField.setText(staff.getFullName());
            genderComboBox.setValue(staff.getGender());
            phoneNumberTextField.setText(staff.getPhoneNumber());
            emailTextField.setText(staff.getEmail());
            socialStatusComboBox.setValue(staff.getSocialStatus());
            addressTextField.setText(staff.getAddress());
            hireDateTextField.setText(staff.getHireDate().toString());
            expertiseTextField.setText(staff.getExpertise());
            roleTextField.setText(staff.getRole());
            salaryTextField.setText(String.format("$%.2f", staff.getSalary()));
            dobTextField.setText(staff.getDateOfBirth().toString());
            ageTextField.setText(String.valueOf(staff.getAge()));
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
                String socialStatus = socialStatusComboBox.getValue();
                String email = emailTextField.getText();
                String phoneNumber = phoneNumberTextField.getText();
                String dob = dobTextField.getText();
                String gender = genderComboBox.getValue();
                String hireDate = hireDateTextField.getText();
                String expertise = expertiseTextField.getText();
                String role = roleTextField.getText();
                double salary = Double.parseDouble(salaryTextField.getText().replaceAll("[$,]", ""));

                if (!validateDate(dob)) {
                    return;
                }

                if (!validateInput(socialStatus, gender, salary)) {
                    return;
                }

                String url = "jdbc:postgresql://localhost:5432/postgres";
                String dbUser = "postgres";
                String dbPassword = "ahm@212005";

                String queryPerson = "UPDATE Person SET FName = ?, LName = ?, Address = ?, Social_Status = ?, "
                        + "Email = ?, Phone_Number = ?, Date_of_Birth = ?, Gender = ? WHERE SSN = ?";
                String queryStaff = "UPDATE Staff SET Hire_Date = ?, Expertise = ?, Role = ?, Salary = ? WHERE SSN = ?";

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

                    try (PreparedStatement psStaff = con.prepareStatement(queryStaff)) {
                        psStaff.setDate(1, Date.valueOf(hireDate));
                        psStaff.setString(2, expertise);
                        psStaff.setString(3, role);
                        psStaff.setDouble(4, salary);
                        psStaff.setString(5, ssn);
                        psStaff.executeUpdate();
                    }

                    LocalDate dateOfBirth = LocalDate.parse(dob);
                    LocalDate currentDate = LocalDate.now();
                    int age = Period.between(dateOfBirth, currentDate).getYears();
                    ageTextField.setText(String.valueOf(age));
                    showAlert("Success", "Information updated successfully.");
                    toggleFieldsEditable(false);
                    editInfoButton.setText("Edit Information");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number for salary.");
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to save information: " + e.getMessage());
            }
        }
    }

    private boolean validateDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        try {
            LocalDate parsedDate = LocalDate.parse(date, formatter);
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

    private boolean validateInput(String socialStatus, String gender, double salary) {
        if (!socialStatus.equals("Single") && !socialStatus.equals("Divorced")
                && !socialStatus.equals("Widowed") && !socialStatus.equals("Married")) {
            showAlert("Invalid Input", "Social Status must be one of: Single, Divorced, Widowed, Married.");
            return false;
        }
        if (!gender.equals("Male") && !gender.equals("Female")) {
            showAlert("Invalid Input", "Gender must be one of: Male, Female.");
            return false;
        }
        if (salary < 0) {
            showAlert("Invalid Input", "Salary must be a positive number.");
            return false;
        }
        return true;
    }

    private void toggleFieldsEditable(boolean enable) {
        fullNameTextField.setDisable(!enable);
        fullNameTextField.setEditable(enable);

        genderComboBox.setDisable(!enable);
        phoneNumberTextField.setDisable(!enable);
        phoneNumberTextField.setEditable(enable);

        emailTextField.setDisable(!enable);
        emailTextField.setEditable(enable);

        socialStatusComboBox.setDisable(!enable);
        addressTextField.setDisable(!enable);
        addressTextField.setEditable(enable);

        hireDateTextField.setDisable(!enable);
        hireDateTextField.setEditable(enable);

        expertiseTextField.setDisable(!enable);
        expertiseTextField.setEditable(enable);

        roleTextField.setDisable(!enable);
        roleTextField.setEditable(enable);

        salaryTextField.setDisable(!enable);
        salaryTextField.setEditable(enable);

        dobTextField.setDisable(!enable);
        dobTextField.setEditable(enable);

        ageTextField.setDisable(true);
    }

    private void showAlert(String title, String message) {
        int messageType = title.equalsIgnoreCase("Success") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }
}
