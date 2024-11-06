/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Adopter;

/**
 * FXML Controller class
 *
 * @author lenovo
 */
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdopterExplorerController implements Initializable {

    @FXML
    private AnchorPane AdopterAnchor;
    @FXML
    private TableView<Adopter> AdoptersTable;
    @FXML
    private TableColumn<Adopter, String> AdopterIdColumn;
    @FXML
    private TableColumn<Adopter, String> FnameColumn;
    @FXML
    private TableColumn<Adopter, String> LnameColumn;
    @FXML
    private TableColumn<Adopter, String> AddressColumn;
    @FXML
    private TableColumn<Adopter, String> SocialStatusColumn;

    @FXML
    private TableColumn<Adopter, String> OccupationColmun;
    @FXML
    private TableColumn<Adopter, Integer> NoPetsColmun;
    @FXML
    private TableColumn<Adopter, Integer> NoChildrenColumn;
    @FXML
    private Button addAdopterButton;
    @FXML
    private Button removeAdopterButton;
    @FXML
    private Button editAdopterButton;

    private ObservableList<Adopter> adopterList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adopterList = FXCollections.observableArrayList();

        // Initialize the table columns
        AdopterIdColumn.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        FnameColumn.setCellValueFactory(new PropertyValueFactory<>("fName"));
        LnameColumn.setCellValueFactory(new PropertyValueFactory<>("lName"));
        AddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        SocialStatusColumn.setCellValueFactory(new PropertyValueFactory<>("socialStatus"));

        OccupationColmun.setCellValueFactory(new PropertyValueFactory<>("occupation"));
        NoPetsColmun.setCellValueFactory(new PropertyValueFactory<>("numberOfPetsOwned"));
        NoChildrenColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfChildren"));

        loadAdopterData();
    }

    private void loadAdopterData() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";
        String query = "SELECT "
                + "p.SSN, p.Password, p.FName, p.LName, p.Address, p.Social_Status, p.Email, p.Phone_Number, "
                + "p.Date_of_Birth, p.Gender, a.Occupation, a.Number_of_Pets_Owned, a.Number_of_Children, a.Yearly_Income "
                + "FROM Person p "
                + "JOIN Adopter a ON p.SSN = a.SSN";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            adopterList.clear();
            while (rs.next()) {
                Adopter adopter = new Adopter(
                        rs.getString("SSN"), // SSN (from Person)
                        rs.getString("Password"), // Password (from Person)
                        rs.getString("Gender"), // Gender (from Person)
                        rs.getString("FName"), // First Name (from Person)
                        rs.getString("LName"), // Last Name (from Person)
                        rs.getString("Address"), // Address (from Person)
                        rs.getString("Social_Status"), // Social Status (from Person)
                        rs.getString("Email"), // Email (from Person)
                        rs.getString("Phone_Number"), // Phone Number (from Person)
                        rs.getDate("Date_of_Birth").toLocalDate(), // Date of Birth (from Person)
                        rs.getString("Occupation"), // Occupation (from Adopter)
                        rs.getInt("Number_of_Pets_Owned"), // Number of Pets (from Adopter)
                        rs.getInt("Number_of_Children"), // Number of Children (from Adopter)
                        rs.getDouble("Yearly_Income") // Yearly Income (from Adopter)
                );
                adopterList.add(adopter);
            }
            AdoptersTable.setItems(adopterList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addAdopter(ActionEvent event) {
//        try {
//            // Load the "AddAdopterPopup.fxml" file
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAdopterPopup.fxml"));
//            AnchorPane popup = loader.load();
//            AddAdopterPopupController controller = loader.getController();
//
//            // Create a new stage for the popup
//            Stage popupStage = new Stage();
//            popupStage.setTitle("Add New Adopter");
//
//            // Set the scene with the loaded FXML
//            Scene scene = new Scene(popup);
//            popupStage.setScene(scene);
//
//            // Optional: Set the modality to block input to other windows
//            popupStage.initModality(Modality.APPLICATION_MODAL);
//
//            // Show the popup and wait for it to close
//            popupStage.showAndWait();
//
//            // Refresh the adopter list after adding a new adopter
//            loadAdopterData();  // You need to implement this method to reload the data into the table
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    @FXML
    private void removeAdopter(ActionEvent event) {
//        Adopter selectedAdopter = AdoptersTable.getSelectionModel().getSelectedItem();
//        if (selectedAdopter != null) {
//            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this adopter?", "Delete Adopter", JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                try {
//                    String url = "jdbc:postgresql://localhost:5432/postgres";
//                    String user = "postgres";
//                    String password = "yourpassword";
//                    String deleteQuery = "DELETE FROM Adopter WHERE adopter_id = ?";
//
//                    try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
//                        pstmt.setString(1, selectedAdopter.getAdopterId());
//                        pstmt.executeUpdate();
//                        adopterList.remove(selectedAdopter);
//                        loadAdopterData();
//                        JOptionPane.showMessageDialog(null, "Adopter deleted successfully.");
//                    }
//
//                } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null, "Error deleting adopter: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @FXML
    private void editAdopter(ActionEvent event) {
//        // Get the selected adopter from the table
//        Adopter selectedAdopter = (Adopter) AdoptersTable.getSelectionModel().getSelectedItem();
//
//        // Check if an adopter is selected
//        if (selectedAdopter == null) {
//            JOptionPane.showMessageDialog(null, "Please select an adopter to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        try {
//            // Load the "EditAdopterPopup.fxml" file
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditAdopterPopup.fxml"));
//            AnchorPane popup = loader.load();
//            EditAdopterPopupController controller = loader.getController();
//            controller.setAdopter(selectedAdopter);  // Set the selected adopter to be edited
//
//            // Create a new stage for the popup
//            Stage popupStage = new Stage();
//            popupStage.setTitle("Edit Adopter");
//
//            // Set the scene with the loaded FXML
//            Scene scene = new Scene(popup);
//            popupStage.setScene(scene);
//
//            // Optional: Set the modality to block input to other windows
//            popupStage.initModality(Modality.APPLICATION_MODAL);
//
//            // Show the popup and wait for it to close
//            popupStage.showAndWait();
//
//            // Refresh the adopter list after editing
//            loadAdopterData();  // You need to implement this method to reload the data into the table
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    @FXML
    private void generateReport() {
        // Code to generate a report for adopters
    }
}
