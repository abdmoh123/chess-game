package main.pieces;

import main.moves.CastlingMove;
import main.moves.Move;
import main.moves.StandardMove;
import main.Space;
import main.boards.Board;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private boolean castling_state;

    public King(boolean is_white_in) {
        super(is_white_in, 999);
        this.castling_state = false;
    }

    private boolean is_starting_position(Space location) {
        // check if castling was already done
        if (!hasCastled()) {
            if (isWhite()) {
                // check if white king is at starting state
                return location.getX() == 4 && location.getY() == 0;
            }
            else {
                // check if black king is at starting state
                return location.getX() == 4 && location.getY() == 7;
            }
        }
        return false;
    }

    /* Check if rooks are deactivated (not moved) to allow castling */
    private boolean check_kingside(Space location, Board chess_board) {
        Space kingside_rook_space = new Space(7, location.getY());

        // check if king-side space has a rook
        if (!(chess_board.getPiece(kingside_rook_space) instanceof Rook)) {
            return false;
        }
        // check if no pieces are between the king and king-side rook
        if (
            chess_board.isSpaceEmpty(new Space(6, location.getY())) &&
            chess_board.isSpaceEmpty(new Space(5, location.getY()))
        ) {
            // check if rook has moved
            return !((Rook) chess_board.getPiece(kingside_rook_space)).is_activated();
        }
        return false;
    }
    private boolean check_queenside(Space location, Board chess_board) {
        Space queenside_rook_space = new Space(0, location.getY());

        // check if queen-side space has a rook
        if (!(chess_board.getPiece(queenside_rook_space) instanceof Rook)) {
            return false;
        }
        // check if no pieces are between the king and queen-side rook
        if (
            chess_board.isSpaceEmpty(new Space(1, location.getY())) &&
            chess_board.isSpaceEmpty(new Space(2, location.getY())) &&
            chess_board.isSpaceEmpty(new Space(3, location.getY()))
        ) {
            // check if rook has moved
            return !((Rook) chess_board.getPiece(queenside_rook_space)).is_activated();
        }
        return false;
    }

    @Override
    public void computeVision(Space location, Board chess_board) {
        resetVision();

        int x_loc = location.getX();
        int y_loc = location.getY();

        int[][] space_coordinates = {
            {x_loc + 1, y_loc},
            {x_loc + 1, y_loc - 1},
            {x_loc, y_loc - 1},
            {x_loc - 1, y_loc - 1},
            {x_loc - 1, y_loc},
            {x_loc - 1, y_loc + 1},
            {x_loc, y_loc + 1},
            {x_loc + 1, y_loc + 1}
        };

        for (int[] set : space_coordinates) {
            Space space = new Space(set[0], set[1]);
            // ensure move follows the rules
            if (Move.is_legal(chess_board, location, space)) {
                addVisibleSpace(space);
            }
        }
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        // get all spaces visible by the king
        computeVision(location, chess_board);

        // convert all visible spaces to moves
        for (Space visible_space : getVisibleSpaces()) {
            possible_moves.add(new StandardMove(location, visible_space, this, chess_board.getPiece(visible_space)));
        }

        // add castling moves if possible and available
        if (is_starting_position(location)) {
            if (check_kingside(location, chess_board)) {
                Move kingside = new CastlingMove(
                    location,
                    new Space(6, location.getY()),
                    this,
                    new Space(7, location.getY()),
                    new Space(5, location.getY())
                );
                possible_moves.add(kingside);
            }
            if (check_queenside(location, chess_board)) {
                Move queenside = new CastlingMove(
                    location,
                    new Space(2, location.getY()),
                    this,
                    new Space(0, location.getY()),
                    new Space(3, location.getY())
                );
                possible_moves.add(queenside);
            }
        }

        return possible_moves;
    }

    @Override
    public String getName() {
        return "King";
    }

    @Override
    public Piece clone() {
        King new_king = new King(isWhite());
        new_king.castling_state = hasCastled();
        new_king.setVisibleSpaces(getVisibleSpaces());
        return new_king;
    }

    public boolean hasCastled() {
        return castling_state;
    }
    public void disableCastling() {
        this.castling_state = true;
    }
}
