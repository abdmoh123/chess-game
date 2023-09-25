package main.moves;

import main.Space;
import main.boards.Board;
import main.pieces.King;
import main.pieces.Rook;

public class CastlingMove extends Move {
    private Space old_rook_space;
    private Space new_rook_space;
    public CastlingMove(Space old_location_in, Space new_location_in, Space old_rook_space_in, Space new_rook_space_in) {
        super(old_location_in, new_location_in);
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
    public String getMoveAsString(boolean is_check_in, boolean be_precise) {
        // castling is always this notation
        return "O-O";
    }

    @Override
    public void apply(Board chess_board) {
        // get the spaces included in the move
        Space old_space = getOldLocation();
        Space new_space = getNewLocation();
        Space old_rook_space = getOldRookSpace();
        Space new_rook_space = getNewRookSpace();

        // prevent future castling
        ((Rook) old_rook_space.getPiece()).activate();
        ((King) getChessPiece()).disableCastling();

        // apply the move by updating the board
        chess_board.updateSpace(new_space, old_space.getPiece());
        chess_board.updateSpace(old_space, null);
        chess_board.updateSpace(new_rook_space, old_rook_space.getPiece());
        chess_board.updateSpace(old_rook_space, null);
    }
}
