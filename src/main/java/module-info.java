module com.mycompany.foreverhomedbmsproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.desktop;
    opens com.mycompany.foreverhomedbmsproject.Server to javafx.base;
    opens com.mycompany.foreverhomedbmsproject to javafx.fxml;
    exports com.mycompany.foreverhomedbmsproject;
}
