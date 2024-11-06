/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Popups.AddAdopterPopupController;
import com.mycompany.foreverhomedbmsproject.Popups.EditAdopterPopupController;
import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import java.io.File;
import java.io.FileInputStream;

/**
 * FXML Controller class
 *
 * @author lenovo
 */
import java.io.IOException;
import java.io.InputStream;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/AddAdopterPopup.fxml"));
            AnchorPane popup = loader.load();
            AddAdopterPopupController controller = loader.getController();

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Add New Adopter");

            // Set the scene with the loaded FXML
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);

            // Optional: Set the modality to block input to other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to close
            popupStage.showAndWait();

            // Refresh the adopter list after adding a new adopter
            loadAdopterData();  // You need to implement this method to reload the data into the table

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void removeAdopter(ActionEvent event) {
        // Get the selected adopter from the table
        Adopter selectedAdopter = AdoptersTable.getSelectionModel().getSelectedItem();

        // Check if an adopter is selected
        if (selectedAdopter == null) {
            // Show a warning message if no adopter is selected
            JOptionPane.showMessageDialog(null, "Please select an adopter to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the SSN of the selected adopter
        String ssnToDelete = selectedAdopter.getSsn();

        // SQL queries to delete from the Adopter and Person tables
        String deleteAdopterQuery = "DELETE FROM Adopter WHERE SSN = ?";
        String deletePersonQuery = "DELETE FROM Person WHERE SSN = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "ahm@212005")) {
            // Start a transaction to ensure both deletions are handled together
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt1 = conn.prepareStatement(deleteAdopterQuery); PreparedStatement pstmt2 = conn.prepareStatement(deletePersonQuery)) {

                // Set SSN in the delete queries
                pstmt1.setString(1, ssnToDelete);
                pstmt2.setString(1, ssnToDelete);

                // Execute the delete queries
                pstmt1.executeUpdate();
                pstmt2.executeUpdate();

                // Commit the transaction
                conn.commit();

                // Inform the user that the adopter has been removed
                JOptionPane.showMessageDialog(null, "Adopter removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Refresh the adopter list after removal
                loadAdopterData();  // Reload the table data
            } catch (Exception e) {
                // Rollback if there is an error
                conn.rollback();
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error removing adopter.", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Reset auto-commit
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void editAdopter(ActionEvent event) {
        // Get the selected adopter from the table
        Adopter selectedAdopter = (Adopter) AdoptersTable.getSelectionModel().getSelectedItem(); 

        // Check if an adopter is selected
        if (selectedAdopter == null) {
            JOptionPane.showMessageDialog(null, "Please select an adopter to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Load the "EditAdopterPopup.fxml" file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/EditAdopterPopup.fxml"));
            AnchorPane popup = loader.load();
            EditAdopterPopupController controller = loader.getController();
            controller.setAdopter(selectedAdopter);  // Set the selected adopter to be edited

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Edit Adopter");

            // Set the scene with the loaded FXML
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);

            // Optional: Set the modality to block input to other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to close
            popupStage.showAndWait();

            // Refresh the adopter list after editing
            loadAdopterData();  // You need to implement this method to reload the data into the table

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

      @FXML
    private void generateReport() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Load the .jrxml file
            InputStream inp = new FileInputStream(new File("AdoptersReport.jrxml"));

            // Compile the report
            JasperDesign jd = JRXmlLoader.load(inp);
            JasperReport jr = JasperCompileManager.compileReport(jd);

            // Fill the report with data
            JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);

            // Display the report in a JFrame
            JFrame frame = new JFrame("Staff Report");
            frame.getContentPane().add(new JRViewer(jp));
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
