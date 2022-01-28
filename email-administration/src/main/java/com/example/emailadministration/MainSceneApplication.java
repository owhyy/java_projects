package com.example.emailadministration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;

public class MainSceneApplication extends Application {

    public static void main(String[] args) throws SQLException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            //Parent root = FXMLLoader.load(getClass().getResource("fxml/MainScene.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("fxml/SignUpScene.fxml"));
            Scene scene = new Scene(root, 1280, 800);
            scene.getStylesheets().addAll(getClass().getResource("application.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Email");

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}