package com.mycompany.foreverhomedbmsproject;

import com.mycompany.foreverhomedbmsproject.Popups.AnimalPopupController;
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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRXmlUtils;
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
    private Button addAnimalButton;
    @FXML
    private Button removeAnimalButton;
    @FXML
    private Button editAnimalButton;

    private String userType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        animalIdColumn.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        adoptionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("adoptionStatus"));

        animalTable.setItems(loadAnimalsByUserType());
        updateButtonVisibility();

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
            query = "SELECT a.Animal_ID, a.Name, "
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
            InputStream inp = new FileInputStream(new File("AnimalExplorerReport.jrxml"));
            JasperDesign jd = JRXmlLoader.load(inp);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);

            JFrame frame = new JFrame("Report");
            frame.getContentPane().add(new JRViewer(jp));
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserType(String userType) {
        this.userType = userType;
        updateButtonVisibility();
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
        System.out.println("Add Animal button clicked");
    }

    @FXML
    private void removeAnimal() {
        System.out.println("Remove Animal button clicked");
    }

    @FXML
    private void editAnimal() {
        System.out.println("Edit Animal button clicked");
    }
}
