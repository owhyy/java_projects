package com.example.emailadministration;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.jdbi.v3.core.Jdbi;

import java.io.DataInput;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
    Label signinErrorLabel;

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

    private boolean allFieldsFilled() {
        return !(firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty()
                || emailAddressTextField.getText().isEmpty() || usernameTextField.getText().isEmpty()
                || passwordTextField.getText().isEmpty() || dateOfBirthTextField.getText().isEmpty()
                || secretQuestionAnswerTextField.getText().isEmpty() || secretQuestionTextField.getText().isEmpty());
    }

    public void createAccount() {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        // TODO: format the string into a sql-good Date type
//        EmailUser user = new EmailUser(firstNameTextField.getText(), lastNameTextField.getText(), dateOfBirthTextField.getText(),
//                usernameTextField.getText(), passwordTextField.getText(),emailAddressTextField.getText(),
//                secretQuestionTextField.getText(),secretQuestionAnswerTextField.getText());

        UserDatabaseConnection databaseConnection = new UserDatabaseConnection();
        Jdbi jdbi = databaseConnection.getJdbi();

        jdbi.useHandle(handle ->
                handle.createUpdate("INSERT INTO users(FirstName, LastName, DateOfBirth, Login, Password," +
                        " EmailAddress, SecretQuestion, SecretQuestionAnswer)" +
                        "VALUES (:firstname, :lastname, :dob, :login, :password, :email, :sq, :sqa)")
                        .bind("firstname", firstNameTextField.getText())
                        .bind("lastname", lastNameTextField.getText())
                        .bind("dob", dateOfBirthTextField.getText())
                        .bind("login", usernameTextField.getText())
                        .bind("password",passwordTextField.getText())
                        .bind("email", emailAddressTextField.getText())
                        .bind("sq", secretQuestionTextField.getText())
                        .bind("sqa", secretQuestionAnswerTextField.getText())
                        .execute());
    }

    public void handleButtonClick(ActionEvent event) throws IOException {
        // if fields are full and data inside them is valid, create a sql instruction
            if (allFieldsFilled()) {
                if (iAgreeCheckBox.isSelected()) {
                createAccount();
                inputAnchorPane.setEffect(new GaussianBlur(10.5));
                successAnchorPane.setVisible(true);
            } else { signinErrorLabel.setText("That won't work! You need to agree!"); }
        } else {  signinErrorLabel.setText("You need to enter data to all of the fields!"); }
    }

}
