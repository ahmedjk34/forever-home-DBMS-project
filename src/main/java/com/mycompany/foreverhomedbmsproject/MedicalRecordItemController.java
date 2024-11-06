/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Popups.EditMedicalRecordPopupController;
import com.mycompany.foreverhomedbmsproject.Server.MedicalRecord;
import com.mycompany.foreverhomedbmsproject.Server.MedicalRecordItem;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MedicalRecordItemController {

    @FXML
    private ImageView animalImage;

    @FXML
    private TextField animalIDField;

    @FXML
    private TextField animalNameField;

    @FXML
    private TextField animalGenderField;

    @FXML
    private TextField animalAgeField;

    @FXML
    private Label clinicNameLabel;

    private MedicalRecordsExplorerController parentController;

    // TableView to hold medical record data
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

    private MedicalRecord currentRecord;

    public void setRecordData(MedicalRecord record) {
        currentRecord = record;
        animalIDField.setText(String.valueOf(record.getAnimalId()));
        animalNameField.setText(record.getAnimalName());
        animalGenderField.setText(record.getAnimalGender());
        animalAgeField.setText(String.valueOf(record.getAnimalAge()));
        clinicNameLabel.setText(record.getClinicName());

        String imagePath = record.getAnimalImage();
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        animalImage.setImage(image);

        // Get the max size of the lists to avoid index out-of-bounds errors
        int maxRecords = Math.max(
                Math.max(record.getVaccinationRecords().size(), record.getTreatmentRecords().size()),
                Math.max(record.getIllnessRecords().size(), record.getNoteRecords().size())
        );

        ObservableList<MedicalRecordItem> items = FXCollections.observableArrayList();

        // Loop over the largest list size and fill in data by index
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

        // Setting up table columns
        vaccinationTable.setCellValueFactory(new PropertyValueFactory<>("vaccination"));
        treatmentTable.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        illnessTable.setCellValueFactory(new PropertyValueFactory<>("illness"));
        noteTable.setCellValueFactory(new PropertyValueFactory<>("note"));
    }

    @FXML
    private void editMedicalRecordClick() {
        try {
            // Load the FXML for the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/EditMedicalRecordPopup.fxml"));
            AnchorPane popup = loader.load();
            EditMedicalRecordPopupController controller = loader.getController();
            controller.setRecord(currentRecord);

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Edit a Medical Record");

            // Set the scene with the loaded FXML
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);

            // Optional: Set the modality to block input to other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to close
            popupStage.showAndWait();

            parentController.getMedicalRecords();  // Fetch updated records and refresh UI

        } catch (IOException ex) {
            Logger.getLogger(MedicalRecordsExplorerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setParentController(MedicalRecordsExplorerController parentController) {
        this.parentController = parentController;
    }

}
