module com.mycompany.foreverhomedbmsproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;

    opens com.mycompany.foreverhomedbmsproject to javafx.fxml;
    exports com.mycompany.foreverhomedbmsproject;
}
