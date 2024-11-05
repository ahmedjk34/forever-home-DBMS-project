package com.mycompany.foreverhomedbmsproject.Popups;

import com.mycompany.foreverhomedbmsproject.Server.Donation;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DonationsPopupController implements Initializable {

    private int eventId;

    @FXML
    private TableView<Donation> donationsTable;
    @FXML
    private TableColumn<Donation, Integer> colDonationId;
    @FXML
    private TableColumn<Donation, String> colDonerName;
    @FXML
    private TableColumn<Donation, String> colDonationType;
    @FXML
    private TableColumn<Donation, LocalDate> colDonationDate;

    @FXML
    private TableColumn<Donation, String> colPurpose;
    @FXML
    private TableColumn<Donation, Double> colAmount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize table columns
        colDonationId.setCellValueFactory(new PropertyValueFactory<>("donationId"));
        colDonerName.setCellValueFactory(new PropertyValueFactory<>("donerName"));
        colDonationType.setCellValueFactory(new PropertyValueFactory<>("donationType"));
        colDonationType.setCellValueFactory(new PropertyValueFactory<>("dateOfDonation"));
        colPurpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Load donations based on eventId
        getEventDonations();
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
        getEventDonations(); // Refresh donations when eventId is set
    }

    public int getEventId() {
        return eventId;
    }

    private void getEventDonations() {
        ObservableList<Donation> donations = FXCollections.observableArrayList();
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";
        String query = "SELECT Donation_ID, Doner_Name, Donation_Type, Purpose, Date_of_Donation, Amount, Event_ID FROM Donation WHERE Event_ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Donation donation = new Donation(
                        rs.getInt("Donation_ID"),
                        rs.getString("Doner_Name"),
                        rs.getString("Donation_Type"),
                        rs.getString("Purpose"),
                        rs.getDate("Date_of_Donation").toLocalDate(),
                        rs.getDouble("Amount"),
                        rs.getInt("Event_ID")
                );
                donations.add(donation);
            }
            donationsTable.setItems(donations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    @FXML
    private void showAddDonationPopup() {
        try {
            // Load the FXML for the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewDonationPopup.fxml"));
            AnchorPane popup = loader.load();
            NewDonationPopupController controller = loader.getController();
            controller.setEventId(eventId);

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Add New Donation");

            // Set the scene with the loaded FXML
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);

            // Optional: Set the modality to block input to other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to close
            popupStage.showAndWait();

            getEventDonations();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
