package com.example.emailadministration;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;

public class SignUpSceneController {
    private Stage stage;
    private Scene scene;

    boolean emailWasChecked=false;
    @FXML
    ScrollPane scrollPane;
    @FXML
    AnchorPane inputAnchorPane;
    @FXML
    AnchorPane successAnchorPane;
    @FXML
    AnchorPane mainSignupAnchorPane;

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
    TextField signupPasswordTextField;
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
    Button checkEmailButton;

    @FXML
    Hyperlink successHyperLink;
    @FXML
    Hyperlink logInHyperLink;

    // setting faster scroll
    // see https://stackoverflow.com/a/59372510
    @FXML
    public void initialize() {
        // this is needed to avoid the text resizing when clicking on the screen bug
        // see https://stackoverflow.com/a/65890967
        mainSignupAnchorPane.setOnMousePressed(Event::consume);
        Platform.runLater(() -> setFasterScroller(scrollPane));
    }

    private static void setFasterScroller(ScrollPane scrollPane) {
        final double SPEED = 0.01;
        scrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * SPEED;
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY);
        });
    }

    private boolean allFieldsFilled() {
        return !(firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty()
                || emailAddressTextField.getText().isEmpty() || usernameTextField.getText().isEmpty()
                || signupPasswordTextField.getText().isEmpty() || dateOfBirthTextField.getText().isEmpty()
                || secretQuestionAnswerTextField.getText().isEmpty() || secretQuestionTextField.getText().isEmpty());
    }

    public void handleLogInHyperlink(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/MainScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void handleCheckEmailButton(ActionEvent event) {
        if (emailAddressTextField.getText().isEmpty()) {
            invalidEmailErrorLabel.setText("We can't check an empty field");
        } else {
            UserDatabaseConnection databaseConnection = new UserDatabaseConnection();
            Jdbi jdbi = databaseConnection.getJdbi();

            int queryResult = jdbi.withHandle(handle ->
                    handle.createQuery("SELECT count(1) FROM users WHERE EmailAddress = ?")
                            .bind(0, emailAddressTextField.getText())
                            .mapTo(Integer.class)
                            .one());
            if (queryResult == 1) {
                invalidEmailErrorLabel.setText("An account with this email already exists");
            } else {
                validEmailLabel.setText("Looks good!");
                emailWasChecked=true;
            }
        }
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
                        .bind("password", signupPasswordTextField.getText())
                        .bind("email", emailAddressTextField.getText())
                        .bind("sq", secretQuestionTextField.getText())
                        .bind("sqa", secretQuestionAnswerTextField.getText())
                        .execute());
    }

    public void handleSignInButtonClick(ActionEvent event) throws Exception {
        // if fields are full and data inside them is valid, create a sql instruction
        if (allFieldsFilled()) {
            if (iAgreeCheckBox.isSelected()) {
                if (emailWasChecked) {
                    createAccount();
                    inputAnchorPane.setEffect(new GaussianBlur(10.5));
                    successAnchorPane.setVisible(true);
                } else { signinErrorLabel.setText("You need to check if your email is available first!"); }
            } else { signinErrorLabel.setText("That won't work! You need to agree!"); }
        } else {  signinErrorLabel.setText("Fields cannot be empty!"); }
    }

}
