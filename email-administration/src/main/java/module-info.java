module com.example.emailadministration {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.emailadministration to javafx.fxml;
    exports com.example.emailadministration;
}