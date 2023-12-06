package main.moves;

import main.boards.Board;
import main.boards.Space;
import main.pieces.King;
import main.pieces.Rook;

public class CastlingMove extends Move {
    private Space old_rook_space;
    private Space new_rook_space;
    public CastlingMove(Space old_location_in, Space new_location_in, King piece_in, Space old_rook_space_in, Space new_rook_space_in) {
        super(old_location_in, new_location_in, piece_in);
        this.old_rook_space = old_rook_space_in;
        this.new_rook_space = new_rook_space_in;
    }

    public Space getOldRookSpace() {
        return this.old_rook_space;
    }
    public Space getNewRookSpace() {
        return this.new_rook_space;
    }

    @Override
    public String getNotation(boolean is_check_in, boolean be_precise) {
        // if castling king-side, move = "O-O"
        if (old_rook_space.getX() == 7) {
            return "O-O";
        }
        // if castling queen-side, move = "O-O-O"
        return "O-O-O";
    }

    @Override
    public void apply(Board chess_board) {
        Space old_space = getOldLocation();
        Space new_space = getNewLocation();
        Space old_rook_space = getOldRookSpace();
        Space new_rook_space = getNewRookSpace();

        Rook moving_rook = (Rook) chess_board.getPiece(old_rook_space);
        King moving_king = (King) getChessPiece();
        // prevent future castling
        moving_rook.activate();
        moving_king.disableCastling();

        // update the board
        chess_board.updateSpace(new_space, moving_king);
        chess_board.updateSpace(old_space, null);
        chess_board.updateSpace(new_rook_space, moving_rook);
        chess_board.updateSpace(old_rook_space, null);
    }
}
