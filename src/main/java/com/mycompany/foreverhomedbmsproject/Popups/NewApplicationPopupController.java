package com.mycompany.foreverhomedbmsproject.Popups;

import com.mycompany.foreverhomedbmsproject.Server.Animal;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class NewApplicationPopupController implements Initializable {

    @FXML
    private TableView<Animal> animalTableView;
    @FXML
    private TableColumn<Animal, String> animalIdColumn;
    @FXML
    private TableColumn<Animal, String> nameColumn;
    @FXML
    private TableColumn<Animal, Integer> ageColumn;
    @FXML
    private TableColumn<Animal, String> genderColumn;
    @FXML
    private TableColumn<Animal, String> breedColumn;
    @FXML
    private TableColumn<Animal, String> sizeColumn;

    private String adopterSSN;
    private ObservableList<Animal> animalList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        animalIdColumn.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));

        animalList = FXCollections.observableArrayList();
        animalTableView.setItems(animalList);

        loadData();

        animalTableView.setOnMouseClicked(this::handleMouseClick);
    }

    private void loadData() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        String query = "SELECT a.Animal_ID, a.Name, a.Date_of_Birth, "
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

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            animalList.clear();

            while (rs.next()) {
                int animalId = rs.getInt("Animal_ID");
                String name = rs.getString("Name");
                String age = rs.getString("Age") + " years";
                String gender = rs.getString("Gender");
                String breed = rs.getString("Breed");
                String size = rs.getString("Size");
                String adoptionStatus = rs.getString("Adoption_Status");
                String behaviorDescription = rs.getString("Behavior_Description");
                String animalImage = rs.getString("Animal_Image");
                String colors = rs.getString("Colors");

                Animal animal = new Animal(animalId, name, age, gender, breed, size, adoptionStatus,
                        behaviorDescription, animalImage, colors, rs.getString("Date_of_Birth"));
                animalList.add(animal);
            }

            animalTableView.setItems(animalList);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading animal data: " + e.getMessage());
        }
    }

    @FXML
    public void handleMouseClick(MouseEvent event) {
        Animal selectedAnimal = animalTableView.getSelectionModel().getSelectedItem();
        if (selectedAnimal != null) {
            if (event.getClickCount() == 2) {
                showAnimalPopup(selectedAnimal);
            }
        }
    }

    private void showAnimalPopup(Animal animal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AnimalPopup.fxml"));
            Parent root = loader.load();

            AnimalPopupController popupController = loader.getController();
            popupController.setAnimal(animal);

            Stage stage = new Stage();
            stage.setTitle("Animal Details");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading AnimalPopup.fxml: " + e.getMessage());
        }
    }

    private void showAnimalDetails(Animal animal) {
    }

    @FXML
    public void adoptAnimal() {
        Animal selectedAnimal = animalTableView.getSelectionModel().getSelectedItem();
        if (selectedAnimal != null) {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "ahm@212005";

            String insertQuery = "INSERT INTO adopts (Animal_ID, ssn, application_date, application_status) VALUES (?, ?, ?, ?)";

            try (Connection connection = DriverManager.getConnection(url, user, password); PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

                // Set the parameters for the query
                preparedStatement.setInt(1, selectedAnimal.getAnimalId());  // Assuming Animal has getId()
                preparedStatement.setString(2, adopterSSN);
                preparedStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));  // Current date in YYYY-MM-DD format
                preparedStatement.setString(4, "Pending");

                // Execute the query
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Adoption request submitted for: " + selectedAnimal.getName());
                    Stage stage = (Stage) animalTableView.getScene().getWindow();
                    stage.close();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to submit adoption request for: " + selectedAnimal.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database error occurred while submitting adoption request.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No animal selected for adoption.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void setAdopterSSN(String adopterSSN) {
        this.adopterSSN = adopterSSN;
    }
}
