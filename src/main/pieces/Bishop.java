package main.pieces;

import main.moves.Move;
import main.moves.StandardMove;
import main.Space;
import main.boards.Board;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(boolean is_white_in) {
        super(is_white_in, 3);
    }

    private boolean search_bishop_spaces(
        Space location,
        Space next_space,
        Board chess_board
    ) {
        // check move is legal
        if (!Move.isLegal(chess_board, location, next_space)) {
            return false;
        }

        addVisibleSpace(next_space);
        // stops search after finding a piece (doesn't have x-ray vision)
        return chess_board.isSpaceEmpty(next_space);
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
            keep_searching = search_bishop_spaces(location, new Space(i, j), chess_board);
        }
        // down-right diagonal
        keep_searching = true;
        for (int i = x_loc + 1, j = y_loc - 1; i < 8 && j >= 0; ++i, --j) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_bishop_spaces(location, new Space(i, j), chess_board);
        }
        // down-left diagonal
        keep_searching = true;
        for (int i = x_loc - 1, j = y_loc - 1; i >= 0 && j >= 0; --i, --j) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_bishop_spaces(location, new Space(i, j), chess_board);
        }
        // up-left diagonal
        keep_searching = true;
        for (int i = x_loc - 1, j = y_loc + 1; i >= 0 && j < 8; --i, ++j) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_bishop_spaces(location, new Space(i, j), chess_board);
        }
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        computeVision(location, chess_board);

        for (Space visible_space : getVisibleSpaces()) {
            possible_moves.add(new StandardMove(location, visible_space, this, chess_board.getPiece(visible_space)));
        }

        return possible_moves;
    }

    @Override
    public String getName() {
        return "Bishop";
    }

    @Override
    public Piece clone() {
        return new Bishop(isWhite());
    }
}
