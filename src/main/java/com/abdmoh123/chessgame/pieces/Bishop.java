package com.abdmoh123.chessgame.pieces;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.moves.StandardMove;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(boolean is_white_in) {
        super(is_white_in, 3);
    }

    private boolean searchBishopSpaces(
        Space location,
        Space next_space,
        Board chess_board
    ) {
        /* Search through the board for possible spaces the Bishop can move to.
         * If the a piece is met (cannot go further), return false.
         */

        if (!Move.isValid(chess_board, location, next_space)) {
            return false;
        }

        addVisibleSpace(next_space);
        // return false after finding a piece (cannot move any further)
        return chess_board.isSpaceEmpty(next_space);
    }

    @Override
    public void computeVision(Space location, Board chess_board) {
        /* Compute and find all spaces that are visible to the Bishop */

        resetVision();

        int x_loc = location.getX();
        int y_loc = location.getY();

        // up-right diagonal
        boolean keep_searching = true;
        for (int i = x_loc + 1, j = y_loc + 1; i < 8 && j < 8; ++i, ++j) {
            if (!keep_searching) {
                break;
            }
            keep_searching = searchBishopSpaces(location, new Space(i, j), chess_board);
        }
        // down-right diagonal
        keep_searching = true;
        for (int i = x_loc + 1, j = y_loc - 1; i < 8 && j >= 0; ++i, --j) {
            if (!keep_searching) {
                break;
            }
            keep_searching = searchBishopSpaces(location, new Space(i, j), chess_board);
        }
        // down-left diagonal
        keep_searching = true;
        for (int i = x_loc - 1, j = y_loc - 1; i >= 0 && j >= 0; --i, --j) {
            if (!keep_searching) {
                break;
            }
            keep_searching = searchBishopSpaces(location, new Space(i, j), chess_board);
        }
        // up-left diagonal
        keep_searching = true;
        for (int i = x_loc - 1, j = y_loc + 1; i >= 0 && j < 8; --i, ++j) {
            if (!keep_searching) {
                break;
            }
            keep_searching = searchBishopSpaces(location, new Space(i, j), chess_board);
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
    public Piece copy() {
        Bishop new_bishop = new Bishop(isWhite());
        new_bishop.setVisibleSpaces(getVisibleSpaces());
        return new_bishop;
    }
}
