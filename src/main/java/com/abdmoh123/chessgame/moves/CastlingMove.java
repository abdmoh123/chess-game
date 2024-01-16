package com.abdmoh123.chessgame.moves;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.pieces.King;
import com.abdmoh123.chessgame.pieces.Rook;

public class CastlingMove extends Move {
    private final Space OLD_ROOK_SPACE;
    private final Space NEW_ROOK_SPACE;

    public CastlingMove(Space old_location_in, Space new_location_in, King piece_in, Space old_rook_space_in, Space new_rook_space_in) {
        super(old_location_in, new_location_in, piece_in);
        this.OLD_ROOK_SPACE = old_rook_space_in;
        this.NEW_ROOK_SPACE = new_rook_space_in;
    }

    public Space getOldRookSpace() {
        return this.OLD_ROOK_SPACE;
    }
    public Space getNewRookSpace() {
        return this.NEW_ROOK_SPACE;
    }

    @Override
    public String getNotation(boolean is_check_in, boolean be_precise) {
        // if castling king-side, move = "O-O"
        if (getOldRookSpace().getX() == 7) {
            return "O-O";
        }
        // if castling queen-side, move = "O-O-O"
        return "O-O-O";
    }

    @Override
    public void apply(Board chess_board) {
        Rook moving_rook = (Rook) chess_board.getPiece(getOldRookSpace());
        King moving_king = (King) getChessPiece();
        // prevent future castling
        moving_rook.activate();
        moving_king.disableCastling();

        // update the board
        chess_board.updateSpace(getNewLocation(), moving_king);
        chess_board.updateSpace(getOldLocation(), null);
        chess_board.updateSpace(getNewRookSpace(), moving_rook);
        chess_board.updateSpace(getOldRookSpace(), null);
    }
}
