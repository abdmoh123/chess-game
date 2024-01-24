package com.abdmoh123.chessgame.moves;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.pieces.Pawn;

public class EnPassantMove extends Move {
    private final Space KILLED_PAWN_SPACE;
    public EnPassantMove(
        Space old_location_in, Space new_location_in, Space killed_pawn_location_in, Pawn moving_piece_in, Pawn killed_piece_in
    ) {
        super(old_location_in, new_location_in, moving_piece_in, killed_piece_in);
        this.KILLED_PAWN_SPACE = killed_pawn_location_in;
    }

    public Space getKilledPawnSpace() {
        return this.KILLED_PAWN_SPACE;
    }

    @Override
    public void apply(Board chess_board) {
        // update the board
        chess_board.updateSpace(getNewLocation(), getMovingPiece());
        chess_board.updateSpace(getOldLocation(), null);
        chess_board.updateSpace(getKilledPawnSpace(), null);
    }

    @Override
    public void reverse(Board chess_board) {
        // killed pawn is brought back an is en-passant-able again
        Pawn killed_pawn = (Pawn) getKilledPiece();
        killed_pawn.setEnPassant(true);

        // reverse the en passant
        chess_board.updateSpace(getNewLocation(), null);
        chess_board.updateSpace(getOldLocation(), getMovingPiece());
        chess_board.updateSpace(getKilledPawnSpace(), killed_pawn);
    }
}
