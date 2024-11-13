package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Popups.AddAnimalPopupController;
import com.mycompany.foreverhomedbmsproject.Popups.AnimalPopupController;
import com.mycompany.foreverhomedbmsproject.Popups.EditAnimalPopupController;
import com.mycompany.foreverhomedbmsproject.Server.Animal;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javax.swing.JFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

public class AnimalExplorerController implements Initializable {

    @FXML
    private TableView<Animal> animalTable;

    @FXML
    private TableColumn<Animal, Integer> animalIdColumn;
    @FXML
    private TableColumn<Animal, String> nameColumn;
    @FXML
    private TableColumn<Animal, String> ageColumn;
    @FXML
    private TableColumn<Animal, String> genderColumn;
    @FXML
    private TableColumn<Animal, String> breedColumn;
    @FXML
    private TableColumn<Animal, String> sizeColumn;
    @FXML
    private TableColumn<Animal, String> adoptionStatusColumn;

    @FXML
    private TableColumn<Animal, String> BehaviourColumn;

    @FXML
    private Button addAnimalButton;
    @FXML
    private Button removeAnimalButton;
    @FXML
    private Button editAnimalButton;

    private String userType;

    private String SSN;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        animalIdColumn.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        adoptionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("adoptionStatus"));
        BehaviourColumn.setCellValueFactory(new PropertyValueFactory<>("behaviorDescription"));

        // Set up double-click event handler on table rows
        animalTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();
                if (selectedAnimal != null) {
                    openAnimalPopupView(selectedAnimal);
                }
            }
        });
    }

    private ObservableList<Animal> loadAnimalsByUserType() {
        ObservableList<Animal> animals = FXCollections.observableArrayList();
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        String query;
        if ("Adopter".equalsIgnoreCase(userType)) {
            animalTable.setPrefHeight(550); 

            query = "SELECT a.Animal_ID, a.Name, a.Date_of_Birth, "
                    + "DATE_PART('year', AGE(a.Date_of_Birth))::text AS Age, "
                    + "a.Gender, a.Breed, a.Size, "
                    + "a.Behavior_Description, a.Animal_Image, "
                    + "COALESCE(CASE WHEN ad.Application_Status = 'Rejected' THEN 'None' "
                    + "ELSE ad.Application_Status END, 'None') AS Adoption_Status, "
                    + "STRING_AGG(c.Color, ',') AS Colors "
                    + "FROM Animal a "
                    + "LEFT JOIN Adopts ad ON a.Animal_ID = ad.Animal_ID "
                    + "LEFT JOIN Animal_Color c ON a.Animal_ID = c.Animal_ID "
                    + "WHERE ad.Application_Status IS NULL OR ad.Application_Status = 'Rejected' "
                    + "GROUP BY a.Animal_ID, ad.Application_Status";
        } else {
            query = "SELECT a.Animal_ID, a.Name, a.Date_of_Birth, "
                    + "DATE_PART('year', AGE(a.Date_of_Birth))::text AS Age, "
                    + "a.Gender, a.Breed, a.Size, "
                    + "a.Behavior_Description, a.Animal_Image, "
                    + "COALESCE(ad.Application_Status, 'None') AS Adoption_Status, "
                    + "STRING_AGG(c.Color, ',') AS Colors "
                    + "FROM Animal a "
                    + "LEFT JOIN Adopts ad ON a.Animal_ID = ad.Animal_ID "
                    + "LEFT JOIN Animal_Color c ON a.Animal_ID = c.Animal_ID "
                    + "GROUP BY a.Animal_ID, ad.Application_Status";
        }

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Animal currentAnimal = new Animal(
                        rs.getInt("Animal_ID"),
                        rs.getString("Name"),
                        rs.getString("Age"),
                        rs.getString("Gender"),
                        rs.getString("Breed"),
                        rs.getString("Size"),
                        rs.getString("Adoption_Status"),
                        rs.getString("Behavior_Description"),
                        rs.getString("Animal_Image"),
                        rs.getString("Colors"),
                        rs.getString("Date_of_Birth")
                );
                animals.add(currentAnimal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return animals;
    }

    @FXML
    private void generateReport() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Load the .jrxml file
            String reportFileName = (userType.equals("Adopter")) ? "AnimalsReport_Adopter.jrxml" : "AnimalsReport_Staff.jrxml";
            InputStream inp = new FileInputStream(new File(reportFileName));

            // Compile the report
            JasperDesign jd = JRXmlLoader.load(inp);
            JasperReport jr = JasperCompileManager.compileReport(jd);

            // Set report parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("adopterSSN", SSN);

            // Fill the report with data
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, connection);

            // Display the report in a JFrame
            JFrame frame = new JFrame("Adopter Report");
            frame.getContentPane().add(new JRViewer(jp));
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserType(String userType) {
        this.userType = userType;
        updateButtonVisibility();
        animalTable.setItems(loadAnimalsByUserType());
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    private void updateButtonVisibility() {
        boolean isStaff = "Staff".equalsIgnoreCase(userType);
        addAnimalButton.setVisible(isStaff);
        removeAnimalButton.setVisible(isStaff);
        editAnimalButton.setVisible(isStaff);
    }

    private void openAnimalPopupView(Animal animal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/AnimalPopup.fxml"));
            Parent root = loader.load();

            AnimalPopupController controller = loader.getController();
            controller.setAnimal(animal);

            Stage stage = new Stage();
            stage.setTitle("Animal Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addAnimal() {
        try {
            // Load the FXML for the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/AddAnimalPopup.fxml"));
            AnchorPane popup = loader.load();
            AddAnimalPopupController controller = loader.getController();

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Add New Aniaml");

            // Set the scene with the loaded FXML
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);

            // Optional: Set the modality to block input to other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to close
            popupStage.showAndWait();

            animalTable.setItems(loadAnimalsByUserType());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeAnimal() {
        // Get the selected animal from the table
        Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();

        if (selectedAnimal == null) {
            // Show error message if no animal is selected
            JOptionPane.showMessageDialog(null, "No animal selected. Please select an animal to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the Animal ID of the selected animal
        int animalId = selectedAnimal.getAnimalId();

        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        String query = "DELETE FROM Animal WHERE Animal_ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password); PreparedStatement pst = conn.prepareStatement(query)) {

            // Set the parameter for the query (Animal ID)
            pst.setInt(1, animalId);

            // Execute the delete query
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                // Successfully deleted the animal from the database
                JOptionPane.showMessageDialog(null, "Animal with ID " + animalId + " has been removed.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Refresh the table data to reflect the change
                animalTable.setItems(loadAnimalsByUserType());
            } else {
                // No animal found with that ID in the database
                JOptionPane.showMessageDialog(null, "No animal found with the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error message if an exception occurs
            JOptionPane.showMessageDialog(null, "Error deleting animal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void editAnimal() {
        // Get the selected animal from the table
        Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();

        if (selectedAnimal == null) {
            // Show error message if no animal is selected
            JOptionPane.showMessageDialog(null, "No animal selected. Please select an animal to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Load the FXML for the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Popups/EditAnimalPopup.fxml"));
            AnchorPane popup = loader.load();

            // Get the controller of the popup
            EditAnimalPopupController controller = loader.getController();

            // Pass the selected animal to the controller
            controller.setAnimal(selectedAnimal);

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Edit Animal");

            // Set the scene with the loaded FXML
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);

            // Optional: Set the modality to block input to other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to close
            popupStage.showAndWait();

            // After the popup closes, refresh the table data (if needed)
            animalTable.setItems(loadAnimalsByUserType());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
