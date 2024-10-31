package com.mycompany.foreverhomedbmsproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Authentication"), 700, 600);
        stage.setScene(scene);
        stage.show();
    }

static void setRoot(String fxml) throws IOException {
    System.out.println("Changing root to: " + fxml); // This should always print
    Parent newRoot = loadFXML(fxml);
    scene.setRoot(newRoot);
    
    // Adjust the window size based on the loaded FXML
    if (!(fxml.equals("Authentication"))) {
        System.out.println("Switching to larger size");
        setStageSize(800, 650); // Set to larger size for Adopter Dashboard
    } else {
        System.out.println("Switching to default size");
        setStageSize(700, 600); // Default size for Authentication
    }
}


    private static void setStageSize(double width, double height) {
        Stage stage = (Stage) scene.getWindow();
        stage.setWidth(width);
        stage.setHeight(height);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
