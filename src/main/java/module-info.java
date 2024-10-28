module com.mycompany.foreverhomedbmsproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.foreverhomedbmsproject to javafx.fxml;
    exports com.mycompany.foreverhomedbmsproject;
}
