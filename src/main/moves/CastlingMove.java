package main.moves;

import main.Space;

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
}
