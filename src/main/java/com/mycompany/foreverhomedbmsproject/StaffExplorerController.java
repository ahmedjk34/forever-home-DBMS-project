package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Popups.AddEmployeePopupController;
import com.mycompany.foreverhomedbmsproject.Popups.EditEmployeePopupController;
import com.mycompany.foreverhomedbmsproject.Server.Staff;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class StaffExplorerController implements Initializable {

    @FXML
    private TableView<Staff> employeeTable; // Updated to employeeTable

    @FXML
    private TableColumn<Staff, String> employeeIdColumn;

    @FXML
    private TableColumn<Staff, String> FnameColumn;

    @FXML
    private TableColumn<Staff, String> LnameColumn;

    @FXML
    private TableColumn<Staff, String> socialStatusColumn;

    @FXML
    private TableColumn<Staff, String> phoneNumberColumn;

    @FXML
    private TableColumn<Staff, String> expertiseColumn;

    @FXML
    private TableColumn<Staff, String> roleColumn;

    @FXML
    private TableColumn<Staff, Double> salaryColumn;

    private ObservableList<Staff> employeeList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employeeList = FXCollections.observableArrayList();

        // Initialize the table columns
        employeeIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSsn()));
        FnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFName()));
        LnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLName()));
        socialStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSocialStatus()));
        phoneNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNumber()));
        expertiseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpertise()));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        salaryColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSalary()).asObject());

        // Create some sample data and load it into the table
        // Fetch employee data from the database and load it into the table
        getEmployeeDate();
    }

    private void getEmployeeDate() {

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";
        String query = "SELECT p.SSN, p.FName,p.address , p.password,  p.LName, p.Social_Status, p.Phone_Number, p.Email, p.Date_of_Birth, p.Gender, "
                + "s.Hire_Date, s.Expertise, s.Role, s.Salary "
                + "FROM Person p "
                + "JOIN Staff s ON p.SSN = s.SSN";

        try (Connection conn = DriverManager.getConnection(url, user, password); var stmt = conn.createStatement(); var rs = stmt.executeQuery(query)) {

            employeeList.clear();  // Clear any existing data

            while (rs.next()) {
                String ssn = rs.getString("SSN");
                String fname = rs.getString("FName");
                String lname = rs.getString("LName");
                String socialStatus = rs.getString("Social_Status");
                String phoneNumber = rs.getString("Phone_Number");
                String email = rs.getString("Email");
                LocalDate dob = rs.getDate("Date_of_Birth").toLocalDate();
                String gender = rs.getString("Gender");
                LocalDate hireDate = rs.getDate("Hire_Date").toLocalDate();
                String expertise = rs.getString("Expertise");
                String role = rs.getString("Role");
                double salary = rs.getDouble("Salary");
                String empPassword = rs.getString("password");
                String address = rs.getString("address");

                Staff staffMember = new Staff(ssn, empPassword, gender, fname, lname, address, socialStatus, email, phoneNumber, dob, hireDate, expertise, role, salary);
                employeeList.add(staffMember);
            }

            // Update the table with the fetched employee data
            employeeTable.setItems(employeeList);

        } catch (Exception e) {
            e.printStackTrace();  // Handle exceptions
        }
    }

    // Method to handle adding a new employee
    @FXML
    private void addEmployee() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/AddEmployeePopup.fxml"));
            AnchorPane popup = loader.load();
            AddEmployeePopupController controller = loader.getController();

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Add a New Employee");

            // Set the scene with the loaded FXML
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);

            // Optional: Set the modality to block input to other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to close
            popupStage.showAndWait();

            getEmployeeDate();
        } catch (IOException ex) {
            Logger.getLogger(StaffExplorerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void removeEmployee() {
        Staff selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        // Check if an employee is selected
        if (selectedEmployee != null) {
            // Confirmation dialog before deletion
            int confirmation = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this employee?",
                    "Delete Employee", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                // Get SSN of the employee to be deleted
                String ssn = selectedEmployee.getSsn();

                // SQL to delete the employee from the database
                String url = "jdbc:postgresql://localhost:5432/postgres";
                String user = "postgres";
                String password = "ahm@212005";
                String deleteQuery = "DELETE FROM Staff WHERE SSN = ? CASCADE";  // SQL query to delete

                try (Connection conn = DriverManager.getConnection(url, user, password); var pstmt = conn.prepareStatement(deleteQuery)) {

                    pstmt.setString(1, ssn);  // Set the SSN parameter
                    int rowsAffected = pstmt.executeUpdate();  // Execute the delete query

                    if (rowsAffected > 0) {
                        // Deletion successful, remove from ObservableList
                        employeeList.remove(selectedEmployee);
                        // Reload the table data after deletion
                        getEmployeeDate();
                        JOptionPane.showMessageDialog(null, "Employee deleted successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // If no rows were affected, show an error
                        JOptionPane.showMessageDialog(null, "Employee not found or unable to delete.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception e) {
                    // Show error dialog if an exception occurs
                    JOptionPane.showMessageDialog(null, "An error occurred while deleting the employee: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();  // Print stack trace for debugging
                }
            }
        } else {
            // If no employee is selected, show a warning
            JOptionPane.showMessageDialog(null, "Please select an employee to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }


    @FXML
    private void editEmployee() {
        Staff selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            // Show a JOptionPane if no row is selected
            JOptionPane.showMessageDialog(null, "Please select an employee from the table.", "No Employee Selected", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/EditEmployeePopup.fxml"));
                AnchorPane popup = loader.load();
                EditEmployeePopupController controller = loader.getController();
                controller.setEmployee(selectedEmployee);

                // Create a new stage for the popup
                Stage popupStage = new Stage();
                popupStage.setTitle("Edit Employee");

                // Set the scene with the loaded FXML
                Scene scene = new Scene(popup);
                popupStage.setScene(scene);

                // Optional: Set the modality to block input to other windows
                popupStage.initModality(Modality.APPLICATION_MODAL);

                // Show the popup and wait for it to close
                popupStage.showAndWait();

                getEmployeeDate();
            } catch (IOException ex) {
                Logger.getLogger(StaffExplorerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void generateReport() {

    }
}
