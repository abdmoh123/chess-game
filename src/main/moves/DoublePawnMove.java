package main.moves;

import main.Space;
import main.boards.Board;
import main.pieces.Pawn;

public class DoublePawnMove extends Move {
    public DoublePawnMove(Space old_location_in, Space new_location_in, Pawn piece_in) {
        super(old_location_in, new_location_in, piece_in);
    }

    @Override
    public void apply(Board chess_board) {
        // get the spaces included in the move
        Space old_space = getOldLocation();
        Space new_space = getNewLocation();

        Pawn moving_pawn = (Pawn) getChessPiece();
        // allow pawn to be taken through en passant rule
        moving_pawn.setEnPassant(true);

        // apply the move by updating the board
        chess_board.updateSpace(new_space, moving_pawn);
        chess_board.updateSpace(old_space, null);
    }
}
