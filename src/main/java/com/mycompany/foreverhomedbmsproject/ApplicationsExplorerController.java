package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Popups.NewApplicationPopupController;
import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import com.mycompany.foreverhomedbmsproject.Server.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class ApplicationsExplorerController implements Initializable {

    private List<Application> applicationList;

    private String userType;
    private String SSN;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox applicationsContainer;  // VBox to hold individual record items

    @FXML
    private Button addApplicationButton;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        applicationList = new ArrayList<>();
        getApplications();
    }

     
    private void getApplications() {
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        // Define the query based on the userType
        String query;

        if ("Adopter".equals(userType)) {
            // Query for Adopter - get only the adopter's pending applications
            query = "SELECT a.Animal_ID, a.animal_image, "
                    + "a.Name, "
                    + "DATE_PART('year', AGE(a.Date_of_Birth))::text AS Age, "
                    + "a.Gender, "
                    + "a.Breed, "
                    + "p.FName || ' ' || p.LName AS Adopter_Name, "
                    + "ad.Occupation, "
                    + "DATE_PART('year', AGE(p.Date_of_Birth))::text AS Adopter_Age, "
                    + "ad.Number_of_Children, "
                    + "ad.Number_of_Pets_Owned, "
                    + "ad.Yearly_Income, "
                    + "ap.Application_Status, "
                    + "ap.Application_Date, "
                    + "p.SSN AS Adopter_SSN "
                    + "FROM Animal a "
                    + "INNER JOIN Adopts ap ON a.Animal_ID = ap.Animal_ID "
                    + "LEFT JOIN Person p ON ap.SSN = p.SSN "
                    + "LEFT JOIN Adopter ad ON p.SSN = ad.SSN "
                    + "WHERE ap.Application_Status = 'Pending' "
                    + "AND p.SSN = ?;";

        } else if ("Staff".equals(userType)) {
            // Query for Staff - get all pending applications
            query = "SELECT a.Animal_ID, a.animal_image, "
                    + "a.Name, "
                    + "DATE_PART('year', AGE(a.Date_of_Birth))::text AS Age, "
                    + "a.Gender, "
                    + "a.Breed, "
                    + "p.FName || ' ' || p.LName AS Adopter_Name, "
                    + "ad.Occupation, "
                    + "DATE_PART('year', AGE(p.Date_of_Birth))::text AS Adopter_Age, "
                    + "ad.Number_of_Children, "
                    + "ad.Number_of_Pets_Owned, "
                    + "ad.Yearly_Income, "
                    + "ap.Application_Status, "
                    + "ap.Application_Date, "
                    + "p.SSN AS Adopter_SSN "
                    + "FROM Animal a "
                    + "INNER JOIN Adopts ap ON a.Animal_ID = ap.Animal_ID "
                    + "LEFT JOIN Person p ON ap.SSN = p.SSN "
                    + "LEFT JOIN Adopter ad ON p.SSN = ad.SSN "
                    + "WHERE ap.Application_Status = 'Pending';";  // Retrieve all pending applications for Staff
        } else {
            System.out.println("Unknown user type.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password); PreparedStatement stmt = conn.prepareStatement(query)) {

            if ("Adopter".equals(userType) && SSN != null && !SSN.isEmpty()) {
                stmt.setString(1, SSN);  // Set SSN if the user is an Adopter
            }
            System.out.println(SSN);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int animalId = rs.getInt("Animal_ID");
                    String animalName = rs.getString("Name");
                    String age = rs.getString("Age");
                    String gender = rs.getString("Gender");
                    String breed = rs.getString("Breed");
                    String adopterSSN = rs.getString("Adopter_SSN");
                    String adopterName = rs.getString("Adopter_Name");
                    String occupation = rs.getString("Occupation");
                    int adopterAge = rs.getInt("Adopter_Age");
                    int numberOfKids = rs.getInt("Number_of_Children");
                    int numberOfPets = rs.getInt("Number_of_Pets_Owned");
                    int yearlyIncome = (int) rs.getDouble("Yearly_Income");
                    String adoptionStatus = rs.getString("Application_Status");
                    Date adoptionDate = rs.getDate("Application_Date");
                    String animalImage = rs.getString("animal_image");

                    // Create a new Application object with the retrieved data
                    Application currentApplication = new Application(
                            animalId,
                            animalName,
                            age,
                            gender,
                            breed,
                            adopterSSN,
                            adopterName,
                            occupation,
                            adopterAge,
                            numberOfKids,
                            numberOfPets,
                            yearlyIncome,
                            adoptionStatus,
                            adoptionDate,
                            animalImage
                    );
                    applicationList.add(currentApplication);  // Add the application to the list
                }

                // Display the applications
                displayApplications();
            }

        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayApplications() {
        for (Application currentApplication : applicationList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationItem.fxml"));
                Node applicationNode = loader.load();

                ApplicationItemController itemController = loader.getController();
                itemController.setUserType(userType);
                itemController.setParentController(this);
                itemController.setApplicationData(currentApplication);

                applicationsContainer.getChildren().add(applicationNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void showAddApplicationPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/NewApplicationPopup.fxml"));
            Node popup = loader.load();
            NewApplicationPopupController controller = loader.getController();
            controller.setAdopterSSN(SSN);

            Stage popupStage = new Stage();
            popupStage.setTitle("Add New Application");

            Scene scene = new Scene((Parent) popup);
            popupStage.setScene(scene);

            // Optional: Set the modality to block input to other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to close
            popupStage.showAndWait();

            // Refresh the applications after the popup closes
            refreshApplications();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshApplications() {
        applicationsContainer.getChildren().clear();
        applicationList.clear();
        getApplications(); // Call to repopulate the applications
    }

    public void setUserType(String userType) {
        this.userType = userType;
        getApplications();
        if (userType.equals("Staff")) addApplicationButton.setVisible(false);
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

}
