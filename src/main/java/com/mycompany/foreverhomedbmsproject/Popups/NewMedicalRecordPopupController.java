package com.mycompany.foreverhomedbmsproject.Popups;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class NewMedicalRecordPopupController implements Initializable {

    @FXML
    private TextField animalIdField;
    @FXML
    private TextField clinicNameField;
    @FXML
    private TextField illnessField;
    @FXML
    private TextField vaccinationField;
    @FXML
    private TextField treatmentField;
    @FXML
    private TextArea noteArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization if necessary
    }

    @FXML
    private void addButton() {
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        if (animalIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Please fill the animal ID field!",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int animalId = Integer.parseInt(animalIdField.getText().trim());
        String clinicName = clinicNameField.getText().trim();
        String illness = illnessField.getText().trim();
        String vaccination = vaccinationField.getText().trim();
        String treatment = treatmentField.getText().trim();
        String note = noteArea.getText().trim();

        if (illness.isEmpty() || vaccination.isEmpty() || treatment.isEmpty() || note.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Please fill in all fields: Illness, Vaccination, Treatment, and Note.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String checkQuery = "SELECT COUNT(*) FROM Medical_Record WHERE Animal_ID = ?";
        String insertMedicalRecordQuery = "INSERT INTO Medical_Record (Animal_ID, Clinic_Name) VALUES (?, ?) RETURNING Record_ID";
        String insertIllnessQuery = "INSERT INTO Illness_Record (Record_ID, Illness) VALUES (?, ?)";
        String insertVaccinationQuery = "INSERT INTO Vaccination_Record (Record_ID, Vaccination) VALUES (?, ?)";
        String insertTreatmentQuery = "INSERT INTO Treatment_Record (Record_ID, Treatment) VALUES (?, ?)";
        String insertNoteQuery = "INSERT INTO Note_Record (Record_ID, Note) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
            // Check if Animal_ID already has a Medical Record
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, animalId);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    // Record exists, show error message
                    JOptionPane.showMessageDialog(null, "This animal already has a medical record.");
                    return;
                }
            }

            // Insert into Medical_Record table
            int recordId;
            try (PreparedStatement insertMedicalStmt = conn.prepareStatement(insertMedicalRecordQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertMedicalStmt.setInt(1, animalId);
                insertMedicalStmt.setString(2, clinicName);
                insertMedicalStmt.executeUpdate();
                ResultSet generatedKeys = insertMedicalStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    recordId = generatedKeys.getInt(1);
                } else {
                    throw new Exception("Failed to retrieve Record_ID for new medical record.");
                }
            }

            // Insert into other tables
            try (PreparedStatement insertIllnessStmt = conn.prepareStatement(insertIllnessQuery)) {
                insertIllnessStmt.setInt(1, recordId);
                insertIllnessStmt.setString(2, illness);
                insertIllnessStmt.executeUpdate();
            }

            try (PreparedStatement insertVaccinationStmt = conn.prepareStatement(insertVaccinationQuery)) {
                insertVaccinationStmt.setInt(1, recordId);
                insertVaccinationStmt.setString(2, vaccination);
                insertVaccinationStmt.executeUpdate();
            }

            try (PreparedStatement insertTreatmentStmt = conn.prepareStatement(insertTreatmentQuery)) {
                insertTreatmentStmt.setInt(1, recordId);
                insertTreatmentStmt.setString(2, treatment);
                insertTreatmentStmt.executeUpdate();
            }

            try (PreparedStatement insertNoteStmt = conn.prepareStatement(insertNoteQuery)) {
                insertNoteStmt.setInt(1, recordId);
                insertNoteStmt.setString(2, note);
                insertNoteStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "New medical record added successfully!");
            ((javafx.stage.Stage) animalIdField.getScene().getWindow()).close();


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding record: " + e.getMessage());
        }
    }

}
