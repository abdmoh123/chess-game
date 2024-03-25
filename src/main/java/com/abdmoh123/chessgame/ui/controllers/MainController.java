package com.abdmoh123.chessgame.ui.controllers;

import com.abdmoh123.chessgame.Game;
import com.abdmoh123.chessgame.GameState;
import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.control.Human;
import com.abdmoh123.chessgame.control.Player;
import com.abdmoh123.chessgame.ui.components.BoardPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class MainController {
    @FXML private Text output_text;
    @FXML public BoardPane chess_board_pane;

    private Game chess_game;

    public void initialize() {
    }
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        output_text.setText("Chess!");
    }

    @FXML protected void startGame(ActionEvent event) {
        // do nothing if game is already active
        if (chess_game != null && chess_game.getState() == GameState.ACTIVE) return;

        Board chess_board = new StandardBoard();
        chess_board.initialise();
        chess_game = new Game(new Player[]{new Human(true), new Human(false)}, chess_board);

        chess_board_pane.initialise(chess_board);
        System.out.println("Game started!");
    }
    
    @FXML protected void endGame(ActionEvent event) {
        chess_game.endGame(chess_game.isP1Turn());
        System.out.println("Game ended!");
    }

    @FXML protected void selectPiece(MouseEvent event) {
        // don't do anything if game hasn't started or if it has ended
        if (chess_game == null) return;
        if (chess_game.getState() != GameState.ACTIVE) return;

        // get square/grid cell that the user clicked
        Node clicked_node = event.getPickResult().getIntersectedNode();
        if (clicked_node != chess_board_pane) {
            Integer column_index = BoardPane.getColumnIndex(clicked_node);
            Integer row_index = BoardPane.getRowIndex(clicked_node);

            // don't do anything if either coordinate value is null
            if (column_index == null | row_index == null) return;
            
            // convert to correct coordinates (following Space class)
            column_index -= 1;
            row_index = chess_board_pane.getSize() - row_index - 1;

            // don't do anything if either coordinate is out of bounds
            if (column_index < 0 || row_index < 0) return;

            Space selected_space = new Space(column_index, row_index);
            if (chess_game.getBoard().isSpaceFriendly(selected_space, chess_game.isP1Turn())) {
                char piece_symbol = chess_game.getBoard().getPiece(selected_space).getSymbol();
                System.out.printf("Found %s at cell: (%d, %d)\n", piece_symbol, column_index, row_index);
            }
        }
    }
}
