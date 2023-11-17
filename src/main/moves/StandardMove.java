package main.moves;

import main.Space;
import main.boards.Board;
import main.pieces.King;
import main.pieces.Piece;
import main.pieces.Rook;

public class StandardMove extends Move {

    public StandardMove(Space old_location_in, Space new_location_in, Piece piece_in) {
        super(old_location_in, new_location_in, piece_in);
    }
    public StandardMove(Space old_location_in, Space new_location_in, Piece piece_in, Piece piece_killed) {
        super(old_location_in, new_location_in, piece_in, piece_killed);
    }

    @Override
    public void apply(Board chess_board) {
        // get the spaces included in the move
        Space old_space = getOldLocation();
        Space new_space = getNewLocation();

        Piece piece_copy = getChessPiece().clone();

        // prevent castling on moved rooks
        if (piece_copy instanceof Rook) {
            ((Rook) piece_copy).activate();
        }
        // prevent castling if king moves
        if (piece_copy instanceof King) {
            ((King) piece_copy).disableCastling();
        }

        // apply the move by updating the board
        chess_board.updateSpace(new_space, piece_copy);
        chess_board.updateSpace(old_space, null);
    }
}
