package com.mycompany.foreverhomedbmsproject.Popups;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

public class NewDonationPopupController implements Initializable {

    @FXML
    private TextField donorNameField; // Connect to your FXML
    @FXML
    private TextField donationDateField; // Connect to your FXML
    @FXML
    private TextField amountField; // Connect to your FXML
    @FXML
    private ComboBox<String> donationTypeComboBox; // Connect to your FXML
    @FXML
    private ComboBox<String> purposeComboBox; // Connect to your FXML
    @FXML
    private Button nowButton; // Connect to your FXML

    private int eventId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the ComboBoxes with values
        donationTypeComboBox.getItems().addAll("Cash", "Check", "Goods", "Sponsor");
        purposeComboBox.getItems().addAll("Animal Goods", "Medical Care", "Rescue Missions");

    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    @FXML
    private void handleNowButtonClick() {
        // Get current date
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        // Set the current date to the donationDateField
        donationDateField.setText(formattedDate);
    }

    @FXML
    private void handleAddDonation() {
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        // Sanitize and validate donor name
        String donorName = donorNameField.getText().trim();
        if (donorName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Donor name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if donor name is empty
        }

        // Sanitize and validate donation type
        String donationType = donationTypeComboBox.getValue();
        if (donationType == null) {
            JOptionPane.showMessageDialog(null, "Please select a donation type.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit if donation type is not selected
        }

        // Sanitize and validate purpose
        String purpose = purposeComboBox.getValue();
        if (purpose == null) {
            JOptionPane.showMessageDialog(null, "Please select a purpose.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit if purpose is not selected
        }

        // Sanitize and validate donation date
        LocalDate dateOfDonation;
        try {
            dateOfDonation = LocalDate.parse(donationDateField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid date (YYYY-MM-DD).", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit if date is invalid
        }

        // Sanitize and validate amount
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                throw new NumberFormatException("Amount cannot be negative or zero.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid positive amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit if amount is invalid
        }

        String query = "INSERT INTO Donation (Doner_Name, Donation_Type, Purpose, Date_of_Donation, Amount, Event_ID) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, donorName);
            pstmt.setString(2, donationType);
            pstmt.setString(3, purpose);
            pstmt.setDate(4, java.sql.Date.valueOf(dateOfDonation));
            pstmt.setDouble(5, amount);
            pstmt.setInt(6, eventId); // Assuming eventId is already set

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Show success message
                JOptionPane.showMessageDialog(null, "Donation added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Show error message
                JOptionPane.showMessageDialog(null, "No donation was added.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Show error message
            JOptionPane.showMessageDialog(null, "Error occurred while adding donation: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close the popup after the operation
            ((javafx.stage.Stage) donorNameField.getScene().getWindow()).close();
        }
    }

}
