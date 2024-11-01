/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;
import javafx.scene.control.Label;

import com.mycompany.foreverhomedbmsproject.Server.Feedback;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


/**
 * FXML Controller class
 *
 * @author lenovo
 */
public class FeedbackItemController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label titleOfFeedback;
    @FXML
    private TextArea feedbackText;
    @FXML
    private Label authorName;
    @FXML
    private Label dateOfFeedback;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
        public void setFeedback(Feedback feedback) {
        titleOfFeedback.setText(feedback.getFeedbackTitle());
        feedbackText.setText(feedback.getFeedbackText());
        authorName.setText(feedback.getFeedbackAuthor());
        dateOfFeedback.setText(feedback.getFeedbackDate().toString());
    }
    
}
