package com.abdmoh123.chessgame.pieces;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.moves.CastlingMove;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.moves.StandardMove;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private boolean castling_state;

    public King(boolean is_white_in) {
        super(is_white_in, 999);
        this.castling_state = false;
    }

    public boolean hasCastled() {
        return this.castling_state;
    }
    public void disableCastling() {
        this.castling_state = true;
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

        // no need to check for castling if condition below is true
        if (!isStartingPosition(location)) {
            return possible_moves;
        }
        
        // add castling moves if possible and available
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

        return possible_moves;
    }

    @Override
    public char getSymbol() {
        if (isWhite()) { return 'K'; }
        return 'k';
    }

    @Override
    public Piece copy() {
        King new_king = new King(isWhite());
        new_king.castling_state = hasCastled();
        new_king.setVisibleSpaces(getVisibleSpaces());
        return new_king;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof King) {
            if (hasCastled() != ((King)obj).hasCastled()) {
                return false;
            }
        }
        return super.equals(obj);
    }
}
