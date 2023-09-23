package main.moves;

import main.Space;
import main.pieces.Piece;

public class PromotePawnMove extends Move {
    private final Piece new_piece;

    public PromotePawnMove(Space old_location_in, Space new_location_in, Piece new_piece_in) {
        super(old_location_in, new_location_in);
        this.new_piece = new_piece_in;
    }

    public Piece getNewPiece() {
        return this.new_piece;
    }
}
