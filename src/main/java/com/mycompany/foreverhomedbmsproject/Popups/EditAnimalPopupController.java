package com.mycompany.foreverhomedbmsproject.Popups;

import com.mycompany.foreverhomedbmsproject.Server.Animal;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
    private TextField sizeTextField;
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
        sizeTextField.setText(animal.getSize());
        ageTextField.setText(animal.getAge());
        colorTextField.setText(animal.getColors());
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
        String size = sizeTextField.getText();
        String age = ageTextField.getText();
        String color = colorTextField.getText();

        // Validation
        if (name.isEmpty() || breed.isEmpty() || gender == null || dob.isEmpty() || size.isEmpty() || color.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate date format (YYYY-MM-DD)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = null;
        try {
            parsedDate = LocalDate.parse(dob, formatter); // Parse the date string into LocalDate
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Invalid date format! Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert LocalDate to java.sql.Date
        java.sql.Date sqlDate = java.sql.Date.valueOf(parsedDate); // Efficient conversion

        // SQL query to update an existing animal
        String updateAnimalQuery = "UPDATE Animal SET Name = ?, Date_of_Birth = ?, Behavior_Description = ?, Size = ?, Breed = ?, Gender = ?, Animal_Image = ? WHERE Animal_ID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD); PreparedStatement pstAnimal = conn.prepareStatement(updateAnimalQuery)) {

            // Set values for the animal prepared statement
            pstAnimal.setString(1, name); // Name
            pstAnimal.setDate(2, sqlDate); // Date_of_Birth (java.sql.Date)
            pstAnimal.setString(3, behavior); // Behavior_Description
            pstAnimal.setString(4, size); // Size
            pstAnimal.setString(5, breed); // Breed
            pstAnimal.setString(6, gender); // Gender
            pstAnimal.setString(7, animalImagePath); // Animal_Image (file path)
            pstAnimal.setInt(8, Integer.parseInt(animalID)); // Animal_ID (integer)

            // Execute the update query for Animal
            int rowsAffected = pstAnimal.executeUpdate();
            if (rowsAffected > 0) {
                // Success: Update colors in Animal_Color
                String[] colorArray = color.split(","); // Split the colors by comma
                String deleteColorQuery = "DELETE FROM Animal_Color WHERE Animal_ID = ?";
                try (PreparedStatement pstDeleteColor = conn.prepareStatement(deleteColorQuery)) {
                    pstDeleteColor.setInt(1, Integer.parseInt(animalID)); // Animal_ID
                    pstDeleteColor.executeUpdate(); // Delete previous color records
                }

                // Insert new colors
                String colorQuery = "INSERT INTO Animal_Color (Animal_ID, Color) VALUES (?, ?)";
                try (PreparedStatement pstColor = conn.prepareStatement(colorQuery)) {
                    for (String c : colorArray) {
                        pstColor.setInt(1, Integer.parseInt(animalID)); // Animal_ID
                        pstColor.setString(2, c.trim()); // Color
                        pstColor.addBatch(); // Add the insert to batch
                    }

                    // Execute batch insert for all colors
                    int[] batchResult = pstColor.executeBatch();
                    JOptionPane.showMessageDialog(null, "Animal updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    ((javafx.stage.Stage) idTextField.getScene().getWindow()).close();

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding colors: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Failure: Animal update failed
                JOptionPane.showMessageDialog(null, "Failed to update animal.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Show error message if an exception occurs
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
            // Get the absolute path of the selected image
            String absolutePath = selectedFile.getAbsolutePath();

            // Define the base directory to compare and strip the correct part of the path
            String baseDirectory = "C:/Users/lenovo/Documents/NetBeansProjects/foreverHomeDBMSProject/src/main/resources/assets/animal_images";
            File baseDir = new File(baseDirectory);

            // Get the relative path based on the base directory
            String relativePath = absolutePath.replace(baseDir.getAbsolutePath() + File.separator, "/assets/animal_images/");

            // Normalize the path (to avoid backslashes on Windows)
            relativePath = relativePath.replace("\\", "/");

            // Save the relative path for the image
            animalImagePath = relativePath;

            // Optionally, you could display the image path in the UI
            System.out.println("Selected image path: " + animalImagePath);
        }
    }
}
