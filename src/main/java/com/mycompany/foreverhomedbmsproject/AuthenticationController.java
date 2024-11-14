/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import com.mycompany.foreverhomedbmsproject.Server.Staff;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
            String query = "SELECT p.SSN, p.Password, p.FName, p.LName, p.Address, p.Social_status, p.Gender, "
                    + "p.Email, p.Phone_Number, p.Date_Of_Birth, "
                    + "a.Occupation, a.Number_of_Pets_Owned, a.Number_of_Children, a.Yearly_Income, "
                    + "s.Hire_Date, s.Expertise, s.Role, s.Salary, "
                    + "CASE WHEN a.SSN IS NOT NULL THEN 'Adopter' "
                    + "     WHEN s.SSN IS NOT NULL THEN 'Staff' "
                    + "     ELSE 'Unknown' END AS RoleType "
                    + "FROM Person p "
                    + "LEFT JOIN Adopter a ON p.SSN = a.SSN "
                    + "LEFT JOIN Staff s ON p.SSN = s.SSN "
                    + "WHERE p.SSN = ? AND p.Password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ssn);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString("Hire_Date") == null) { // This implies user is an Adopter
                    // Extract all necessary data from ResultSet to create an Adopter object
                    String retrievedSSN = resultSet.getString("SSN");
                    String retrievedPassword = resultSet.getString("Password");
                    String retrievedGender = resultSet.getString("Gender");
                    String fName = resultSet.getString("FName");
                    String lName = resultSet.getString("LName");
                    String address = resultSet.getString("Address");
                    String socialStatus = resultSet.getString("Social_status");
                    String email = resultSet.getString("Email");
                    String phoneNumber = resultSet.getString("Phone_Number");
                    LocalDate dateOfBirth = resultSet.getDate("Date_Of_Birth").toLocalDate();
                    String occupation = resultSet.getString("Occupation");
                    int numberOfPetsOwned = resultSet.getInt("Number_of_Pets_Owned");
                    int numberOfChildren = resultSet.getInt("Number_of_Children");
                    double yearlyIncome = resultSet.getDouble("Yearly_Income");

                    // Create Adopter object with the retrieved data
                    Adopter loggedAdopter = new Adopter(retrievedSSN, retrievedPassword, retrievedGender, fName, lName, address, socialStatus,
                            email, phoneNumber, dateOfBirth, occupation, numberOfPetsOwned,
                            numberOfChildren, yearlyIncome);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AdopterDashboard.fxml"));
                    Parent root = loader.load();
                    AdopterDashboardController controller = loader.getController();
                    controller.setAdopter(loggedAdopter);
                    Stage stage = (Stage) ((Node) ssnField).getScene().getWindow();
                    stage.setWidth(930);
                    stage.setHeight(685);
                    stage.getScene().setRoot(root);

                } else { // User is Staff
                    String staffRole = resultSet.getString("Role");
                    System.out.println("Staff role: " + staffRole);  // For debugging

                    // Create Staff object with data retrieved (Optional)
                    String retrievedSSN = resultSet.getString("SSN");
                    String retrievedPassword = resultSet.getString("Password");
                    String fName = resultSet.getString("FName");
                    String lName = resultSet.getString("LName");
                    String address = resultSet.getString("Address");
                    String email = resultSet.getString("Email");
                    String phoneNumber = resultSet.getString("Phone_Number");
                    LocalDate dateOfBirth = resultSet.getDate("Date_Of_Birth").toLocalDate();
                    LocalDate hireDate = resultSet.getDate("Hire_Date").toLocalDate();
                    String expertise = resultSet.getString("Expertise");
                    String role = resultSet.getString("Role");
                    double salary = resultSet.getDouble("Salary");
                    String socialStatus = resultSet.getString("Social_status");
                    String retrievedGender = resultSet.getString("Gender");

                    if (!role.equals("Manager")) {
                        showAlert("Login Error", "Only Manager login is supported for staff currently");
                        return;
                    }

                    Staff loggedStaff = new Staff(retrievedSSN, retrievedPassword, retrievedGender, fName, lName, address,
                            socialStatus, email, phoneNumber, dateOfBirth, hireDate, expertise, role, salary);

                    // Load Staff Dashboard for staff user
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeDashboard.fxml"));
                    Parent root = loader.load();
                    EmployeeDashboardController controller = loader.getController();
                    controller.setStaff(loggedStaff);
                    Stage stage = (Stage) ((Node) ssnField).getScene().getWindow();
                    stage.setWidth(930);
                    stage.setHeight(685);
                    stage.getScene().setRoot(root);
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
