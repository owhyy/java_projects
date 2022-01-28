package com.example.emailadministration;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import java.awt.*;

class SignUpSceneControllerTest extends ApplicationTest {

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
    public void should_blur_secondary_anchor_pane() {
        clickOn("#signUpButton");
        GaussianBlur gaussianBlur = new GaussianBlur(10.5);
        FxAssert.verifyThat("#inputAnchorPane", (AnchorPane a) -> a.getEffect().equals(gaussianBlur));
    }


    @Test
    public void should_set_second_anchorpane_as_primary() {


    }
}