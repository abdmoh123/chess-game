package com.abdmoh123.chessgame.ui.controllers;

import com.abdmoh123.chessgame.GameState;
import com.abdmoh123.chessgame.ui.components.BoardPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MainController {
    @FXML private Text output_text;
    @FXML public BoardPane chess_board;

    private GameState game_state;
    private boolean is_white_turn = true;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        output_text.setText("Chess!");
    }

    @FXML protected void startGame(ActionEvent event) {
        // do nothing if game is already active
        if (game_state == GameState.ACTIVE) return;

        game_state = GameState.ACTIVE;

        chess_board.initialiseStandard();
    }
    @FXML protected void endGame(ActionEvent event) {
        if (is_white_turn) game_state = GameState.BLACK_WIN;
        else game_state = GameState.WHITE_WIN;
    }
}
