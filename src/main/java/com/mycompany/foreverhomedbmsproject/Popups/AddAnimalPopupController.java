package com.mycompany.foreverhomedbmsproject.Popups;

import java.io.File;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;

public class AddAnimalPopupController implements Initializable {

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
    private ComboBox<String> sizeComboBox; // ComboBox for size
    @FXML
    private TextField colorTextField;

    // Variable to hold the image path
    private String animalImagePath;

    // Database credentials
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "ahm@212005";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the gender ComboBox with possible values
        genderComboBox.getItems().addAll("Male", "Female");
        sizeComboBox.getItems().addAll("Small", "Medium", "Large");

    }

    @FXML
    private void addAnimal() {
        // Get values from the form fields
        String animalID = idTextField.getText();
        String name = nameTextField.getText();
        String behavior = behaviorTextArea.getText();
        String gender = genderComboBox.getValue();
        String breed = breedTextField.getText();
        String dob = dobTextField.getText(); // Date string from input field
        String size = sizeComboBox.getValue(); // Get selected size from ComboBox
        String color = colorTextField.getText(); // Comma-separated list of colors

        // Validation
        if (animalID.isEmpty() || name.isEmpty() || gender == null || breed.isEmpty() || dob.isEmpty() || size == null || color.isEmpty() || animalImagePath == null || animalImagePath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required, and an image must be selected!", "Error", JOptionPane.ERROR_MESSAGE);
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

        // SQL query to insert a new animal
        String animalQuery = "INSERT INTO Animal (Animal_ID, Name, Date_of_Birth, Behavior_Description, Size, Breed, Gender, Animal_Image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD); PreparedStatement pstAnimal = conn.prepareStatement(animalQuery)) {

            // Set values for the animal prepared statement
            pstAnimal.setInt(1, Integer.parseInt(animalID)); // Animal_ID (integer)
            pstAnimal.setString(2, name); // Name
            pstAnimal.setDate(3, sqlDate); // Date_of_Birth (java.sql.Date)
            pstAnimal.setString(4, behavior); // Behavior_Description
            pstAnimal.setString(5, size); // Size
            pstAnimal.setString(6, breed); // Breed
            pstAnimal.setString(7, gender); // Gender
            pstAnimal.setString(8, animalImagePath); // Animal_Image (file path)

            // Execute the insert query for Animal
            int rowsAffected = pstAnimal.executeUpdate();
            if (rowsAffected > 0) {
                // Success: Insert colors into Animal_Color
                String[] colorArray = color.split(","); // Split the colors by comma
                String colorQuery = "INSERT INTO Animal_Color (Animal_ID, Color) VALUES (?, ?)";

                try (PreparedStatement pstColor = conn.prepareStatement(colorQuery)) {
                    // Insert each color into Animal_Color table
                    for (String c : colorArray) {
                        pstColor.setInt(1, Integer.parseInt(animalID)); // Animal_ID
                        pstColor.setString(2, c.trim()); // Color (trim any extra spaces)
                        pstColor.addBatch(); // Add the insert to batch
                    }

                    // Execute batch insert for all colors
                    int[] batchResult = pstColor.executeBatch();
                    JOptionPane.showMessageDialog(null, "Animal added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    ((javafx.stage.Stage) idTextField.getScene().getWindow()).close();

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding colors: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Failure: Animal insert failed
                JOptionPane.showMessageDialog(null, "Failed to add animal.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Show error message if an exception occurs
            JOptionPane.showMessageDialog(null, "Error adding animal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
        }
    }

}
