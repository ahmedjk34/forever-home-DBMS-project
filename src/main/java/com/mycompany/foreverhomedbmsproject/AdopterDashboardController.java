package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;

public class AdopterDashboardController implements Initializable {

    private Adopter adopter;
    private MainInfoAdopterController mainInfoAdopterController;

    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code here
    }

    public void setAdopter(Adopter adopter) {
        this.adopter = adopter;
        loadMainInfoAdopter();
    }

    private void loadMainInfoAdopter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainInfoAdopter.fxml"));
            AnchorPane mainInfoPane = loader.load();
            mainInfoAdopterController = loader.getController();
            mainInfoAdopterController.setAdopter(adopter);
            contentPane.getChildren().setAll(mainInfoPane);
        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMainInfoAdopterAction() {
        loadMainInfoAdopter();
    }

    @FXML
    private void handleApplicationsAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationsExplorer.fxml"));
            AnchorPane applicationsPane = loader.load();
            ApplicationsExplorerController controller = loader.getController();
            controller.setSSN(adopter.getSsn());
            controller.setUserType("Adopter");
            contentPane.getChildren().setAll(applicationsPane);
        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleAnimalsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AnimalExplorer.fxml"));
        AnchorPane animalPane = loader.load();
        AnimalExplorerController controller = loader.getController();
        controller.setUserType("Adopter");
        controller.setSSN(adopter.getSsn());
        contentPane.getChildren().setAll(animalPane);
    }

    @FXML
    private void handleContactUsAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EventsExplorer.fxml"));
            AnchorPane eventsPanel = loader.load();
            EventsExplorerController controller = loader.getController();
            controller.setUserType("Adopter");
            contentPane.getChildren().setAll(eventsPanel);
        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleFeedbackAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FeedbackExplorer.fxml"));
            AnchorPane feedbackPane = loader.load();
            FeedbackExplorerController controller = loader.getController();
            controller.setAdopterSSN(adopter.getSsn());
            contentPane.getChildren().setAll(feedbackPane);
        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMedicalRecordsAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MedicalRecordsExplorer.fxml"));
            AnchorPane medicalRecordsPane = loader.load();
            MedicalRecordsExplorerController controller = loader.getController();
            controller.setSSN(adopter.getSsn());
            controller.setUserType("Adopter");
            contentPane.getChildren().setAll(medicalRecordsPane);

        } catch (IOException ex) {
            Logger.getLogger(AdopterDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleLogoutAction() {
        try {
            // Load the new FXML for the Authentication view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Authentication.fxml"));
            AnchorPane authenticationPane = loader.load();

            // Retrieve the current stage using the contentPane
            Stage stage = (Stage) contentPane.getScene().getWindow();

            // Set the new root and adjust the window size
            stage.getScene().setRoot(authenticationPane);
            stage.setWidth(700);
            stage.setHeight(600);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
