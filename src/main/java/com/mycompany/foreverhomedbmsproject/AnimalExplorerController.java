package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Server.Animal;
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
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        animalIdColumn.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        adoptionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("adoptionStatus")); // Matches the getter
        animalTable.setItems(getAnimalsWithPendingOrRejectedStatus());
    }

    private ObservableList<Animal> getAnimalsWithPendingOrRejectedStatus() {
        ObservableList<Animal> animals = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "ahm@212005";

        String query = "SELECT a.Animal_ID, a.Name, " +
                       "DATE_PART('year', AGE(a.Date_of_Birth))::text AS Age, " +
                       "a.Gender, a.Breed, a.Size, " +
                       "CASE WHEN ad.Application_Status = 'Rejected' THEN 'None' " +
                       "ELSE ad.Application_Status END AS Adoption_Status " +
                       "FROM Animal a " +
                       "LEFT JOIN Adopts ad ON a.Animal_ID = ad.Animal_ID " +
                       "WHERE ad.Application_Status IN ('Pending', 'Rejected')";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Animal currentAnimal = new Animal(
                    rs.getInt("Animal_ID"),
                    rs.getString("Name"),
                    rs.getString("Age"),
                    rs.getString("Gender"),
                    rs.getString("Breed"),
                    rs.getString("Size"),
                    rs.getString("Adoption_Status")
                );
                System.out.println(currentAnimal);
                animals.add(currentAnimal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return animals;
    }
}
