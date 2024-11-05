package com.mycompany.foreverhomedbmsproject.Popups;

import com.mycompany.foreverhomedbmsproject.Server.MedicalRecord;
import com.mycompany.foreverhomedbmsproject.Server.MedicalRecordItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

public class EditMedicalRecordPopupController implements Initializable {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "ahm@212005";

    private MedicalRecord record;

    @FXML
    private TableView<MedicalRecordItem> medicalRecordsTable;
    @FXML
    private TableColumn<MedicalRecordItem, String> vaccinationTable;
    @FXML
    private TableColumn<MedicalRecordItem, String> treatmentTable;
    @FXML
    private TableColumn<MedicalRecordItem, String> illnessTable;
    @FXML
    private TableColumn<MedicalRecordItem, String> noteTable;
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
        vaccinationTable.setCellValueFactory(new PropertyValueFactory<>("vaccination"));
        treatmentTable.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        illnessTable.setCellValueFactory(new PropertyValueFactory<>("illness"));
        noteTable.setCellValueFactory(new PropertyValueFactory<>("note"));
    }

    public void setRecord(MedicalRecord record) {
        this.record = record;
        clinicNameField.setText(record.getClinicName());

        ObservableList<MedicalRecordItem> items = FXCollections.observableArrayList();

        int maxRecords = Math.max(
                Math.max(record.getVaccinationRecords().size(), record.getTreatmentRecords().size()),
                Math.max(record.getIllnessRecords().size(), record.getNoteRecords().size())
        );

        for (int i = 0; i < maxRecords; i++) {
            String vaccination = i < record.getVaccinationRecords().size()
                    ? record.getVaccinationRecords().get(i).getVaccination() : null;
            String treatment = i < record.getTreatmentRecords().size()
                    ? record.getTreatmentRecords().get(i).getTreatment() : null;
            String illness = i < record.getIllnessRecords().size()
                    ? record.getIllnessRecords().get(i).getIllness() : null;
            String note = i < record.getNoteRecords().size()
                    ? record.getNoteRecords().get(i).getNote() : null;

            items.add(new MedicalRecordItem(vaccination, treatment, illness, note));
        }

        medicalRecordsTable.setItems(items);
    }

    // Method to add a new row to the database and table view
    @FXML
    private void addRow() {
        String vaccination = vaccinationField.getText();
        String treatment = treatmentField.getText();
        String illness = illnessField.getText();
        String note = noteArea.getText();

        if (vaccination.isEmpty() || treatment.isEmpty() || illness.isEmpty() || note.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String insertVaccination = "INSERT INTO Vaccination_Record (Record_ID, Vaccination) VALUES (?, ?)";
            String insertTreatment = "INSERT INTO Treatment_Record (Record_ID, Treatment) VALUES (?, ?)";
            String insertIllness = "INSERT INTO Illness_Record (Record_ID, Illness) VALUES (?, ?)";
            String insertNote = "INSERT INTO Note_Record (Record_ID, Note) VALUES (?, ?)";

            try (PreparedStatement pstmtVaccination = conn.prepareStatement(insertVaccination);
                 PreparedStatement pstmtTreatment = conn.prepareStatement(insertTreatment);
                 PreparedStatement pstmtIllness = conn.prepareStatement(insertIllness);
                 PreparedStatement pstmtNote = conn.prepareStatement(insertNote)) {

                pstmtVaccination.setInt(1, record.getRecordId());
                pstmtVaccination.setString(2, vaccination);
                pstmtVaccination.executeUpdate();

                pstmtTreatment.setInt(1, record.getRecordId());
                pstmtTreatment.setString(2, treatment);
                pstmtTreatment.executeUpdate();

                pstmtIllness.setInt(1, record.getRecordId());
                pstmtIllness.setString(2, illness);
                pstmtIllness.executeUpdate();

                pstmtNote.setInt(1, record.getRecordId());
                pstmtNote.setString(2, note);
                pstmtNote.executeUpdate();

                medicalRecordsTable.getItems().add(new MedicalRecordItem(vaccination, treatment, illness, note));
                JOptionPane.showMessageDialog(null, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to delete the selected row from the database and table view
    @FXML
    private void removeSelectedRow() {
        MedicalRecordItem selectedItem = medicalRecordsTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(null, "No row selected.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String deleteVaccination = "DELETE FROM Vaccination_Record WHERE Record_ID = ? AND Vaccination = ?";
            String deleteTreatment = "DELETE FROM Treatment_Record WHERE Record_ID = ? AND Treatment = ?";
            String deleteIllness = "DELETE FROM Illness_Record WHERE Record_ID = ? AND Illness = ?";
            String deleteNote = "DELETE FROM Note_Record WHERE Record_ID = ? AND Note = ?";

            try (PreparedStatement pstmtVaccination = conn.prepareStatement(deleteVaccination);
                 PreparedStatement pstmtTreatment = conn.prepareStatement(deleteTreatment);
                 PreparedStatement pstmtIllness = conn.prepareStatement(deleteIllness);
                 PreparedStatement pstmtNote = conn.prepareStatement(deleteNote)) {

                pstmtVaccination.setInt(1, record.getRecordId());
                pstmtVaccination.setString(2, selectedItem.getVaccination());
                pstmtVaccination.executeUpdate();

                pstmtTreatment.setInt(1, record.getRecordId());
                pstmtTreatment.setString(2, selectedItem.getTreatment());
                pstmtTreatment.executeUpdate();

                pstmtIllness.setInt(1, record.getRecordId());
                pstmtIllness.setString(2, selectedItem.getIllness());
                pstmtIllness.executeUpdate();

                pstmtNote.setInt(1, record.getRecordId());
                pstmtNote.setString(2, selectedItem.getNote());
                pstmtNote.executeUpdate();

                medicalRecordsTable.getItems().remove(selectedItem);
                JOptionPane.showMessageDialog(null, "Record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to edit the clinic name in the database
    @FXML
    private void editClinicName() {
        if (record == null) {
            JOptionPane.showMessageDialog(null, "No medical record loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newClinicName = clinicNameField.getText();
        if (newClinicName.equals(record.getClinicName())) {
            JOptionPane.showMessageDialog(null, "Clinic name is unchanged.", "No Change", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String updateClinicName = "UPDATE Medical_Record SET Clinic_Name = ? WHERE Record_ID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(updateClinicName)) {
            pstmt.setString(1, newClinicName);
            pstmt.setInt(2, record.getRecordId());
            pstmt.executeUpdate();

            record.setClinicName(newClinicName);
            JOptionPane.showMessageDialog(null, "Clinic name updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update clinic name: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public MedicalRecord getRecord() {
        return record;
    }
}
