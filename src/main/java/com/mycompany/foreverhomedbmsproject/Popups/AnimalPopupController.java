package com.mycompany.foreverhomedbmsproject.Popups;

import com.mycompany.foreverhomedbmsproject.Server.Animal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AnimalPopupController implements Initializable {

    // Declare FXML elements
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
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
    private ComboBox<String> genderComboBox;
    @FXML
    private TextArea behaviorTextArea;
    @FXML
    private ImageView animalImageView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize ComboBox items or other properties here if needed
    }

    // Method to set the animal data
    public void setAnimal(Animal animal) {
        idTextField.setText(String.valueOf(animal.getAnimalId()));
        nameTextField.setText(animal.getName()); // Assuming `Animal` has a method getName()
        breedTextField.setText(animal.getBreed()); // Assuming `Animal` has a method getBreed()
        dobTextField.setText(animal.getDob()); // Assuming `Animal` has a method getDob()
        sizeTextField.setText(animal.getSize()); // Assuming `Animal` has a method getSize()
        ageTextField.setText(String.valueOf(animal.getAge())); // Assuming `Animal` has a method getAge()
        colorTextField.setText(animal.getColors()); // Assuming `Animal` has a method getColor()
        genderComboBox.getSelectionModel().select(animal.getGender()); // Assuming `Animal` has a method getGender()
        behaviorTextArea.setText(animal.getBehaviorDescription()); // Assuming `Animal` has a method getBehaviorDescription()

        String imagePath = animal.getAnimalImage();
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        animalImageView.setImage(image);

        
    }
}
