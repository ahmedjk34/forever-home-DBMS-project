/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject.Popups;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author lenovo
 */
public class NewFeedbackPopupController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button addReviewButton;

    @FXML
    private TextArea reviewBody;

    @FXML
    private TextField reviewTitle;

    private String adopterSSN;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void addFeedback() {
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        // Get input values
        String title = reviewTitle.getText();
        String body = reviewBody.getText();

        // Validate inputs
        if (adopterSSN == null || adopterSSN.isEmpty()) {
            showAlert("Error", "SSN cannot be null or empty.");
            return;
        }
        if (title == null || title.isEmpty() || title.length() > 100) {
            showAlert("Error", "Review title must be between 1 and 100 characters.");
            return;
        }
        if (body == null || body.isEmpty()) {
            showAlert("Error", "Review body cannot be empty.");
            return;
        }

        String query = "INSERT INTO Feedback (SSN, Feedback_Title, Feedback_Text) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password); PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters
            pstmt.setString(1, adopterSSN);
            pstmt.setString(2, title);
            pstmt.setString(3, body);

            // Execute the query
            int rowsAffected = pstmt.executeUpdate();

            // Show success message
            if (rowsAffected > 0) {
                showAlert("Success", "Review added successfully!");
                Stage stage = (Stage) addReviewButton.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Error", "Failed to add review. Please try again.");
            }

        } catch (Exception e) {
            showAlert("Error", "An error occurred while adding the review: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        int messageType = title.equalsIgnoreCase("Success") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    public void setAdopterSSN(String adopterSSN) {
        this.adopterSSN = adopterSSN;
    }
}
