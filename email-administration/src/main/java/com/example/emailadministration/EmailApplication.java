package com.example.emailadministration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EmailApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("EmailApplication.fxml"));
            //Image mailIcon = new Image("mail_icon.png");
            // Font font = Font.loadFont(getClass().getResourceAsStream("scp.ttf"), 14);
            // System.out.println(font.getName());
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().addAll(getClass().getResource("application.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}