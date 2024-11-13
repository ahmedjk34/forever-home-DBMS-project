package com.mycompany.foreverhomedbmsproject.Popups;

import com.mycompany.foreverhomedbmsproject.Server.Animal;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.util.ResourceBundle;

public class EditAnimalPopupController implements Initializable {

    // FXML fields mapped to UI elements
    private Animal animal;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea behaviorTextArea;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField breedTextField;
    @FXML
    private TextField dobTextField;
    @FXML
    private ComboBox<String> sizeComboBox;  // Replaced TextField with ComboBox for Size
    @FXML
    private TextField ageTextField;
    @FXML
    private TextField colorTextField;
    @FXML
    private Button editButton;
    @FXML
    private Button addImageButton;

    private String animalImagePath;  // Store the selected image's relative path

    // Database credentials
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "ahm@212005";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize gender options (this can be changed based on your needs)
        genderComboBox.getItems().addAll("Male", "Female");

        // Initialize size options
        sizeComboBox.getItems().addAll("Small", "Medium", "Large");

        // Call populateFields to initialize fields if an animal is set
        if (animal != null) {
            populateFields();
        }
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
        // Call populateFields when an animal is set
        if (this.animal != null) {
            populateFields();
        }
    }

    // Method to populate the form fields based on the animal data
    private void populateFields() {
        idTextField.setText(String.valueOf(animal.getAnimalId()));
        nameTextField.setText(animal.getName());
        behaviorTextArea.setText(animal.getBehaviorDescription());
        genderComboBox.setValue(animal.getGender());
        breedTextField.setText(animal.getBreed());
        dobTextField.setText(animal.getDob()); // Or you can format it
        sizeComboBox.setValue(animal.getSize()); // Set the ComboBox value based on size
        ageTextField.setText(animal.getAge());
        colorTextField.setText(animal.getColors());
        animalImagePath = animal.getAnimalImage();
    }

    // Method to handle the "Edit" button click
    @FXML
    private void handleEditButtonClick() {
        // Get values from the fields
        String animalID = idTextField.getText();
        String name = nameTextField.getText();
        String behavior = behaviorTextArea.getText();
        String gender = genderComboBox.getValue();
        String breed = breedTextField.getText();
        String dob = dobTextField.getText();
        String size = sizeComboBox.getValue();
        String age = ageTextField.getText();
        String color = colorTextField.getText();

        // Validation
        if (name.isEmpty() || breed.isEmpty() || gender == null || dob.isEmpty() || size == null || color.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate date format (YYYY-MM-DD)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = null;
        try {
            parsedDate = LocalDate.parse(dob, formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Invalid date format! Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.sql.Date sqlDate = java.sql.Date.valueOf(parsedDate);

        // SQL query to update an existing animal
        String updateAnimalQuery = "UPDATE Animal SET Name = ?, Date_of_Birth = ?, Behavior_Description = ?, Size = ?, Breed = ?, Gender = ?, Animal_Image = ? WHERE Animal_ID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD); PreparedStatement pstAnimal = conn.prepareStatement(updateAnimalQuery)) {

            // Set values for the animal prepared statement
            pstAnimal.setString(1, name);
            pstAnimal.setDate(2, sqlDate);
            pstAnimal.setString(3, behavior);
            pstAnimal.setString(4, size);
            pstAnimal.setString(5, breed);
            pstAnimal.setString(6, gender);
            pstAnimal.setString(7, animalImagePath); // New image path if selected
            pstAnimal.setInt(8, Integer.parseInt(animalID));

            int rowsAffected = pstAnimal.executeUpdate();
            if (rowsAffected > 0) {
                // Success: Update colors in Animal_Color
                String[] colorArray = color.split(",");
                String deleteColorQuery = "DELETE FROM Animal_Color WHERE Animal_ID = ?";
                try (PreparedStatement pstDeleteColor = conn.prepareStatement(deleteColorQuery)) {
                    pstDeleteColor.setInt(1, Integer.parseInt(animalID));
                    pstDeleteColor.executeUpdate();
                }

                String colorQuery = "INSERT INTO Animal_Color (Animal_ID, Color) VALUES (?, ?)";
                try (PreparedStatement pstColor = conn.prepareStatement(colorQuery)) {
                    for (String c : colorArray) {
                        pstColor.setInt(1, Integer.parseInt(animalID));
                        pstColor.setString(2, c.trim());
                        pstColor.addBatch();
                    }
                    pstColor.executeBatch();
                    JOptionPane.showMessageDialog(null, "Animal updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Update the animal instance
                    animal.setName(name);
                    animal.setBehaviorDescription(behavior);
                    animal.setGender(gender);
                    animal.setBreed(breed);
                    animal.setDob(dob);
                    animal.setSize(size);
                    animal.setAge(age);
                    animal.setColors(color);
                    if (animalImagePath != null) {
                        animal.setAnimalImage(animalImagePath);
                    }

                    ((javafx.stage.Stage) idTextField.getScene().getWindow()).close();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding colors: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update animal.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating animal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle the "Add Image" button click
    @FXML
    private void handleAddImageButtonClick() {
        // Open a file chooser to select an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"));

        // Show the open file dialog and get the selected file
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String absolutePath = selectedFile.getAbsolutePath();

            String baseDirectory = "C:/Users/lenovo/Documents/NetBeansProjects/foreverHomeDBMSProject/src/main/resources/assets/animal_images";
            File baseDir = new File(baseDirectory);

            String relativePath = absolutePath.replace(baseDir.getAbsolutePath() + File.separator, "/assets/animal_images/");

            relativePath = relativePath.replace("\\", "/");

            animalImagePath = relativePath;

        }
    }
}
