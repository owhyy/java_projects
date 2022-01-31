package com.example.emailadministration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.jdbi.v3.core.Jdbi;

public class ForgotPasswordSceneController {
    private String secretQuestion;
    private String secretQuestionAnswer;

    @FXML
    AnchorPane forgotPasswordMainAnchorPane;
    @FXML
    AnchorPane forgotPasswordInputAnchorPane;
    @FXML
    AnchorPane forgotPasswordSuccessAnchorPane;

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

        int searchLoginQuery =  jdbi.withHandle(handle ->
                handle.createQuery("SELECT count(1) FROM users WHERE Login = ?")
                        .bind(0, forgotPasswordUsernameTextField.getText())
                        .mapTo(Integer.class)
                        .one());


        if (forgotPasswordUsernameTextField.getText().isEmpty()) {
            forgotPasswordErrorLabel.setText("Field cannot be empty!");
        } else if (searchLoginQuery!=1) {
           forgotPasswordErrorLabel.setText("We can't find an account with this username in our database");
        } else {
            secretQuestion = jdbi.withHandle(handle ->
                    handle.createQuery("SELECT SecretQuestion FROM users WHERE Login = 'test'")
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

    public void handleGetPasswordButtonPressed(ActionEvent event) {
        if (forgotPasswordSecretQuestionAnswerTextField.getText().isEmpty()) {
            forgotPasswordErrorLabel.setText("Field cannot be empty!");
        } else {
            UserDatabaseConnection userDatabaseConnection = new UserDatabaseConnection();
            Jdbi jdbi = userDatabaseConnection.getJdbi();
            secretQuestionAnswer = jdbi.withHandle(handle ->
                    handle.createQuery("SELECT SecretQuestionAnswer FROM users WHERE Login = 'test'")
                            .mapTo(String.class)
                            .one());
            if (forgotPasswordSecretQuestionAnswerTextField.getText().equals(secretQuestionAnswer)) {
                forgotPasswordInputAnchorPane.setEffect(new GaussianBlur(10.5));
                forgotPasswordSuccessAnchorPane.setVisible(true);
            } else {
                forgotPasswordErrorLabel.setText("Wrong answer. Try again");
            }
        }
    }

    public void handleSuccessCopyHyperlinkClicked(ActionEvent event) {}
    public void handleSuccesGoBackHyperlinkClicked(ActionEvent event) {}
}
