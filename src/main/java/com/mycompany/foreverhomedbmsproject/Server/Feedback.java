/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject.Server;

import java.sql.Date;

public class Feedback {
    private int feedbackId;      // Corresponds to Feedback_ID
    private String feedbackAuthor; // Corresponds to Feedback_Author
    private String feedbackTitle; // Corresponds to Feedback_Title
    private String feedbackText;  // Corresponds to Feedback_Text
    private Date feedbackDate; // Corresponds to Feedback_Date

    // Constructor
    public Feedback(int feedbackId, String feedbackAuthor, String feedbackTitle, String feedbackText, Date feedbackDate) {
        this.feedbackId = feedbackId;
        this.feedbackAuthor = feedbackAuthor;
        this.feedbackTitle = feedbackTitle;
        this.feedbackText = feedbackText;
        this.feedbackDate = feedbackDate; // Initialize feedback date
    }

    // Getters
    public int getFeedbackId() {
        return feedbackId;
    }

    public String getFeedbackAuthor() {
        return feedbackAuthor;
    }

    public String getFeedbackTitle() {
        return feedbackTitle;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public Date getFeedbackDate() {
        return feedbackDate; // Getter for feedback date
    }

    // Setters
    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public void setFeedbackAuthor(String feedbackAuthor) {
        this.feedbackAuthor = feedbackAuthor;
    }

    public void setFeedbackTitle(String feedbackTitle) {
        this.feedbackTitle = feedbackTitle;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate; 
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", feedbackAuthor='" + feedbackAuthor + '\'' +
                ", feedbackTitle='" + feedbackTitle + '\'' +
                ", feedbackText='" + feedbackText + '\'' +
                ", feedbackDate=" + feedbackDate +
                '}';
    }
}
