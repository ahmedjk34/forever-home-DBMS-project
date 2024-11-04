package com.mycompany.foreverhomedbmsproject.Popups;

import com.mycompany.foreverhomedbmsproject.Server.Adopter;
import com.mycompany.foreverhomedbmsproject.Server.Animal; // Assuming you have an Animal class
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class NewApplicationPopupController implements Initializable {

    @FXML
    private TableView<Animal> animalTableView; // Assuming Animal is the data class for the animals
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

    private Adopter adopter;
    private ObservableList<Animal> animalList; // List to hold animal data

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the columns
        animalIdColumn.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));

        // Populate the animal list with fake data
        animalList = FXCollections.observableArrayList(
                new Animal(1, "Buddy", "2 years", "Male", "Labrador", "Large", "Available", "Friendly and energetic", "path/to/buddy_image.png", "Black, Yellow", "2022-01-01"),
                new Animal(2, "Bella", "1 year", "Female", "Beagle", "Medium", "Available", "Curious and playful", "path/to/bella_image.png", "Brown, White", "2023-03-15"),
                new Animal(3, "Max", "3 years", "Male", "Poodle", "Small", "Adopted", "Loves to play fetch", "path/to/max_image.png", "White", "2021-05-20"),
                new Animal(4, "Luna", "4 years", "Female", "German Shepherd", "Large", "Available", "Protective and loyal", "path/to/luna_image.png", "Black, Tan", "2019-08-30"),
                new Animal(5, "Charlie", "6 months", "Male", "Siamese Cat", "Small", "Available", "Loves to cuddle", "path/to/charlie_image.png", "Cream, Chocolate", "2024-04-10"),
                new Animal(6, "Daisy", "2 years", "Female", "Tabby Cat", "Small", "Adopted", "Independent and playful", "path/to/daisy_image.png", "Gray, Black", "2021-12-05")
        );

        animalTableView.setItems(animalList);

        // Add event handler for table row selection
        animalTableView.setOnMouseClicked(this::handleMouseClick);
    }

    @FXML
    public void handleMouseClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Animal selectedAnimal = animalTableView.getSelectionModel().getSelectedItem();
            if (selectedAnimal != null) {
                // Show more information about the selected animal
                showAnimalDetails(selectedAnimal);
            }
        }
    }

    private void showAnimalDetails(Animal animal) {
        // Implement a method to display detailed information about the selected animal
        // This could open a new popup or dialog with animal details
    }

    @FXML
    public void adoptAnimal() {
        Animal selectedAnimal = animalTableView.getSelectionModel().getSelectedItem();
        if (selectedAnimal != null) {
            // Implement the adoption logic here
            System.out.println("Adoption request for: " + selectedAnimal.getName());
            // Perhaps update the database, inform the adopter, etc.
        }
    }

    public void setAdopter(Adopter adopter) {
        this.adopter = adopter;
    }
}
