package com.mycompany.foreverhomedbmsproject.Popups;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddEventController implements Initializable {

    @FXML
    private TextField eventIdField;

    @FXML
    private TextField eventNameField;

    @FXML
    private TextField eventTimeField;

    @FXML
    private TextField eventDateField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField fundGoalField;

    @FXML
    private Button addButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code if needed
    }

    @FXML
    private void handleAddButtonClick() {
        // Retrieve values from text fields
        String eventId = eventIdField.getText();
        String eventName = eventNameField.getText();
        String eventTimeStr = eventTimeField.getText();
        String eventDateStr = eventDateField.getText();
        String location = locationField.getText();
        String fundGoalStr = fundGoalField.getText();

        // Validate date format (YYYY-MM-DD)
        LocalDate eventDate;
        try {
            eventDate = LocalDate.parse(eventDateStr);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid date (YYYY-MM-DD).");
            return;
        }

        // Validate time format (HH:MM:SS)
        LocalTime eventTime;
        try {
            eventTime = LocalTime.parse(eventTimeStr);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid time (HH:MM:SS).");
            return;
        }

        // Validate funding goal to be a positive number
        double fundGoal;
        try {
            fundGoal = Double.parseDouble(fundGoalStr);
            if (fundGoal < 0) {
                throw new NumberFormatException("Funding goal cannot be negative.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid non-negative number for the funding goal.");
            return;
        }

        // Database connection parameters
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "ahm@212005";

        // SQL insert query
        String insertQuery = "INSERT INTO Event (Event_Name, Date_of_The_Event, Time_of_The_Event, Location, Funding_Goal) " +
                             "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            // Set query parameters
            statement.setString(1, eventName);
            statement.setDate(2, java.sql.Date.valueOf(eventDate));
            statement.setTime(3, java.sql.Time.valueOf(eventTime));
            statement.setString(4, location);
            statement.setDouble(5, fundGoal);

            // Execute the query
            statement.executeUpdate();

            // Close the window after successful insertion
            ((javafx.stage.Stage) eventIdField.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the event.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
