package com.example.emailadministration;

import org.jdbi.v3.core.Jdbi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.controlsfx.control.action.Action;
import java.io.DataInput;
import java.io.IOException;
import java.util.List;

public class MainSceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    ImageView messageIcon;
    @FXML
    Button loginButton;
    @FXML
    TextField loginTextField;
    @FXML
    PasswordField passwordTextField;
    @FXML
    Hyperlink forgotPasswordLink;
    @FXML
    Hyperlink createAccountLink;
    @FXML
    Label errorLabel;

    public void switchToScene(String fxmlFileName, ActionEvent event) throws  IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSignUpScene(ActionEvent event) throws IOException {
        switchToScene("fxml/SignUpScene.fxml", event);
    }

    public void switchToMessageViewScene(ActionEvent event) throws IOException {
        switchToScene("fxml/MessageViewScene.fxml", event);
    }

    public void switchToForgotPasswordScene(ActionEvent event) throws IOException {
        switchToScene("fxml/ForgotPasswordScene.fxml", event);
    }

    public void handleLoginButtonPressed(ActionEvent event) throws IOException {
        String loginTextFieldInput = loginTextField.getText();
        String passwordTextFieldInput = passwordTextField.getText();
        // Stage stage = (Stage) loginButton.getScene().getWindow();
        if (loginTextFieldInput.isEmpty() || passwordTextFieldInput.isEmpty()) {
            errorLabel.setText("Data you entered is not valid. Try again.");
        } else if (validateLogin()) {
            // XXX not sure that is is correct
            switchToMessageViewScene(event);
        } else {
            errorLabel.setText("The account you tried to enter does not exist. Sign up instead.");
        }
    }

    public boolean validateLogin() {
        UserDatabaseConnection userDatabaseConnection = new UserDatabaseConnection();
        Jdbi jdbi = userDatabaseConnection.getJdbi();

        int queryResult = jdbi.withHandle(handle ->
                handle.createQuery("SELECT count(1) FROM users WHERE Login = ? AND Password = ?")
                        .bind(0, loginTextField.getText())
                        .bind(1, passwordTextField.getText())
                        .mapTo(Integer.class)
                        .one());

        return queryResult==1;
    }

    public void handleForgotPasswordPressed() {
        // TODO handle forgot password somehow
    }

    public void handloCreateAccountPressed() {
        // switchToSignUpScene( );
    }
}