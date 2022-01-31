package com.example.emailadministration;

import javafx.fxml.FXMLLoader;
import javafx.geometry.VerticalDirection;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class MainSceneControllerTest extends ApplicationTest {
    Jdbi jdbi;

    @BeforeEach
    void setUp() {
        UserDatabaseConnection userDatabaseConnection = new UserDatabaseConnection();
        jdbi = userDatabaseConnection.getJdbi();
    }

    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(EmailUser.class.getResource("fxml/MainScene.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
    }

    @AfterEach
    void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    private void create_test_user() {
        jdbi.useHandle(handle ->
                handle.createUpdate("INSERT INTO users(FirstName, LastName, DateOfBirth, Login, Password," +
                                " EmailAddress, SecretQuestion, SecretQuestionAnswer)" +
                                "VALUES (:firstname, :lastname, :dob, :login, :password, :email, :sq, :sqa)")
                        .bind("firstname", "test")
                        .bind("lastname", "test")
                        .bind("dob", "1990-01-01")
                        .bind("login", "test")
                        .bind("password", "test")
                        .bind("email", "testemail")
                        .bind("sq", "test")
                        .bind("sqa", "boobs")
                        .execute());

    }

    private void delete_test_user() {
        jdbi.useHandle(handle -> handle.execute("DELETE FROM users WHERE Login = 'test'"));
    }

    @Test
    public void both_fields_empty() {
        clickOn("#loginButton");
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText("Fields cannot be empty!"));
    }

    @Test
    public void login_field_empty() {
        clickOn("#passwordTextField").write("something");
        clickOn("#loginButton");
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText("Fields cannot be empty!"));
    }

    @Test
    public void password_field_empty() {
        clickOn("#loginTextField").write("something");
        clickOn("#loginButton");
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText("Fields cannot be empty!"));
    }

   @Test
   public void login_successful_account_non_existent() {
       clickOn("#loginTextField").write("something");
       clickOn("#passwordTextField").write("something");
       clickOn("#loginButton");
       FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText("The account you tried to enter does not exist. Sign up instead."));
   }

    @Test
    public void login_successful_account_existent() {
        clickOn("#loginTextField").write("test");
        clickOn("#passwordTextField").write("test");
        create_test_user();
        clickOn("#loginButton");
        delete_test_user();
        // if the name of messageViewAnchorPane is changed, will have to modify this
        // kinda pain, wish it was possible to generalize somehow
        FxAssert.verifyThat("#messageViewAnchorPane", NodeMatchers.isVisible());
    }

    @Test
    public void forgot_password_clicked() {
        clickOn("#forgotPasswordLink");
        FxAssert.verifyThat("#forgotPasswordAnchorPane", NodeMatchers.isVisible());
    }
}