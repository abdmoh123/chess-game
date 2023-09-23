package main.moves;

import main.Space;

public class EnPassantMove extends Move {
    private Space killed_pawn_space;
    public EnPassantMove(Space old_location_in, Space new_location_in, Space killed_pawn_space_in) {
        super(old_location_in, new_location_in);
        this.killed_pawn_space = killed_pawn_space_in;
        // override points to be 1 (new location is not where the killed pawn is)
        setKillPoints(1);
    }

    public Space get_killed_pawn_space() {
        return killed_pawn_space;
    }
}
