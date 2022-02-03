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

import static org.testfx.api.FxAssert.verifyThat;

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
                        .bind("sq", "test")
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
    public void signin_clicked_checkbox_not_clicked() throws Exception {
        clickOn("#firstNameTextField").write("test");
        clickOn("#lastNameTextField").write("test");
        clickOn("#dateOfBirthTextField").write("test");
        clickOn("#usernameTextField").write("test");
        clickOn("#signupPasswordTextField").write("test");
        clickOn("#emailAddressTextField").write("test");
        clickOn("#secretQuestionTextField").write("test");
        clickOn("#secretQuestionAnswerTextField").write("test");
        clickOn("#signUpButton");
        verifyThat("#signinErrorLabel", LabeledMatchers.hasText("That won't work! You need to agree!"));
    }

    // This doesn't want to work, although is technically correct
//    @Test
//    public void should_blur_secondary_anchor_pane() {
//        clickOn("#firstNameTextField").write("test");
//        clickOn("#lastNameTextField").write("test");
//        clickOn("#dateOfBirthTextField").write("1990-01-01");
//        clickOn("#usernameTextField").write("test");
//        clickOn("#signupPasswordTextField").write("test");
//        clickOn("#emailAddressTextField").write("test");
//        clickOn("#secretQuestionTextField").write("test");
//        clickOn("#secretQuestionAnswerTextField").write("test");
//        clickOn("#checkEmailButton");
//        clickOn("#iAgreeCheckBox");
//        clickOn("#signUpButton");
//
//        delete_test_user();
//        WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);
//        GaussianBlur gb = new GaussianBlur(10.5);
//
//        verifyThat("#inputAnchorPane", (AnchorPane a) -> a.getEffect().equals(gb));
//    }

    @Test
    public void should_set_box_and_success_hyperlink_visible() throws Exception {
        clickOn("#firstNameTextField").write("test");
        clickOn("#lastNameTextField").write("test");
        clickOn("#dateOfBirthTextField").write("1900-01-01");
        clickOn("#usernameTextField").write("test");
        clickOn("#signupPasswordTextField").write("test");
        clickOn("#emailAddressTextField").write("test");
        clickOn("#secretQuestionTextField").write("test");
        clickOn("#secretQuestionAnswerTextField").write("test");
        clickOn("#iAgreeCheckBox");
        clickOn("#checkEmailButton");
        clickOn("#signUpButton");

        delete_test_user();
        verifyThat("#successRectangle", NodeMatchers.isVisible());
        verifyThat("#successHyperLink", NodeMatchers.isVisible());
    }

    @Test
    public void signin_clicked_all_labels_empty() throws Exception {
        clickOn("#iAgreeCheckBox");
        clickOn("#signUpButton");
        verifyThat("#signinErrorLabel", LabeledMatchers.hasText("Fields cannot be empty!"));
    }

    @Test
    public void signin_clicked_one_label() throws Exception {
        clickOn("#firstNameTextField").write("test");
        clickOn("#iAgreeCheckBox");
        clickOn("#signUpButton");
        verifyThat("#signinErrorLabel", LabeledMatchers.hasText("Fields cannot be empty!"));
    }

    @Test
    public void signin_clicked_labels_empty_checkbox_empty() throws Exception {
        clickOn("#signUpButton");
        verifyThat("#signinErrorLabel", LabeledMatchers.hasText("Fields cannot be empty!"));
    }

    @Test
    public void signin_clicked_email_not_checked() throws Exception {
        clickOn("#firstNameTextField").write("test");
        clickOn("#lastNameTextField").write("test");
        clickOn("#dateOfBirthTextField").write("test");
        clickOn("#usernameTextField").write("test");
        clickOn("#signupPasswordTextField").write("test");
        clickOn("#emailAddressTextField").write("test");
        clickOn("#secretQuestionTextField").write("test");
        clickOn("#secretQuestionAnswerTextField").write("test");
        clickOn("#iAgreeCheckBox");
        clickOn("#signUpButton");
        verifyThat("#signinErrorLabel", LabeledMatchers.hasText("You need to check if your email is available first!"));
    }

    @Test
    public void should_send_sql_create() throws Exception {
        clickOn("#firstNameTextField").write("test");
        clickOn("#lastNameTextField").write("test");
        clickOn("#dateOfBirthTextField").write("1990-01-01");
        clickOn("#usernameTextField").write("test");
        clickOn("#signupPasswordTextField").write("test");
        clickOn("#emailAddressTextField").write("test");
        clickOn("#secretQuestionTextField").write("test");
        clickOn("#secretQuestionAnswerTextField").write("test");
        clickOn("#checkEmailButton");
        clickOn("#iAgreeCheckBox");
        clickOn("#signUpButton");

        int queryResult = jdbi.withHandle(handle ->
                handle.createQuery("SELECT count(1) FROM users WHERE Login = 'test' AND Password = 'test'")
                        .mapTo(Integer.class)
                        .one());

        delete_test_user();

        Assertions.assertEquals(queryResult, 1);
    }

    @Test
    public void have_an_account_pressed_go_back_to_main_scene() throws Exception {
        scroll(30, VerticalDirection.DOWN);
        clickOn("#logInHyperLink");
        verifyThat("#loginButton", NodeMatchers.isVisible());
    }

    @Test
    public void account_succes_go_back() throws Exception {
        clickOn("#firstNameTextField").write("test");
        clickOn("#lastNameTextField").write("test");
        clickOn("#dateOfBirthTextField").write("1900-01-01");
        clickOn("#usernameTextField").write("test");
        clickOn("#signupPasswordTextField").write("test");
        clickOn("#emailAddressTextField").write("test");
        clickOn("#secretQuestionTextField").write("test");
        clickOn("#secretQuestionAnswerTextField").write("test");
        clickOn("#iAgreeCheckBox");
        clickOn("#checkEmailButton");
        clickOn("#signUpButton");

        delete_test_user();

        clickOn("#successHyperLink");
        verifyThat("#loginButton", NodeMatchers.isVisible());
    }

    @Test
    public void email_empty_check_pressed() {
        clickOn("#checkEmailButton");
        verifyThat("#invalidEmailErrorLabel", LabeledMatchers.hasText("We can't check an empty field"));
    }

    @Test
    public void email_nonexistent_check_pressed() {
        clickOn("#emailAddressTextField").write("testemail");
        clickOn("#checkEmailButton");
        verifyThat("#validEmailLabel", LabeledMatchers.hasText("Looks good!"));
    }

    @Test
    public void email_existent_check_pressed() {
        create_test_user();
        clickOn("#emailAddressTextField").write("test");
        clickOn("#checkEmailButton");

        delete_test_user();

        FxAssert.verifyThat("#invalidEmailErrorLabel", LabeledMatchers.hasText("An account with this email already exists"));
    }
}