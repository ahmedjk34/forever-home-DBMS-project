package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Staff;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;

public class EmployeeDashboardController {

    Staff staff;
    AnchorPane contentPane;

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

//    private void loadMainInfoAdopter() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainInfoEmployee.fxml"));
//            AnchorPane mainInfoPane = loader.load();
//            MainInfoEmployeeController controller = loader.getController();
//            controller.setStaff(staff);
//            contentPane.getChildren().setAll(mainInfoPane);
//        } catch (IOException ex) {
//            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    @FXML
    private void handleMainInfoAction() {
        System.out.println("Main Info button clicked");
        // Add your handling code here (e.g., show main information)
    }

    // Handles the Applications button click
    @FXML
    private void handleApplicationsAction() {
        System.out.println("Applications button clicked");
        // Add your handling code here (e.g., show applications)
    }

    // Handles the Animals button click
    @FXML
    private void handleAnimalsAction() {
        System.out.println("Animals button clicked");
        // Add your handling code here (e.g., show animal details)
    }

    // Handles the Events button click
    @FXML
    private void handleEventsAction() {
        System.out.println("Events button clicked");
        // Add your handling code here (e.g., show events)
    }

    // Handles the Feedback button click
    @FXML
    private void handleFeedbackAction() {
        System.out.println("Feedback button clicked");
        // Add your handling code here (e.g., show feedback form)
    }

    // Handles the Medical Records button click
    @FXML
    private void handleMedicalRecordsAction() {
        System.out.println("Medical Records button clicked");
        // Add your handling code here (e.g., show medical records)
    }

    // Handles the Employees button click
    @FXML
    private void handleEmployeesAction() {
        System.out.println("Employees button clicked");
        // Add your handling code here (e.g., show employees list)
    }

    // Handles the Log Out button click
    @FXML
    private void handleLogoutAction() {
        System.out.println("Log Out button clicked");
        // Add your handling code here (e.g., log out the user)
    }
}
