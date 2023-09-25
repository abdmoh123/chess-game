package main.moves;

import main.Space;
import main.boards.Board;
import main.pieces.King;
import main.pieces.Rook;

public class StandardMove extends Move {

    public StandardMove(Space old_location_in, Space new_location_in) {
        super(old_location_in, new_location_in);
    }

    @Override
    public void apply(Board chess_board) {
        // get the spaces included in the move
        Space old_space = getOldLocation();
        Space new_space = getNewLocation();

        // prevent castling on moved rooks
        if (getChessPiece() instanceof Rook) {
            ((Rook) getChessPiece()).activate();
        }
        // prevent castling if king moves
        if (getChessPiece() instanceof King) {
            ((King) getChessPiece()).disableCastling();
        }

        // apply the move by updating the board
        chess_board.updateSpace(new_space, old_space.getPiece());
        chess_board.updateSpace(old_space, null);
    }
}
