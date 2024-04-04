package com.abdmoh123.chessgame.ui.controllers;

import java.util.ArrayList;
import java.util.List;

import com.abdmoh123.chessgame.Game;
import com.abdmoh123.chessgame.GameState;
import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.control.Human;
import com.abdmoh123.chessgame.control.Player;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.pieces.Piece;
import com.abdmoh123.chessgame.ui.components.BoardPane;
import com.abdmoh123.chessgame.ui.components.SpacePane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class MainController {
    @FXML private Text output_text;
    @FXML public BoardPane chess_board_pane;

    private Game chess_game;

    private Space selected_space;
    List<Space> spaces_to_move_to;

    public void initialize() {
        spaces_to_move_to = new ArrayList<>();
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
        // do nothing if it has already ended or if game hasn't started
        if (chess_game == null || chess_game.hasEnded()) return;

        chess_game.endGame(chess_game.isP1Turn());
        System.out.println("Game ended!");
    }

    @FXML protected void handleSpaceSelection(MouseEvent event) {
        // don't do anything if game hasn't started or if it has ended
        if (chess_game == null) return;
        if (chess_game.getState() != GameState.ACTIVE) return;

        // reset highlighting every time the player clicks a space
        List<SpacePane> pane_list = chess_board_pane.getCells();
        for (SpacePane pane : pane_list) {
            pane.resetBackground();
        }

        // get square/grid cell that the user clicked
        Node clicked_node = event.getPickResult().getIntersectedNode();
        // do nothing if clicked node is not a chess board space
        if (!(clicked_node instanceof SpacePane)) return;

        Space temp_space = getSpacePaneLocation((SpacePane) clicked_node);
        if (chess_game.getBoard().isSpaceFriendly(temp_space, chess_game.isP1Turn())) {
            this.selected_space = temp_space;
            char piece_symbol = chess_game.getBoard().getPiece(selected_space).getSymbol();
            System.out.printf("Found %s at cell: (%d, %d)\n", piece_symbol, selected_space.getX(), selected_space.getY());
            // highlight space in orange
            ((SpacePane) clicked_node).highlight("#d65d0e");
        }
        highlightMovablePanes(temp_space);
    }

    private Space getSpacePaneLocation(SpacePane pane) {
        Integer column_index = BoardPane.getColumnIndex(pane);
        Integer row_index = BoardPane.getRowIndex(pane);

        // convert to correct coordinates (following Space class)
        column_index -= 1;
        row_index = chess_board_pane.getSize() - row_index - 1;

        return new Space(column_index, row_index);
    }
    
    private void highlightMovablePanes(Space space_in) {
        Board chess_board = chess_game.getBoard();
        // do nothing if piece cannot be moved
        if (!chess_board.isSpaceFriendly(space_in, chess_game.isP1Turn())) return;

        Piece selected_piece = chess_board.getPiece(selected_space);
        List<Move> possible_moves = selected_piece.getPossibleMoves(selected_space, chess_game.getBoard());
        
        for (Move move : possible_moves) {
            Space new_location = move.getNewLocation();
            SpacePane pane = chess_board_pane.getCell(new_location.getX(), new_location.getY());
            // highlight the spaces in blue
            pane.highlight("#458588", "#076678");
        }
    }
}
