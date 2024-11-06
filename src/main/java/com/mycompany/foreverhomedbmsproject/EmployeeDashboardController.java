package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Staff;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;

public class EmployeeDashboardController implements Initializable {

    Staff staff;

    @FXML
    AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadMainInfoStaff();
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
        loadMainInfoStaff();
    }

    private void loadMainInfoStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainInfoStaff.fxml"));
            AnchorPane mainInfoPane = loader.load();
            MainInfoStaffController controller = loader.getController();
            controller.setStaff(staff);
            System.out.println("TEST" + staff);
            contentPane.getChildren().setAll(mainInfoPane);
        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMainInfoAction() {
        System.out.println("Main Info button clicked");
        loadMainInfoStaff();
    }

    // Handles the Applications button click
    @FXML
    private void handleApplicationsAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationsExplorer.fxml"));
            AnchorPane applicationsPane = loader.load();
            ApplicationsExplorerController controller = loader.getController();
            controller.setUserType("Staff");
            controller.setSSN(staff.getSsn());
            contentPane.getChildren().setAll(applicationsPane);
        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Handles the Animals button click
    @FXML
    private void handleAnimalsAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AnimalExplorer.fxml"));
            AnchorPane animalPane = loader.load();
            AnimalExplorerController controller = loader.getController();
            controller.setSSN(staff.getSsn());
            controller.setUserType("Staff");
            contentPane.getChildren().setAll(animalPane);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Handles the Events button click
    @FXML
    private void handleEventsAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EventsExplorer.fxml"));
            AnchorPane eventsPanel = loader.load();
            EventsExplorerController controller = loader.getController();
            controller.setUserType("Staff");
            contentPane.getChildren().setAll(eventsPanel);
        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleAdopterAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdoptersExplorer.fxml"));
            AnchorPane adopterPanel = loader.load();
            contentPane.getChildren().setAll(adopterPanel);
        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Handles the Feedback button click
    @FXML
    private void handleFeedbackAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FeedbackExplorer.fxml"));
            AnchorPane feedbackPane = loader.load();
            FeedbackExplorerController controller = loader.getController();
            contentPane.getChildren().setAll(feedbackPane);
        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Handles the Medical Records button click
    @FXML
    private void handleMedicalRecordsAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MedicalRecordsExplorer.fxml"));
            AnchorPane medicalRecordsPane = loader.load();
            MedicalRecordsExplorerController controller = loader.getController();
            controller.setSSN(staff.getSsn());
            controller.setUserType("Staff");
            contentPane.getChildren().setAll(medicalRecordsPane);

        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Handles the Employees button click
    @FXML
    private void handleEmployeesAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StaffExplorer.fxml"));
            AnchorPane staffRecordsPane = loader.load();
            StaffExplorerController controller = loader.getController();
            contentPane.getChildren().setAll(staffRecordsPane);

        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Handles the Log Out button click
    @FXML
    private void handleLogoutAction() {
        System.out.println("Log Out button clicked");
        // Add your handling code here (e.g., log out the user)
    }

}
