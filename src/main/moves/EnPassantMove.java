package main.moves;

import main.Space;
import main.boards.Board;

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

    @Override
    public void apply(Board chess_board) {
        // get the spaces included in the move
        Space old_space = getOldLocation();
        Space new_space = getNewLocation();
        Space pawn_space = get_killed_pawn_space();

        // apply the move by updating the board
        chess_board.updateSpace(new_space, old_space.getPiece());
        chess_board.updateSpace(old_space, null);
        chess_board.updateSpace(pawn_space, null);
    }
}