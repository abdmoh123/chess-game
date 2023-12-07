package com.abdmoh123.chessgame.moves;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.pieces.Pawn;

public class EnPassantMove extends Move {
    private final Space KILLED_PAWN_SPACE;
    public EnPassantMove(Space old_location_in, Space new_location_in, Space killed_pawn_location_in, Pawn piece_in, Pawn piece_killed) {
        super(old_location_in, new_location_in, piece_in, piece_killed);
        this.KILLED_PAWN_SPACE = killed_pawn_location_in;
        // override points to be 1 (new location is not where the killed pawn is)
        setKillPoints(1);
    }

    public Space getKilledPawnSpace() {
        return this.KILLED_PAWN_SPACE;
    }

    @Override
    public void apply(Board chess_board) {
        // update the board
        chess_board.updateSpace(getNewLocation(), getChessPiece());
        chess_board.updateSpace(getOldLocation(), null);
        chess_board.updateSpace(getKilledPawnSpace(), null);
    }
}
