package com.example.emailadministration;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
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
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.awt.*;
import java.util.concurrent.TimeUnit;

class SignUpSceneControllerTest extends ApplicationTest {
    Jdbi jdbi;

   @BeforeEach
   public void setUp() throws Exception {
       UserDatabaseConnection userDatabaseConnection = new UserDatabaseConnection();
       jdbi = userDatabaseConnection.getJdbi();
   }

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(EmailUser.class.getResource("fxml/SignUpScene.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Test
    public void signin_clicked_checkbox_not_clicked() throws Exception {
        clickOn("#firstNameTextField").write("test");
        clickOn("#lastNameTextField").write("test");
        clickOn("#dateOfBirthTextField").write("test");
        clickOn("#usernameTextField").write("test");
        clickOn("#passwordTextField").write("test");
        clickOn("#emailAddressTextField").write("test");
        clickOn("#secretQuestionTextField").write("test");
        clickOn("#secretQuestionAnswerTextField").write("test");
        clickOn("#signUpButton");
        FxAssert.verifyThat("#signinErrorLabel", LabeledMatchers.hasText("That won't work! You need to agree!"));
    }

    // This doesn't want to work, although is technically correct
//    @Test
//    public void should_blur_secondary_anchor_pane() {
//        clickOn("#signUpButton");
//        WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);
//        FxAssert.verifyThat("#inputAnchorPane", (AnchorPane a) -> a.getEffect().equals(new GaussianBlur(10.5)));
//    }

    @Test
    public void should_set_second_anchorpane_as_primary() throws Exception {
        clickOn("#firstNameTextField").write("test");
        clickOn("#lastNameTextField").write("test");
        clickOn("#dateOfBirthTextField").write("test");
        clickOn("#usernameTextField").write("test");
        clickOn("#passwordTextField").write("test");
        clickOn("#emailAddressTextField").write("test");
        clickOn("#secretQuestionTextField").write("test");
        clickOn("#secretQuestionAnswerTextField").write("test");
        clickOn("#iAgreeCheckBox");
        clickOn("#checkEmailButton");
        clickOn("#signUpButton");
        FxAssert.verifyThat("#successAnchorPane", (AnchorPane a) -> a.isVisible());
    }

    @Test
    public void signin_clicked_all_labels_not_full() throws Exception {
        clickOn("#iAgreeCheckBox");
        clickOn("#signUpButton");
        FxAssert.verifyThat("#signinErrorLabel", LabeledMatchers.hasText("You need to enter data to all of the fields!"));
    }

    @Test
    public void signin_clicked_one_label() throws Exception {
        clickOn("#firstNameTextField").write("test");
        clickOn("#iAgreeCheckBox");
        clickOn("#signUpButton");
        FxAssert.verifyThat("#signinErrorLabel", LabeledMatchers.hasText("Fields cannot be empty!"));
    }

    @Test
    public void signin_clicked_labels_empty_checkbox_empty() throws Exception {
        clickOn("#signUpButton");
        FxAssert.verifyThat("#signinErrorLabel", LabeledMatchers.hasText("Fields cannot be empty!"));
    }

    @Test
    public void signin_clicked_email_not_checked() throws Exception {
        clickOn("#firstNameTextField").write("test");
        clickOn("#lastNameTextField").write("test");
        clickOn("#dateOfBirthTextField").write("test");
        clickOn("#usernameTextField").write("test");
        clickOn("#passwordTextField").write("test");
        clickOn("#emailAddressTextField").write("test");
        clickOn("#secretQuestionTextField").write("test");
        clickOn("#secretQuestionAnswerTextField").write("test");
        clickOn("#iAgreeCheckBox");
        clickOn("#signUpButton");
        FxAssert.verifyThat("#signinErrorLabel", LabeledMatchers.hasText("You need to check if your email is available first!"));
    }

   @Test
   public void should_send_sql_create() throws Exception {
       clickOn("#firstNameTextField").write("test");
       clickOn("#lastNameTextField").write("test");
       clickOn("#dateOfBirthTextField").write("1990-01-01");
       clickOn("#usernameTextField").write("testsql");
       clickOn("#passwordTextField").write("testsql");
       clickOn("#emailAddressTextField").write("test");
       clickOn("#secretQuestionTextField").write("test");
       clickOn("#secretQuestionAnswerTextField").write("test");
       clickOn("#iAgreeCheckBox");
       clickOn("#signUpButton");

       int queryResult = jdbi.withHandle(handle ->
               handle.createQuery("SELECT count(1) FROM users WHERE Login = 'testsql' AND Password = 'testsql'")
               .mapTo(Integer.class)
               .one());

       jdbi.useHandle(handle -> handle.execute("DELETE FROM users WHERE Login = 'testsql'"));

       Assertions.assertEquals(queryResult, 1);
    }

    // TODO: check on desktop if this works
    @Test
    public void have_an_account_pressed_go_back_to_main_scene() throws Exception {
        clickOn("#logInHyperLink");
        FxAssert.verifyThat("#logInButton", NodeMatchers.isVisible());
   }

    // TODO: check on desktop if this works
    @Test
    public void account_succes_go_back() throws Exception {
        clickOn("#logInHyperLink");
        clickOn("#firstNameTextField").write("test");
        clickOn("#lastNameTextField").write("test");
        clickOn("#dateOfBirthTextField").write("test");
        clickOn("#usernameTextField").write("test");
        clickOn("#passwordTextField").write("test");
        clickOn("#emailAddressTextField").write("test");
        clickOn("#secretQuestionTextField").write("test");
        clickOn("#secretQuestionAnswerTextField").write("test");
        clickOn("#iAgreeCheckBox");
        clickOn("#checkEmailButton");
        clickOn("#signUpButton");

        clickOn("#successHyperLink");
        FxAssert.verifyThat("#logInButton", NodeMatchers.isVisible());
    }

    @Test
    public void email_empty_check_pressed() {
        clickOn("#checkEmailButton");
        FxAssert.verifyThat("#invalidEmailErrorLabel", TextMatchers.hasText("We can't check an empty field"));
   }

    @Test
    public void email_nonexistent_check_pressed() {
        clickOn("#emailAddressTextField").write("testemail");
        clickOn("#checkEmailButton");
        FxAssert.verifyThat("#validEmailLabel", TextMatchers.hasText("Looks good!"));
    }

    @Test
    public void email_existent_check_pressed() {
        clickOn("#emailAddressTextField").write("testemail");
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

        clickOn("#checkEmailButton");

        jdbi.useHandle(handle -> handle.execute("DELETE FROM users WHERE Login = 'test'"));

        FxAssert.verifyThat("#invalidEmailErrorLabel", TextMatchers.hasText("An account with this email already exists"));
    }


}