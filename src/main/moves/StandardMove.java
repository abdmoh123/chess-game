package main.moves;

import main.boards.Board;
import main.boards.Space;
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
        Space old_space = getOldLocation();
        Space new_space = getNewLocation();

        Piece moving_piece = getChessPiece();

        // prevent castling if rook moves
        if (moving_piece instanceof Rook) {
            ((Rook) moving_piece).activate();
        }
        // prevent castling if king moves
        if (moving_piece instanceof King) {
            ((King) moving_piece).disableCastling();
        }

        // update the board
        chess_board.updateSpace(new_space, moving_piece);
        chess_board.updateSpace(old_space, null);
    }
}
