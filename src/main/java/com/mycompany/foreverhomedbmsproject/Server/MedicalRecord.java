/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject.Server;


import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private int recordId;
    private int animalId;
    private String animalName;
    private String animalGender;
    private int animalAge;
    private String clinicName;

    private List<VaccinationRecord> vaccinationRecords;
    private List<TreatmentRecord> treatmentRecords;
    private List<IllnessRecord> illnessRecords;
    private List<NoteRecord> noteRecords;

    public MedicalRecord(int recordId, int animalId, String clinicName) {
        this.recordId = recordId;
        this.animalId = animalId;
        this.clinicName = clinicName;

        this.vaccinationRecords = new ArrayList<>();
        this.treatmentRecords = new ArrayList<>();
        this.illnessRecords = new ArrayList<>();
        this.noteRecords = new ArrayList<>();
    }

    // Add methods to manage records
    public void addVaccinationRecord(String vaccination) {
        vaccinationRecords.add(new VaccinationRecord(recordId, vaccination));
    }

    public void addTreatmentRecord(String treatment) {
        treatmentRecords.add(new TreatmentRecord(recordId, treatment));
    }

    public void addIllnessRecord(String illness) {
        illnessRecords.add(new IllnessRecord(recordId, illness));
    }

    public void addNoteRecord(String note) {
        noteRecords.add(new NoteRecord(recordId, note));
    }

    // Getters
    public int getRecordId() { return recordId; }
    public int getAnimalId() { return animalId; }
    public String getAnimalName() { return animalName; }
    public String getAnimalGender() { return animalGender; }
    public int getAnimalAge() { return animalAge; }
    public String getClinicName() { return clinicName; }
    public List<VaccinationRecord> getVaccinationRecords() { return vaccinationRecords; }
    public List<TreatmentRecord> getTreatmentRecords() { return treatmentRecords; }
    public List<IllnessRecord> getIllnessRecords() { return illnessRecords; }
    public List<NoteRecord> getNoteRecords() { return noteRecords; }

    // Setters
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public void setAnimalGender(String animalGender) {
        this.animalGender = animalGender;
    }

    public void setAnimalAge(int animalAge) {
        this.animalAge = animalAge;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public void setVaccinationRecords(List<VaccinationRecord> vaccinationRecords) {
        this.vaccinationRecords = vaccinationRecords;
    }

    public void setTreatmentRecords(List<TreatmentRecord> treatmentRecords) {
        this.treatmentRecords = treatmentRecords;
    }

    public void setIllnessRecords(List<IllnessRecord> illnessRecords) {
        this.illnessRecords = illnessRecords;
    }

    public void setNoteRecords(List<NoteRecord> noteRecords) {
        this.noteRecords = noteRecords;
    }

    public static class VaccinationRecord {
        private int recordId;
        private String vaccination;

        public VaccinationRecord(int recordId, String vaccination) {
            this.recordId = recordId;
            this.vaccination = vaccination;
        }

        public int getRecordId() { return recordId; }
        public String getVaccination() { return vaccination; }
    }

    public static class TreatmentRecord {
        private int recordId;
        private String treatment;

        public TreatmentRecord(int recordId, String treatment) {
            this.recordId = recordId;
            this.treatment = treatment;
        }

        public int getRecordId() { return recordId; }
        public String getTreatment() { return treatment; }
    }

    public static class IllnessRecord {
        private int recordId;
        private String illness;

        public IllnessRecord(int recordId, String illness) {
            this.recordId = recordId;
            this.illness = illness;
        }

        public int getRecordId() { return recordId; }
        public String getIllness() { return illness; }
    }

    public static class NoteRecord {
        private int recordId;
        private String note;

        public NoteRecord(int recordId, String note) {
            this.recordId = recordId;
            this.note = note;
        }

        public int getRecordId() { return recordId; }
        public String getNote() { return note; }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Medical Record for Animal ID: ").append(animalId).append("\n");
        sb.append("Clinic: ").append(clinicName).append("\n");

        sb.append("Vaccinations:\n");
        for (VaccinationRecord record : vaccinationRecords) {
            sb.append(" - ").append(record.getVaccination()).append("\n");
        }

        sb.append("Treatments:\n");
        for (TreatmentRecord record : treatmentRecords) {
            sb.append(" - ").append(record.getTreatment()).append("\n");
        }

        sb.append("Illnesses:\n");
        for (IllnessRecord record : illnessRecords) {
            sb.append(" - ").append(record.getIllness()).append("\n");
        }

        sb.append("Notes:\n");
        for (NoteRecord record : noteRecords) {
            sb.append(" - ").append(record.getNote()).append("\n");
        }

        return sb.toString();
    }
}
