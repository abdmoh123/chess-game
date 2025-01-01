package com.abdmoh123.chessgame.javafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class NewGameDialogController extends GridPane {
    private MainController mainController;

    @FXML
    private Text output_text;

    public NewGameDialogController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/new_game_form.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void startGame() {
        this.output_text.setText("Chess!");
        mainController.startGame();
    }
}
