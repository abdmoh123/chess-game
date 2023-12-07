package main.pieces;

import main.moves.Move;
import main.moves.StandardMove;
import main.boards.Board;
import main.boards.Space;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    private boolean activated;

    public Rook(boolean is_white_in) {
        super(is_white_in, 5);
        this.activated = false;
    }

    private boolean searchRookSpaces(
        Space location,
        Space next_space,
        Board chess_board
    ) {
        /* Search through the board for possible spaces the Rook can move to.
         * If the a piece is met (cannot go further), return false.
         */

        if (!Move.isLegal(chess_board, location, next_space)) {
            return false;
        }

        addVisibleSpace(next_space);
        // return false after finding a piece (cannot move any further)
        return chess_board.isSpaceEmpty(next_space);
    }

    @Override
    public void computeVision(Space location, Board chess_board) {
        /* Compute and find all spaces that are visible to the Rook */

        resetVision();

        int x_loc = location.getX();
        int y_loc = location.getY();

        // right straight
        boolean keep_searching = true;
        for (int i = x_loc + 1, j = y_loc + 1; i < 8 && j < 8; ++i, ++j) {
            if (!keep_searching) {
                break;
            }
            keep_searching = searchRookSpaces(location, new Space(i, y_loc), chess_board);
        }
        // up straight
        keep_searching = true;
        for (int j = y_loc + 1; j < 8; ++j) {
            if (!keep_searching) {
                break;
            }
            keep_searching = searchRookSpaces(location, new Space(x_loc, j), chess_board);
        }
        // left straight
        keep_searching = true;
        for (int i = x_loc - 1; i >= 0; --i) {
            if (!keep_searching) {
                break;
            }
            keep_searching = searchRookSpaces(location, new Space(i, y_loc), chess_board);
        }
        // down straight
        keep_searching = true;
        for (int j = y_loc - 1; j >= 0; --j) {
            if (!keep_searching) {
                break;
            }
            keep_searching = searchRookSpaces(location, new Space(x_loc, j), chess_board);
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
        return "Rook";
    }
    @Override
    public Piece copy() {
        Rook new_rook = new Rook(isWhite());
        new_rook.activated = isActivated();
        new_rook.setVisibleSpaces(getVisibleSpaces());
        return new_rook;
    }

    public boolean isActivated() {
        return this.activated;
    }
    public void activate() {
        this.activated = true;
    }
}
