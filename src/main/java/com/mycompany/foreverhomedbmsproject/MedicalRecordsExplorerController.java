package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Popups.NewMedicalRecordPopupController;
import com.mycompany.foreverhomedbmsproject.Popups.RemoveMedicalRecordPopupController;
import com.mycompany.foreverhomedbmsproject.Server.MedicalRecord;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MedicalRecordsExplorerController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox recordsContainer;  // VBox to hold individual record items

    private final List<MedicalRecord> medicalRecordsList = new ArrayList<>();

    // Database credentials and URL
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "ahm@212005";

    // ID of the specific animal to retrieve medical records for
    private int specificAnimalId = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getMedicalRecords();
    }

    private void getMedicalRecords() {

        medicalRecordsList.clear();

        String query = "SELECT m.Record_ID, a.Animal_ID, a.animal_image ,m.Clinic_Name, a.name, a.gender, "
                + "vr.Vaccination, tr.Treatment, ir.Illness, nr.Note, "
                + "DATE_PART('year', AGE(a.Date_of_Birth))::text AS Age "
                + "FROM Medical_Record m "
                + "JOIN Animal a ON m.Animal_ID = a.Animal_ID "
                + "LEFT JOIN Vaccination_Record vr ON m.Record_ID = vr.Record_ID "
                + "LEFT JOIN Treatment_Record tr ON m.Record_ID = tr.Record_ID "
                + "LEFT JOIN Illness_Record ir ON m.Record_ID = ir.Record_ID "
                + "LEFT JOIN Note_Record nr ON m.Record_ID = nr.Record_ID "
                + "WHERE a.Animal_ID = m.Animal_ID";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

//        stmt.setInt(1, specificAnimalId);
            try (ResultSet rs = stmt.executeQuery()) {
                MedicalRecord currentRecord = null;

                while (rs.next()) {
                    int recordId = rs.getInt("Record_ID");
                    int animalId = rs.getInt("Animal_ID");
                    String clinicName = rs.getString("Clinic_Name");
                    String animalName = rs.getString("name");
                    String animalGender = rs.getString("gender");
                    String animalAge = rs.getString("Age");
                    String animalImage = rs.getString("animal_image");

                    // Check if we are processing a new medical record
                    if (currentRecord == null || currentRecord.getRecordId() != recordId) {
                        // Create a new MedicalRecord instance and add it to the list
                        currentRecord = new MedicalRecord(recordId, animalId, clinicName, animalName, animalGender, animalAge, animalImage);
                        medicalRecordsList.add(currentRecord);
                    }

                    // Add vaccination if it's not already in the list
                    String vaccination = rs.getString("Vaccination");
                    if (vaccination != null && currentRecord.getVaccinationRecords().stream()
                            .noneMatch(v -> v.getVaccination().equals(vaccination))) {
                        currentRecord.addVaccinationRecord(vaccination);
                    }

                    // Add treatment if it's not already in the list
                    String treatment = rs.getString("Treatment");
                    if (treatment != null && currentRecord.getTreatmentRecords().stream()
                            .noneMatch(t -> t.getTreatment().equals(treatment))) {
                        currentRecord.addTreatmentRecord(treatment);
                    }

                    // Add illness if it's not already in the list
                    String illness = rs.getString("Illness");
                    if (illness != null && currentRecord.getIllnessRecords().stream()
                            .noneMatch(i -> i.getIllness().equals(illness))) {
                        currentRecord.addIllnessRecord(illness);
                    }

                    // Add note if it's not already in the list
                    String note = rs.getString("Note");
                    if (note != null && currentRecord.getNoteRecords().stream()
                            .noneMatch(n -> n.getNote().equals(note))) {
                        currentRecord.addNoteRecord(note);
                    }
                }
            }

            displayMedicalRecords();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayMedicalRecords() {
        for (MedicalRecord record : medicalRecordsList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MedicalRecordItem.fxml"));
                Node recordNode = loader.load();

                MedicalRecordItemController itemController = loader.getController();
                itemController.setRecordData(record);

                recordsContainer.getChildren().add(recordNode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addMedicalRecordClick() {
        try {
            // Load the FXML for the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/NewMedicalRecordPopup.fxml"));
            AnchorPane popup = loader.load();
            NewMedicalRecordPopupController controller = loader.getController();

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Add a New Medical Record");

            // Set the scene with the loaded FXML
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);

            // Optional: Set the modality to block input to other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to close
            popupStage.showAndWait();

            recordsContainer.getChildren().clear();

            getMedicalRecords();
        } catch (IOException ex) {
            Logger.getLogger(MedicalRecordsExplorerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void deleteMedicalRecordClick() {
        try {
            // Load the FXML for the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/RemoveMedicalRecordPopup.fxml"));
            AnchorPane popup = loader.load();
            RemoveMedicalRecordPopupController controller = loader.getController();

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Delete a Medical Record");

            // Set the scene with the loaded FXML
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);

            // Optional: Set the modality to block input to other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to close
            popupStage.showAndWait();

            recordsContainer.getChildren().clear();
            getMedicalRecords();
        } catch (IOException ex) {
            Logger.getLogger(MedicalRecordsExplorerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
