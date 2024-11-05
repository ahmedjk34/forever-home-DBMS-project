/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Event;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author lenovo
 */
public class EventsExplorerController implements Initializable {

    private String userType;

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, Integer> eventIdColumn;

    @FXML
    private TableColumn<Event, String> eventNameColumn;

    @FXML
    private TableColumn<Event, LocalDate> dateOfEventColumn;

    @FXML
    private TableColumn<Event, LocalTime> timeOfEventColumn;

    @FXML
    private TableColumn<Event, String> locationColumn;

    @FXML
    private TableColumn<Event, Double> fundingGoalColumn;

    private ObservableList<Event> eventsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up the table columns
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        dateOfEventColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfEvent"));
        timeOfEventColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfEvent"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        fundingGoalColumn.setCellValueFactory(new PropertyValueFactory<>("fundingGoal"));

        // Load data from the database
        getEvents();
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    private void getEvents() {
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        String query = "SELECT Event_ID, Event_Name, Date_of_The_Event, Time_of_The_Event, Location, Funding_Goal FROM Event";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("Event_ID"),
                        rs.getString("Event_Name"),
                        rs.getDate("Date_of_The_Event").toLocalDate(),
                        rs.getTime("Time_of_The_Event").toLocalTime(),
                        rs.getString("Location"),
                        rs.getDouble("Funding_Goal")
                );
                eventsList.add(event);
            }

            eventTable.setItems(eventsList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void generateReport() {
    }
}
