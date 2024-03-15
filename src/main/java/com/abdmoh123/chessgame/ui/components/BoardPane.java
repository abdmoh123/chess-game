package com.abdmoh123.chessgame.ui.components;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.boards.StandardBoard;

import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BoardPane extends GridPane {
    public int size;
    GridPane board_spaces;

    // starts screwing up at 42
    private final int SPACE_SIZE = 50;

    public BoardPane(@NamedArg("size") int size) {
        this.size = size;
        this.board_spaces = new GridPane();

        // add axes labels
        add(getXAxis(), 1, 1);
        add(getYAxis(), 0, 0);
        // add actual chess board
        add(board_spaces, 1, 0);

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                // automatically determine whether or not the square should be light or dark
                this.board_spaces.add(new SpacePane((i - j) % 2 == 0, SPACE_SIZE), i, j);
            }
        }
    }
    public BoardPane() {
        this(8);
    }

    public void initialiseFEN(String fen_string_in) {
        Board chess_board = new StandardBoard();
        chess_board.initialiseFEN(fen_string_in);

        fillBoard(chess_board);
    }
    public void initialiseStandard() {
        Board chess_board = new StandardBoard();
        chess_board.initialise();
        
        fillBoard(chess_board);
    }
    private void fillBoard(Board chess_board_in) {
        // iterate through rows
        for (int j = 0; j < chess_board_in.getLength(); ++j) {
            // iterate through columns
            for (int i = 0; i < chess_board_in.getLength(); ++i) {
                Space board_space = new Space(i, j);
                SpacePane space_pane = getCell(i, j);

                if (!chess_board_in.isSpaceEmpty(board_space)) {
                    space_pane.setPieceImage(chess_board_in.getPiece(board_space).getSymbol());
                }
            }
        }
    }

    public SpacePane getCell(int column, int row) {
        /* Return cell based on inputted columns and rows (similar to Space class/Board.getPiece method) */

        // gridpane stores cells in format (row, column) instead of (column, row) like the Space class
        SpacePane space_pane = (SpacePane) board_spaces.getChildren().get((size - row - 1) + size * column);
        return space_pane;
    }

    public GridPane getBoardGrid() {
        return this.board_spaces;
    }

    private HBox getXAxis() {
        HBox x_labels = new HBox();

        char letter = 'a';
        for (int i = 0; i < this.size; ++i) {
            Label x_label = new Label(Character.toString(letter));
            x_label.setMinWidth(SPACE_SIZE);
            x_label.setAlignment(Pos.CENTER);
            x_label.setPadding(new Insets(5));

            x_labels.getChildren().add(x_label);
            ++letter;
        }
        return x_labels;
    }
    private VBox getYAxis() {
        VBox y_labels = new VBox();
        for (int i = 1; i < this.size + 1; ++i) {
            Label y_label = new Label(Integer.toString(i));
            y_label.setMinHeight(SPACE_SIZE);
            y_label.setAlignment(Pos.CENTER);
            // need more padding to have similar look to x axis labels
            y_label.setPadding(new Insets(10));

            y_labels.getChildren().add(y_label);
        }
        return y_labels;
    }
}
