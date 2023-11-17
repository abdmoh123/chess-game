package main.pieces;

import main.moves.Move;
import main.moves.StandardMove;
import main.Space;
import main.boards.Board;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    private boolean activated;

    public Rook(boolean is_white_in) {
        super(is_white_in, 5);
        this.activated = false;
    }

    private boolean search_rook_spaces(
        Space location,
        Space next_space,
        Board chess_board
    ) {
        // check horizontal move is legal
        if (!Move.is_legal(chess_board, location, next_space)) {
            return false;
        }
        // add space as visible
        addVisibleSpace(next_space);
        // if current space isn't empty, rook cannot see any more pieces
        return chess_board.isSpaceEmpty(next_space);
    }

    @Override
    public void computeVision(Space location, Board chess_board) {
        resetVision();

        int x_loc = location.getX();
        int y_loc = location.getY();

        // right straight
        boolean keep_searching = true;
        for (int i = x_loc + 1, j = y_loc + 1; i < 8 && j < 8; ++i, ++j) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_rook_spaces(location, new Space(i, y_loc), chess_board);
        }
        // up straight
        keep_searching = true;
        for (int j = y_loc + 1; j < 8; ++j) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_rook_spaces(location, new Space(x_loc, j), chess_board);
        }
        // left straight
        keep_searching = true;
        for (int i = x_loc - 1; i >= 0; --i) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_rook_spaces(location, new Space(i, y_loc), chess_board);
        }
        // down straight
        keep_searching = true;
        for (int j = y_loc - 1; j >= 0; --j) {
            // stop searching after finding a piece
            if (!keep_searching) {
                break;
            }
            keep_searching = search_rook_spaces(location, new Space(x_loc, j), chess_board);
        }
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        // get all spaces visible by the rook
        computeVision(location, chess_board);

        // convert the visible spaces into moves
        for (Space visible_space : getVisibleSpaces()) {
            // if (chess_board.isSpaceEmpty(visible_space)) {
            //     possible_moves.add(new StandardMove(location, visible_space, this));
            // }
            // else {
            //     possible_moves.add(new StandardMove(location, visible_space, this, chess_board.getPiece(visible_space)));
            // }
            possible_moves.add(new StandardMove(location, visible_space, this, chess_board.getPiece(visible_space)));
        }

        return possible_moves;
    }

    @Override
    public String getName() {
        return "Rook";
    }
    @Override
    public Piece clone() {
        Rook new_rook = new Rook(isWhite());
        new_rook.activated = this.activated;
        new_rook.setVisibleSpaces(getVisibleSpaces());
        return new_rook;
    }

    public boolean is_activated() {
        return activated;
    }
    public void activate() {
        this.activated = true;
    }
}
