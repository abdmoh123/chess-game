package com.abdmoh123.chessgame.ui.components;

import java.util.ArrayList;
import java.util.List;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.moves.Move;

import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class BoardPane extends GridPane {
    private int size;

    // starts screwing up at 42
    private final int SPACE_SIZE = 50;

    public BoardPane(@NamedArg("size") int size) {
        this.size = size;

        // add axes labels
        List<String> x_label_list = generateXAxis();
        for (int i = 0; i < x_label_list.size(); ++i) {
            Label x_label = new Label(x_label_list.get(i));

            x_label.setMinWidth(SPACE_SIZE);
            x_label.setAlignment(Pos.CENTER);

            add(x_label, i + 1, size);
        }
        for (int i : generateYAxis()) {
            Label y_label = new Label(Integer.toString(i));

            y_label.setMinHeight(SPACE_SIZE);
            y_label.setAlignment(Pos.CENTER);
            y_label.setPadding(new Insets(7));

            add(y_label, 0, size - i);
        }

        // add actual chess board
        for (int i = 1; i < size + 1; ++i) {
            for (int j = 0; j < size; ++j) {
                // automatically determine whether or not the square should be light or dark
                add(new SpacePane((i - j) % 2 == 0, SPACE_SIZE), i, j);
            }
        }
    }
    public BoardPane() {
        this(8);
    }

    public void initialiseFEN(String fen_string_in) {
        Board chess_board = new StandardBoard();
        chess_board.initialiseFEN(fen_string_in);

        initialise(chess_board);
    }
    public void initialiseStandard() {
        Board chess_board = new StandardBoard();
        chess_board.initialise();
        
        initialise(chess_board);
    }
    public void initialise(Board chess_board_in) {
        // iterate through columns
        for (int i = 0; i < chess_board_in.getLength(); ++i) {
            // iterate through rows
            for (int j = 0; j < chess_board_in.getLength(); ++j) {
                Space board_space = new Space(i, j);
                SpacePane space_pane = getCell(i, j);

                if (!chess_board_in.isSpaceEmpty(board_space)) {
                    space_pane.setPieceImage(chess_board_in.getPiece(board_space).getSymbol());
                }
            }
        }
    }

    public void reset() {
        /* Resets the chess board (makes it empty) */
        
        for (int i = 0; i < getSize(); ++i) {
            for (int j = 0; j < getSize(); ++j) {
                // set space image to null (empty)
                updateCell(new Space(i, j), ' ');
            }
        }
    }

    private List<String> generateXAxis() {
        List<String> x_labels = new ArrayList<>();

        char letter = 'a';
        for (int i = 0; i < this.size; ++i) {
            x_labels.add(Character.toString(letter));
            ++letter;
        }
        return x_labels;
    }
    private List<Integer> generateYAxis() {
        List<Integer> y_labels = new ArrayList<>();

        for (int i = 1; i < this.size + 1; ++i) y_labels.add(i);
        return y_labels;
    }

    public SpacePane getCell(Space space) {
        return getCell(space.getX(), space.getY());
    }
    public SpacePane getCell(int column, int row) {
        /* Return cell based on inputted columns and rows (similar to Space class/Board.getPiece method) */

        // gridpane stores cells in format (row, column) instead of (column, row) like the Space class
        SpacePane space_pane = (SpacePane) getChildren().get((size - row - 1) + size * (column + 2));
        return space_pane;
    }
    public List<SpacePane> getCells() {
        List<SpacePane> panes = new ArrayList<>();
        for (int i = 0; i < getSize(); ++i) {
            for (int j = 0; j < getSize(); ++j) {
                panes.add(getCell(i, j));
            }
        }
        return panes;
    }

    public void updateCell(Space coordinate, char piece_symbol) {
        SpacePane space_pane = getCell(coordinate);
        space_pane.setPieceImage(piece_symbol);
    }

    public void applyMove(Move move_in) {
        SpacePane old_cell = getCell(move_in.getOldLocation());
        SpacePane new_cell = getCell(move_in.getNewLocation());

        old_cell.setPieceImage(' ');
        new_cell.setPieceImage(move_in.getChessPiece().getSymbol());
    }

    public int getSize() { return this.size; }
}
