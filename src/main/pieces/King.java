package main.pieces;

import main.moves.CastlingMove;
import main.moves.Move;
import main.moves.StandardMove;
import main.boards.Board;
import main.boards.Space;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private boolean castling_state;

    public King(boolean is_white_in) {
        super(is_white_in, 999);
        this.castling_state = false;
    }

    private boolean isStartingPosition(Space location) {
        if (!hasCastled()) {
            if (location.getX() != 4) {
                return false;
            }
            if (isWhite()) {
                return location.getY() == 0;
            }
            return location.getY() == 7;
        }
        return false;
    }

    private boolean checkKingside(Space location, Board chess_board) {
        /* Check if king-side rook is deactivated (not moved) to allow castling */

        Space kingside_rook_space = new Space(7, location.getY());

        if (!(chess_board.getPiece(kingside_rook_space) instanceof Rook)) {
            return false;
        }

        // check if no pieces are between the king and king-side rook
        if (
            chess_board.isSpaceEmpty(new Space(6, location.getY())) &&
            chess_board.isSpaceEmpty(new Space(5, location.getY()))
        ) {
            // check if rook has moved
            return !((Rook) chess_board.getPiece(kingside_rook_space)).isActivated();
        }
        return false;
    }
    private boolean checkQueenside(Space location, Board chess_board) {
        /* Check if queen-side rook is deactivated (not moved) to allow castling */

        Space queenside_rook_space = new Space(0, location.getY());

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
            return !((Rook) chess_board.getPiece(queenside_rook_space)).isActivated();
        }
        return false;
    }

    @Override
    public void computeVision(Space location, Board chess_board) {
        /* Compute and find all spaces that are visible to the King */

        resetVision();

        int x_loc = location.getX();
        int y_loc = location.getY();

        // predetermined spaces king can move to (8 spaces around it)
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
            if (Move.isLegal(chess_board, location, space)) {
                addVisibleSpace(space);
            }
        }
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        computeVision(location, chess_board);

        for (Space visible_space : getVisibleSpaces()) {
            possible_moves.add(new StandardMove(location, visible_space, this, chess_board.getPiece(visible_space)));
        }

        // add castling moves if possible and available
        if (isStartingPosition(location)) {
            if (checkKingside(location, chess_board)) {
                Move kingside = new CastlingMove(
                    location,
                    new Space(6, location.getY()),
                    this,
                    new Space(7, location.getY()),
                    new Space(5, location.getY())
                );
                possible_moves.add(kingside);
            }
            if (checkQueenside(location, chess_board)) {
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
        return this.castling_state;
    }
    public void disableCastling() {
        this.castling_state = true;
    }
}
