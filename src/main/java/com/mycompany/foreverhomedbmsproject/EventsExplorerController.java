/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Popups.DonationsPopupController;
import com.mycompany.foreverhomedbmsproject.Server.Event;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

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
    private void showDonationsPopup() {
        // Get the selected Event from the eventTable
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            try {
                // Load the FXML for the popup
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/DonationsPopup.fxml"));
                AnchorPane popup = loader.load();

                // Get the controller for the popup and set the event ID
                DonationsPopupController controller = loader.getController();
                controller.setEventId(selectedEvent.getEventId());

                // Create a new stage for the popup
                Stage popupStage = new Stage();
                popupStage.setTitle("Event Donations");

                // Set the scene with the loaded FXML
                Scene scene = new Scene(popup);
                popupStage.setScene(scene);

                // Optional: Set the modality to block input to other windows
                popupStage.initModality(Modality.APPLICATION_MODAL);

                // Show the popup and wait for it to close
                popupStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Display an error message using JOptionPane
            JOptionPane.showMessageDialog(null, "No event selected. Please select an event to view donations.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @FXML
    private void generateReport() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Load the .jrxml file
            InputStream inp = new FileInputStream(new File("EventsReport.jrxml"));

            // Compile the report
            JasperDesign jd = JRXmlLoader.load(inp);
            JasperReport jr = JasperCompileManager.compileReport(jd);

            // Fill the report with data
            JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);

            // Display the report in a JFrame
            JFrame frame = new JFrame("Events Report");
            frame.getContentPane().add(new JRViewer(jp));
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
