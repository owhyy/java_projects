package com.example.emailadministration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;

public class ForgotPasswordSceneController {

    private String secretQuestion;
    private String secretQuestionAnswer;

    @FXML
    AnchorPane forgotPasswordMainAnchorPane;
    @FXML
    AnchorPane forgotPasswordInputAnchorPane;

    @FXML
    ImageView forgotPasswordImage;

    @FXML
    TextField forgotPasswordUsernameTextField;
    @FXML
    TextField forgotPasswordSecretQuestionAnswerTextField;

    @FXML
    Button forgotPasswordNextButton;

    @FXML
    Hyperlink forgotPasswordBackHyperlink;
    @FXML
    Hyperlink forgotPasswordSuccessHyperlink;

    @FXML
    Label secretQuestionLabel;
    @FXML
    Label forgotPasswordErrorLabel;

    @FXML
    Rectangle forgotPasswordSuccessRectangle;

    public void handleNextButtonPressed(ActionEvent event) {
        UserDatabaseConnection userDatabaseConnection = new UserDatabaseConnection();
        Jdbi jdbi = userDatabaseConnection.getJdbi();

        if (forgotPasswordUsernameTextField.getText().isEmpty()) {
            forgotPasswordErrorLabel.setText("Field cannot be empty!");
        } else {
            String username = forgotPasswordUsernameTextField.getText();

            // 1 means there is 1 person with login = input
            int searchLoginQuery =  jdbi.withHandle(handle ->
                    handle.createQuery("SELECT count(1) FROM users WHERE Login = ?")
                            .bind(0, username)
                            .mapTo(Integer.class)
                            .one());
            if (searchLoginQuery!=1) {
                forgotPasswordErrorLabel.setText("We can't find an account with this username in our database");
            } else {
                secretQuestion = jdbi.withHandle(handle ->
                        handle.createQuery("SELECT SecretQuestion FROM users WHERE Login = ?")
                                .bind(0, username)
                                .mapTo(String.class)
                                .one());
                forgotPasswordUsernameTextField.setVisible(false);
                forgotPasswordSecretQuestionAnswerTextField.setVisible(true);
                forgotPasswordImage.setVisible(false);
                secretQuestionLabel.setVisible(true);
                secretQuestionLabel.setText(secretQuestion);
                forgotPasswordNextButton.setOnAction(this::handleGetPasswordButtonPressed);
            }
        }
    }

    public void handleGetPasswordButtonPressed(ActionEvent event) {
        if (forgotPasswordSecretQuestionAnswerTextField.getText().isEmpty()) {
            forgotPasswordErrorLabel.setText("Field cannot be empty!");
        } else {
            UserDatabaseConnection userDatabaseConnection = new UserDatabaseConnection();
            Jdbi jdbi = userDatabaseConnection.getJdbi();
            secretQuestionAnswer = jdbi.withHandle(handle ->
                    handle.createQuery("SELECT SecretQuestionAnswer FROM users WHERE Login = ?")
                            .bind(0, forgotPasswordUsernameTextField.getText())
                            .mapTo(String.class)
                            .one());
            if (forgotPasswordSecretQuestionAnswerTextField.getText().equals(secretQuestionAnswer)) {
                forgotPasswordInputAnchorPane.setEffect(new GaussianBlur(10.5));
                forgotPasswordSuccessRectangle.setVisible(true);
                forgotPasswordSuccessHyperlink.setVisible(true);
            } else {
                forgotPasswordErrorLabel.setText("Wrong answer. Try again");
            }
        }
    }

    public void moveToMainScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/MainScene.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void handleSuccessCopyHyperlinkClicked(ActionEvent event) {

    }

    public void handleForgotPasswordGoBackHyperlink(ActionEvent event) throws IOException {
        moveToMainScene(event);
    }

    public void handleSuccesGoBackHyperlinkClicked(ActionEvent event) throws IOException {
        moveToMainScene(event);
    }
}
