package com.abdmoh123.chessgame.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MainController {
    @FXML private Text output_text;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        output_text.setText("Chess!");
    }
}
