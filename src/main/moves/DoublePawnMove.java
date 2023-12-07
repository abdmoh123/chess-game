package main.moves;

import main.boards.Board;
import main.boards.Space;
import main.pieces.Pawn;

public class DoublePawnMove extends Move {
    public DoublePawnMove(Space old_location_in, Space new_location_in, Pawn piece_in) {
        super(old_location_in, new_location_in, piece_in);
    }

    @Override
    public void apply(Board chess_board) {
        Pawn moving_pawn = (Pawn) getChessPiece();
        // allow pawn to be taken through en passant rule
        moving_pawn.setEnPassant(true);

        // update the board
        chess_board.updateSpace(getNewLocation(), moving_pawn);
        chess_board.updateSpace(getOldLocation(), null);
    }
}
