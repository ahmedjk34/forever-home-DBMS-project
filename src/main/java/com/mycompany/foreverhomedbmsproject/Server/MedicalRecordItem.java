/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject.Server;

/**
 *
 * @author lenovo
 */

public class MedicalRecordItem {
    private String vaccination;
    private String treatment;
    private String illness;
    private String note;

    public MedicalRecordItem(String vaccination, String treatment, String illness, String note) {
        this.vaccination = vaccination;
        this.treatment = treatment;
        this.illness = illness;
        this.note = note;
    }

    // Getters and setters
    public String getVaccination() { return vaccination; }
    public void setVaccination(String vaccination) { this.vaccination = vaccination; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getIllness() { return illness; }
    public void setIllness(String illness) { this.illness = illness; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
