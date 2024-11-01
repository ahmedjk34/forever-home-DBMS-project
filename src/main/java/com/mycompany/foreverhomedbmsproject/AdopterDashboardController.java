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
    private void handleMainInfoAdopterAction(){
         loadMainInfoAdopter();
    }
    @FXML
    private void handleApplicationsAction() {
        System.out.println("Applications button clicked");
    }

    @FXML
    private void handleAnimalsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AnimalExplorer.fxml"));
        AnchorPane animalPane = loader.load();
        contentPane.getChildren().setAll(animalPane);
    }

    @FXML
    private void handleContactUsAction() {
        System.out.println("Contact Us button clicked");
    }

    @FXML
    private void handleFeedbackAction() {
        System.out.println("Feedback button clicked");
    }

    @FXML
    private void handleMedicalRecordsAction() {
        System.out.println("Medical Records button clicked");
    }
}
