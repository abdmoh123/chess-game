package main.pieces;

import main.Board;
import main.moves.Move;
import main.moves.StandardMove;
import main.Space;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(boolean is_white_in) {
        super(is_white_in, 3);
    }

    private boolean search_bishop_spaces(
        Space location,
        Board chess_board,
        int x_loc,
        int y_loc
    ) {
        Space next_space = chess_board.getSpace(x_loc, y_loc);

        // check move is legal
        if (!Move.is_legal(location, next_space)) {
            return false;
        }

        addVisibleSpace(next_space);
        // stops search after finding a piece (doesn't have x-ray vision)
        return next_space.isEmpty();
    }

    @Override
    public void computeVision(Space location, Board chess_board) {
        resetVision();

        int x_loc = location.getX();
        int y_loc = location.getY();

        // up-right diagonal
        boolean keep_searching = true;
        for (int i = x_loc + 1, j = y_loc + 1; i < 8 && j < 8; ++i, ++j) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_bishop_spaces(location, chess_board, i, j);
        }
        // down-right diagonal
        keep_searching = true;
        for (int i = x_loc + 1, j = y_loc - 1; i < 8 && j >= 0; ++i, --j) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_bishop_spaces(location, chess_board, i, j);
        }
        // down-left diagonal
        keep_searching = true;
        for (int i = x_loc - 1, j = y_loc - 1; i >= 0 && j >= 0; --i, --j) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_bishop_spaces(location, chess_board, i, j);
        }
        // up-left diagonal
        keep_searching = true;
        for (int i = x_loc - 1, j = y_loc + 1; i >= 0 && j < 8; --i, ++j) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_bishop_spaces(location, chess_board, i, j);
        }
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        computeVision(location, chess_board);

        for (Space visible_space : getVisibleSpaces()) {
            possible_moves.add(new StandardMove(location, visible_space));
        }

        return possible_moves;
    }

    @Override
    public String getName() {
        return "Bishop";
    }
}
