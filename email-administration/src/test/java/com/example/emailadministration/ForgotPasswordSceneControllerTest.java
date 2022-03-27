package com.example.emailadministration;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeUnit;

// TODO: modify the delete sql function to delete only if a test user exists, and use it in the @aftereach
// TODO: maybe move the create_test_user() and delete_test_user() to a class, just to make code more DRY
// TODO: pressing enter acts as pressing on the button

class ForgotPasswordSceneControllerTest extends ApplicationTest {
    Jdbi jdbi;

    @BeforeEach
    public void setUp() throws Exception {
        UserDatabaseConnection userDatabaseConnection = new UserDatabaseConnection();
        jdbi = userDatabaseConnection.getJdbi();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(EmailUser.class.getResource("fxml/ForgotPasswordScene.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
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
                        .bind("email", "test")
                        .bind("sq", "What do you like most in this world, but never will touch in your life?")
                        .bind("sqa", "boobs")
                        .execute());

    }

    private void delete_test_user() {
        jdbi.useHandle(handle -> handle.execute("DELETE FROM users WHERE Login = 'test'"));
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Test
    public void empty_field_should_set_error_label() {
        clickOn("#forgotPasswordNextButton");
        FxAssert.verifyThat("#forgotPasswordErrorLabel", LabeledMatchers.hasText("Field cannot be empty!"));
    }

    @Test
    public void nonexisting_login_should_set_error_label() {
        clickOn("#forgotPasswordUsernameTextField").write("test");
        clickOn("#forgotPasswordNextButton");
        FxAssert.verifyThat("#forgotPasswordErrorLabel",
                LabeledMatchers.hasText("We can't find an account with this username in our database"));
    }

    @Test
    public void existing_login_should_display_secret_question() {
        create_test_user();
        clickOn("#forgotPasswordUsernameTextField").write("test");
        clickOn("#forgotPasswordNextButton");
        FxAssert.verifyThat("#forgotPasswordImage", NodeMatchers.isInvisible());
        FxAssert.verifyThat("#secretQuestionLabel", NodeMatchers.isVisible());
        FxAssert.verifyThat("#forgotPasswordUsernameTextField", NodeMatchers.isInvisible());
        FxAssert.verifyThat("#forgotPasswordSecretQuestionAnswerTextField", NodeMatchers.isVisible());
        delete_test_user();
    }

    @Test
    public void test_that_label_matches_secretquestion() {
        create_test_user();
        clickOn("#forgotPasswordUsernameTextField").write("test");
        clickOn("#forgotPasswordNextButton");
        String secretQuestion = jdbi.withHandle(handle ->
                  handle.createQuery("SELECT SecretQuestion FROM users WHERE Login = 'test'")
                        .mapTo(String.class)
                        .one());
        delete_test_user();
        FxAssert.verifyThat("#secretQuestionLabel", LabeledMatchers.hasText(secretQuestion));
    }

    @Test
    public void existing_login_empty_question_field_should_set_error_label() {
        create_test_user();
        clickOn("#forgotPasswordUsernameTextField").write("test");
        clickOn("#forgotPasswordNextButton");
        clickOn("#forgotPasswordNextButton");
        delete_test_user();
        FxAssert.verifyThat("#forgotPasswordErrorLabel", LabeledMatchers.hasText("Field cannot be empty!"));
    }

    @Test
    public void existing_login_wrong_answer_should_set_error_label() {
        create_test_user();
        clickOn("#forgotPasswordUsernameTextField").write("test");
        clickOn("#forgotPasswordNextButton");
        clickOn("#forgotPasswordSecretQuestionAnswerTextField").write("Kitai bugaga");
        clickOn("#forgotPasswordNextButton");

        delete_test_user();
        FxAssert.verifyThat("#forgotPasswordErrorLabel", LabeledMatchers.hasText("Wrong answer. Try again"));
    }

    @Test
    public void default_behaviour_success_pane_invisible() {
        FxAssert.verifyThat("#forgotPasswordMainAnchorPane", NodeMatchers.isVisible());
        FxAssert.verifyThat("#forgotPasswordInputAnchorPane", NodeMatchers.isVisible());
    }

    @Test
    public void correct_answer_test() {
        create_test_user();
        clickOn("#forgotPasswordUsernameTextField").write("test");
        clickOn("#forgotPasswordNextButton");
        clickOn("#forgotPasswordSecretQuestionAnswerTextField").write("boobs");
        clickOn("#forgotPasswordNextButton");
        delete_test_user();
        FxAssert.verifyThat("#forgotPasswordMainAnchorPane", NodeMatchers.isVisible());
        FxAssert.verifyThat("#forgotPasswordInputAnchorPane", NodeMatchers.isVisible());
    }

    // still can't figure out how to check for effects
//    @Test
//    public void should_blur_scene() {
//        create_test_user();
//        clickOn("#forgotPasswordUsernameTextField").write("test");
//        clickOn("#forgotPasswordNextButton");
//        clickOn("#forgotPasswordSecretQuestionAnswerTextField").write("boobs");
//
//        verifyThat("#forgotPasswordAnchorPane", (AnchorPane a) -> a.getEffect().equals(new GaussianBlur(10.5)));
//        delete_test_user();
//    }

    @Test
    public void test_forgot_password_go_back_hyperlink_pressed() {
        clickOn("#forgotPasswordBackHyperlink");
        FxAssert.verifyThat("#loginButton", NodeMatchers.isVisible());
    }

    @Test
    public void test_success_go_back_hyperlink_pressed() {}

    @Test
    public void test_copy_password_hyperlink_pressed() {}
}
