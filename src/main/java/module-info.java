module com.mycompany.foreverhomedbmsproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.desktop;
    requires net.sf.jasperreports.core;

    // Open packages for JavaFX reflection
    opens com.mycompany.foreverhomedbmsproject.Server to javafx.base;
    opens com.mycompany.foreverhomedbmsproject to javafx.fxml;

    // Export the main package
    exports com.mycompany.foreverhomedbmsproject;
}
