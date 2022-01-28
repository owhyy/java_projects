package com.example.emailadministration;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;

public class SignUpSceneController {
    EmailUser user;

    @FXML
    AnchorPane inputAnchorPane;
    @FXML
    AnchorPane successAnchorPane;

    @FXML
    Rectangle successRectangle;

    @FXML
    Label createAnAccountLabel;
    @FXML
    Label validEmailLabel;
    @FXML
    Label invalidEmailErrorLabel;

    @FXML
    TextField firstNameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    TextField dateOfBirthTextField;
    @FXML
    TextField usernameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    TextField emailAddressTextField;
    @FXML
    TextField secretQuestionTextField;
    @FXML
    TextField secretQuestionAnswerTextField;

    @FXML
    CheckBox iAgreeCheckBox;

    @FXML
    Button signUpButton;
    @FXML
    Button validEmailButton;

    @FXML
    Hyperlink successHyperLink;
    @FXML
    Hyperlink logInHyperLink;



    public void handleButtonClick(ActionEvent event) throws IOException {
        System.out.println("Button clicked");
    }

    public void createUser() {
        // first check and validate and blah blah blah
        UserDatabaseConnection userDatabaseConnection = new UserDatabaseConnection();
        Jdbi jdbi = userDatabaseConnection.getJdbi();

        jdbi.useHandle(handle ->
                handle.createUpdate("INSERT INTO users(idUser,FirstName,LastName,DateOfBirth," +
                                "Login,Password,EmailAddress,SecretQuestion,SecretQuestionAnswer) VALUES (:userdata)")
                        .bind("userdata", user.getQueryFormat())
                        .execute());
    }


}
