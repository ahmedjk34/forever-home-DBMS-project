/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Application;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

public class ApplicationItemController implements Initializable {

    @FXML
    private ImageView animalImage;
    @FXML
    private Label applicationStatusLabel;
    @FXML
    private Label applicationDateLabel;
    @FXML
    private Label adopterNameLabel;
    @FXML
    private Label adopterSSNLabel;
    @FXML
    private Label adopterOccupationLabel;
    @FXML
    private Label adopterNumOfKidsLabel;
    @FXML
    private Label adopterNumOfPetsLabel;
    @FXML
    private Label adopterYearlyIncomeLabel;
    @FXML
    private TextField animalIDField;
    @FXML
    private TextField animalNameField;
    @FXML
    private TextField animalGenderField;
    @FXML
    private TextField animalAgeField;

    @FXML
    private Button acceptButton;
    @FXML
    private Button withdrawButton;
    @FXML
    private Button rejectButton;

    private ApplicationsExplorerController parentController;

    private String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private String user = "postgres";
    private String password = "ahm@212005";

    private String userType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic if needed

    }

    public void setApplicationData(Application application) {
        // Set animal details
        animalIDField.setText(String.valueOf(application.getAnimalId()));
        animalNameField.setText(application.getAnimalName());
        animalGenderField.setText(application.getGender());
        animalAgeField.setText(application.getAge());

        // Set adopter details
        adopterNameLabel.setText(application.getAdopterName());
        adopterSSNLabel.setText(application.getAdopterSSN());
        adopterOccupationLabel.setText(application.getOccupation());
        adopterNumOfKidsLabel.setText(String.valueOf(application.getNumberOfKids()));
        adopterNumOfPetsLabel.setText(String.valueOf(application.getNumberOfPets()));
        adopterYearlyIncomeLabel.setText(String.valueOf(application.getYearlyIncome() + "$"));

        // Set application status and date
        applicationStatusLabel.setText(application.getAdoptionStatus());
        applicationDateLabel.setText(application.getAdoptionDate() != null ? application.getAdoptionDate().toString() : "N/A");

        // Set the animal image
        if (application.getAnimalImage() != null && !application.getAnimalImage().isEmpty()) {
            animalImage.setImage(new Image(application.getAnimalImage()));
        } else {
            // Set a default image if none is provided
            animalImage.setImage(new Image("path/to/default/image.png")); // replace with actual default image path
        }

        if (userType.equals("Adopter")) {
            acceptButton.setVisible(false);
            rejectButton.setVisible(false);
        } else {
            withdrawButton.setVisible(false);
        }
    }

    @FXML
    private void withdrawApplication() {
        String adopterSSN = adopterSSNLabel.getText();
        String animalID = animalIDField.getText();

        // Define the SQL query for deleting the application
        String query = "DELETE FROM Adopts WHERE SSN = ? AND Animal_ID = ?";

        try (
                Connection conn = DriverManager.getConnection(dbUrl, user, password); PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            pstmt.setString(1, adopterSSN);
            pstmt.setInt(2, Integer.parseInt(animalID));

            // Execute the deletion
            int affectedRows = pstmt.executeUpdate();

            // Check if any rows were deleted
            if (affectedRows > 0) {
                showAlert("Success", "Application withdrawn successfully.");
                parentController.refreshApplications();
            } else {
                showAlert("Error", "Application already withdrawn");

            }

        } catch (SQLException e) {
            // Handle SQL exceptions using showAlert
            e.printStackTrace();
            showAlert("Error", "Error withdrawing application: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Handle invalid number format for animal ID using showAlert
            showAlert("Error", "Invalid Animal ID.");
        }
    }

    @FXML
    private void acceptApplication() {
        String adopterSSN = adopterSSNLabel.getText();
        String animalID = animalIDField.getText();

        // Define the SQL query to update the application status to "Approved"
        String query = "UPDATE Adopts SET application_status = 'Approved' WHERE SSN = ? AND Animal_ID = ?";

        try (
                Connection conn = DriverManager.getConnection(dbUrl, user, password); PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameters for the prepared statement
            pstmt.setString(1, adopterSSN);
            pstmt.setInt(2, Integer.parseInt(animalID));

            // Execute the update
            int affectedRows = pstmt.executeUpdate();

            // Check if any rows were updated
            if (affectedRows > 0) {
                showAlert("Success", "Application accepted successfully.");
                parentController.refreshApplications();
            } else {
                showAlert("Error", "Failed to accept application. Please try again.");
            }

        } catch (SQLException e) {
            // Handle SQL exceptions using showAlert
            e.printStackTrace();
            showAlert("Error", "Error accepting application: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Handle invalid number format for animal ID
            showAlert("Error", "Invalid Animal ID.");
        }
    }

    @FXML
    private void rejectApplication() {
        String adopterSSN = adopterSSNLabel.getText();
        String animalID = animalIDField.getText();

        // Define the SQL query to update the application status to "Rejected"
        String query = "UPDATE Adopts SET application_status = 'Rejected' WHERE SSN = ? AND Animal_ID = ?";

        try (
                Connection conn = DriverManager.getConnection(dbUrl, user, password); PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the parameters for the prepared statement
            pstmt.setString(1, adopterSSN);
            pstmt.setInt(2, Integer.parseInt(animalID));

            // Execute the update
            int affectedRows = pstmt.executeUpdate();

            // Check if any rows were updated
            if (affectedRows > 0) {
                showAlert("Success", "Application rejected successfully.");
                parentController.refreshApplications();
            } else {
                showAlert("Error", "Failed to reject application. Please try again.");
            }

        } catch (SQLException e) {
            // Handle SQL exceptions using showAlert
            e.printStackTrace();
            showAlert("Error", "Error rejecting application: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Handle invalid number format for animal ID
            showAlert("Error", "Invalid Animal ID.");
        }
    }

    private void showAlert(String title, String message) {
        int messageType = title.equalsIgnoreCase("Success") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setParentController(ApplicationsExplorerController parentController) {
        this.parentController = parentController;
    }
}
