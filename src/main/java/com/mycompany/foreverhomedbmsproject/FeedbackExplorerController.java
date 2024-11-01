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
public class FeedbackExplorerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        getFeedbacks();
    }    
    
    private void getFeedbacks(){
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        String query = "SELECT f.Feedback_ID, p.FName, p.LName,f.Feedback_Title ,f.Feedback_Text, f.Feedback_Date " +
                       "FROM Feedback f " +
                       "JOIN Person p ON f.SSN = p.SSN"; // Join to get the author's full name

        try (Connection conn = DriverManager.getConnection(url, user, password);
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
                System.out.println(currentFeedback);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}


