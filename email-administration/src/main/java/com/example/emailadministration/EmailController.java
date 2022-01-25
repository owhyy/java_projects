package com.example.emailadministration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class EmailController {

    @FXML
    private Circle myCircle;
    private double x;
    private double y;

    public void up(ActionEvent e) {
        myCircle.setCenterY(y-=5);
    }

    public void down(ActionEvent e) {
        myCircle.setCenterY(y+=5);
    }

    public void left(ActionEvent e) {
        myCircle.setCenterX(x-=5);
    }

    public void right(ActionEvent e) {
        myCircle.setCenterX(x+=5);
    }
}