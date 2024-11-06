package com.mycompany.foreverhomedbmsproject.Popups;

import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditAdopterPopupController implements Initializable {

    private Adopter adopter;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate ComboBox items for gender and social status
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        socialStatusComboBox.getItems().addAll("Single", "Married", "Divorced", "Widowed");
    }

    /**
     * Sets the adopter and populates the form fields with adopter data.
     *
     * @param adopter The adopter to edit.
     */
    public void setAdopter(Adopter adopter) {
        this.adopter = adopter;
        populateFields();
    }

    /**
     * Populates the form fields with the adopter's existing data.
     */
    private void populateFields() {
        if (adopter != null) {
            idTextField.setText(adopter.getSsn());
            fnameTextField.setText(adopter.getFName());
            lnameTextField.setText(adopter.getLName());
            emailTextField.setText(adopter.getEmail());
            phoneNumberTextField.setText(adopter.getPhoneNumber());
            OccupationTextField.setText(adopter.getOccupation());
            NoPetsTextField.setText(String.valueOf(adopter.getNumberOfPetsOwned()));
            dobTextField.setText(adopter.getDateOfBirth().toString());
            addressTextField.setText(adopter.getAddress());
            socialStatusComboBox.setValue(adopter.getSocialStatus());
            NoChildrenTextField.setText(String.valueOf(adopter.getNumberOfChildren()));
            YearlyIncomeTextField.setText(String.valueOf(adopter.getYearlyIncome()));
            genderComboBox.setValue(adopter.getGender());
            passwordField.setText(adopter.getPassword());
        }
    }

    /**
     * Handles the edit adopter action.
     */
    @FXML
    private void editAdopter() {
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

        // Database connection parameters
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String dbPassword = "ahm@212005";

        Connection conn = null;
        PreparedStatement psPerson = null;
        PreparedStatement psAdopter = null;

        try {
            conn = java.sql.DriverManager.getConnection(url, user, dbPassword);
            // Start transaction
            conn.setAutoCommit(false);

            // Update Person table
            String personUpdateQuery = "UPDATE Person SET "
                    + "Password = ?, "
                    + "FName = ?, "
                    + "LName = ?, "
                    + "Address = ?, "
                    + "Social_Status = ?, "
                    + "Email = ?, "
                    + "Phone_Number = ?, "
                    + "Date_of_Birth = ?, "
                    + "Gender = ? "
                    + "WHERE SSN = ?";

            psPerson = conn.prepareStatement(personUpdateQuery);
            psPerson.setString(1, password);
            psPerson.setString(2, firstName);
            psPerson.setString(3, lastName);
            psPerson.setString(4, address);
            psPerson.setString(5, socialStatus);
            psPerson.setString(6, email);
            psPerson.setString(7, phoneNumber);
            psPerson.setDate(8, Date.valueOf(dob));
            psPerson.setString(9, gender);
            psPerson.setString(10, adopterID);

            int personRows = psPerson.executeUpdate();
            if (personRows == 0) {
                throw new SQLException("Updating Person failed, no rows affected.");
            }

            // Update Adopter table
            String adopterUpdateQuery = "UPDATE Adopter SET "
                    + "Occupation = ?, "
                    + "Number_of_Pets_Owned = ?, "
                    + "Number_of_Children = ?, "
                    + "Yearly_Income = ? "
                    + "WHERE SSN = ?";

            psAdopter = conn.prepareStatement(adopterUpdateQuery);
            psAdopter.setString(1, occupation);
            psAdopter.setInt(2, noPets);
            psAdopter.setInt(3, noChildren);
            psAdopter.setBigDecimal(4, new java.math.BigDecimal(yearlyIncome).setScale(2, java.math.RoundingMode.HALF_UP));
            psAdopter.setString(5, adopterID);

            int adopterRows = psAdopter.executeUpdate();
            if (adopterRows == 0) {
                throw new SQLException("Updating Adopter failed, no rows affected.");
            }

            // Commit transaction
            conn.commit();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Adopter information updated successfully.");
            closePopup();

        } catch (SQLException e) {
            // Rollback transaction if any error occurs
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to rollback transaction: " + ex.getMessage());
                }
            }
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating adopter information: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (psPerson != null) {
                    psPerson.close();
                }
                if (psAdopter != null) {
                    psAdopter.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error closing database resources: " + ex.getMessage());
            }
        }
    }

    /**
     * Displays an alert dialog.
     *
     * @param alertType The type of alert.
     * @param title The title of the alert.
     * @param message The message content.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Closes the current popup window.
     */
    private void closePopup() {
        Stage stage = (Stage) idTextField.getScene().getWindow();
        stage.close();
    }
}
