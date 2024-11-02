/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.MedicalRecord;
import com.mycompany.foreverhomedbmsproject.Server.MedicalRecordItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MedicalRecordItemController {

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


    public void setRecordData(MedicalRecord record) {
        // Set data to fields
        animalIDField.setText(String.valueOf(record.getAnimalId()));
        animalNameField.setText(record.getAnimalName());
        animalGenderField.setText(record.getAnimalGender());
        animalAgeField.setText(String.valueOf(record.getAnimalAge()));
        clinicNameLabel.setText(record.getClinicName());

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

    }




