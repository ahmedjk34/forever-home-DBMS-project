/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Animal;
import com.mycompany.foreverhomedbmsproject.Server.Feedback;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author lenovo
 */
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackExplorerController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    private List<Feedback> feedbackList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getFeedbacks();
        populateScrollPane();
    }

    private void getFeedbacks() {
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        String query = "SELECT f.Feedback_ID, p.FName, p.LName, f.Feedback_Title, f.Feedback_Text, f.Feedback_Date " +
                       "FROM Feedback f " +
                       "JOIN Person p ON f.SSN = p.SSN";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String fullName = rs.getString("FName") + " " + rs.getString("LName");
                String feedbackTitle = rs.getString("Feedback_Title");
                String feedbackText = rs.getString("Feedback_Text");
                Date feedbackDate = rs.getDate("Feedback_Date");

                Feedback currentFeedback = new Feedback(
                    rs.getInt("Feedback_ID"),
                    fullName,
                    feedbackTitle,
                    feedbackText,
                    feedbackDate
                );
                feedbackList.add(currentFeedback); // Add to the ArrayList
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateScrollPane() {
        VBox vBox = new VBox();
        for (Feedback feedback : feedbackList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FeedbackItem.fxml"));
                AnchorPane feedbackPane = loader.load();

                // Get controller for the loaded item
                FeedbackItemController itemController = loader.getController();
                itemController.setFeedback(feedback);

                vBox.getChildren().add(feedbackPane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        scrollPane.setContent(vBox);
    }
}
