/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Application;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author lenovo
 */
public class ApplicationsExplorerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private List<Application> applicationList;
    
    
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox applicationsContainer;  // VBox to hold individual record items
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        applicationList = new ArrayList<>(); 
        getApplications();
    }
    private void getApplications() {
    String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String password = "ahm@212005";

    String query = "SELECT a.Animal_ID, a.animal_image, " +
                   "a.Name, " +
                   "DATE_PART('year', AGE(a.Date_of_Birth))::text AS Age, " +
                   "a.Gender, " +
                   "a.Breed, " +
                   "p.FName || ' ' || p.LName AS Adopter_Name, " +  // Concatenate first and last name
                   "ad.Occupation, " +
                   "DATE_PART('year', AGE(p.Date_of_Birth))::text AS Adopter_Age, " +  // Calculate adopter age
                   "ad.Number_of_Children, " +  // Correct field name
                   "ad.Number_of_Pets_Owned, " +  // Correct field name
                   "ad.Yearly_Income, " +
                   "ap.Application_Status, " +  // Correct alias for Application_Status
                   "ap.Application_Date, " +  // Correct alias for Application_Date
                   "p.SSN AS Adopter_SSN " +  // Alias for SSN
                   "FROM Animal a " +
                   "INNER JOIN Adopts ap ON a.Animal_ID = ap.Animal_ID "+
                   "LEFT JOIN Person p ON ap.SSN = p.SSN " +
                   "LEFT JOIN Adopter ad ON p.SSN = ad.SSN;";  // Join Adopter with Person using SSN

    
    try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {


        while (rs.next()) {
            // Extracting data from the result set
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

            // Creating an Application object (assuming you have this class)
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
            applicationList.add(currentApplication);
            
        }
    displayApplications();

    } catch (SQLException e) {
        e.printStackTrace();
    }
 }
 private void displayApplications(){
     for(Application currentApplication: applicationList){
          try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationItem.fxml"));
                Node applicationNode = loader.load();

                ApplicationItemController itemController = loader.getController();
                itemController.setUserType("Adopter");
                itemController.setApplicationData(currentApplication);

                applicationsContainer.getChildren().add(applicationNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
     }
 }
   
}

