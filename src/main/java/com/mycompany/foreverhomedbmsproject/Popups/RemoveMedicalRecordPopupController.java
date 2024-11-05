package com.mycompany.foreverhomedbmsproject.Popups;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class RemoveMedicalRecordPopupController implements Initializable {

    @FXML
    private TextField animalIdField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code if needed
    }

    @FXML
    private void removeButton() {
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        int animalId;
        try {
            animalId = Integer.parseInt(animalIdField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid Animal ID.");
            return;
        }

        String checkQuery = "SELECT Record_ID FROM Medical_Record WHERE Animal_ID = ?";
        String deleteIllnessQuery = "DELETE FROM Illness_Record WHERE Record_ID = ?";
        String deleteVaccinationQuery = "DELETE FROM Vaccination_Record WHERE Record_ID = ?";
        String deleteTreatmentQuery = "DELETE FROM Treatment_Record WHERE Record_ID = ?";
        String deleteNoteQuery = "DELETE FROM Note_Record WHERE Record_ID = ?";
        String deleteMedicalRecordQuery = "DELETE FROM Medical_Record WHERE Record_ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
            int recordId;

            // Check if the medical record exists for the given Animal_ID
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, animalId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    recordId = rs.getInt("Record_ID");
                } else {
                    JOptionPane.showMessageDialog(null, "No medical record found for the provided Animal ID.");
                    return;
                }
            }

            // Delete related records from associated tables
            try (PreparedStatement deleteIllnessStmt = conn.prepareStatement(deleteIllnessQuery)) {
                deleteIllnessStmt.setInt(1, recordId);
                deleteIllnessStmt.executeUpdate();
            }

            try (PreparedStatement deleteVaccinationStmt = conn.prepareStatement(deleteVaccinationQuery)) {
                deleteVaccinationStmt.setInt(1, recordId);
                deleteVaccinationStmt.executeUpdate();
            }

            try (PreparedStatement deleteTreatmentStmt = conn.prepareStatement(deleteTreatmentQuery)) {
                deleteTreatmentStmt.setInt(1, recordId);
                deleteTreatmentStmt.executeUpdate();
            }

            try (PreparedStatement deleteNoteStmt = conn.prepareStatement(deleteNoteQuery)) {
                deleteNoteStmt.setInt(1, recordId);
                deleteNoteStmt.executeUpdate();
            }

            // Finally, delete from Medical_Record table
            try (PreparedStatement deleteMedicalStmt = conn.prepareStatement(deleteMedicalRecordQuery)) {
                deleteMedicalStmt.setInt(1, recordId);
                deleteMedicalStmt.executeUpdate();
            }

            
            JOptionPane.showMessageDialog(null, "Medical record removed successfully!");
            
            ((javafx.stage.Stage) animalIdField.getScene().getWindow()).close();


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error removing record: " + e.getMessage());
        }
    }
}
