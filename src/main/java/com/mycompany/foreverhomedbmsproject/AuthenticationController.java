/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane; 

/**
 * FXML Controller class
 *
 * @author lenovo
 */
public class AuthenticationController implements Initializable {

    @FXML
    private TextField ssnField; // Assuming there's a TextField for SSN input
    @FXML
    private TextField passwordField; // Assuming there's a TextField for password input

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleLogin() throws IOException {
        String ssn = ssnField.getText();
        String password = passwordField.getText();

        if (ssn.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please enter both SSN and password.");
            return;
        }

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "ahm@212005";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String query = "SELECT p.SSN, a.SSN AS AdopterSSN, s.SSN AS StaffSSN "
                         + "FROM Person p "
                         + "LEFT JOIN Adopter a ON p.SSN = a.SSN "
                         + "LEFT JOIN Staff s ON p.SSN = s.SSN "
                         + "WHERE p.SSN = ? AND p.Password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ssn);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString("AdopterSSN") != null) {
                    // Load the adopter view if user is an adopter
                    App.setRoot("AdopterDashboard"); // Adjust to your Adopter FXML file name
                } else if (resultSet.getString("StaffSSN") != null) {
                    // Load the staff view if user is staff
                    App.setRoot("StaffDashboard"); // Adjust to your Staff FXML file name
                } else {
                    // Handle case if user is neither in Adopter nor Staff (unlikely if data is correct)
                    showAlert("Login Error", "Invalid user role.");
                }
            } else {
                showAlert("Login Error", "Invalid SSN or password.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to connect to the database: " + e.getMessage());
        }
    }

    // Method to show alerts using JOptionPane
    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
