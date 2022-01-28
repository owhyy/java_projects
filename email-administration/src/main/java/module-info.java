module com.example.emailadministration {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.jdbi.v3.core;

    opens com.example.emailadministration to javafx.fxml;
    exports com.example.emailadministration;
}