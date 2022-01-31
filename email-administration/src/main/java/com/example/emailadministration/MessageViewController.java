package com.example.emailadministration;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class MessageViewController {
    @FXML
    AnchorPane messageViewAnchorPane;
    @FXML
    AnchorPane topAnchorPane;
    @FXML
    AnchorPane leftAnchorPane;

    @FXML
    Button myProfileButton;
    @FXML
    Button composeButton;
    @FXML
    Button viewImportantButton;
    @FXML
    Button viewSentButton;
    @FXML
    Button viewStarredButton;
    @FXML
    Button nextPageButton;
    @FXML
    Button prevPageButton;

    @FXML
    Label pageNumberTextField;
    @FXML
    Label dateLabel;

    @FXML
    TextField searchMessageTextField;

    @FXML
    Rectangle dateRectangle;

    @FXML
    TableView messagesTable;

    @FXML
    TableColumn senderColumn;
    @FXML
    TableColumn titleColumn;
    @FXML
    TableColumn contentsColumn;
    @FXML
    TableColumn dateColumn;
}
