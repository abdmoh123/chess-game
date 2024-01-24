package com.abdmoh123.chessgame.moves;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.pieces.Pawn;

public class DoublePawnMove extends Move {
    public DoublePawnMove(Space old_location_in, Space new_location_in, Pawn piece_in) {
        super(old_location_in, new_location_in, piece_in);
    }

    @Override
    public void apply(Board chess_board) {
        Pawn moving_pawn = (Pawn) getMovingPiece();
        // allow pawn to be taken through en passant rule
        moving_pawn.setEnPassant(true);

        // update the board
        chess_board.updateSpace(getNewLocation(), moving_pawn);
        chess_board.updateSpace(getOldLocation(), null);
    }

    @Override
    public void reverse(Board chess_board) {
        Pawn moving_pawn = (Pawn) getMovingPiece();
        // prevent pawn from being taken through en passant rule
        moving_pawn.setEnPassant(false);

        // reverse double pawn move
        chess_board.updateSpace(getNewLocation(), null);
        chess_board.updateSpace(getOldLocation(), moving_pawn);
    }
}
