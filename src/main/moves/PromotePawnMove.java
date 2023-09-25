package main.moves;

import main.Space;
import main.pieces.Bishop;
import main.pieces.Knight;
import main.pieces.Piece;
import main.pieces.Queen;
import main.pieces.Rook;

public class PromotePawnMove extends Move {
    private Piece new_piece;

    public PromotePawnMove(Space old_location_in, Space new_location_in) {
        super(old_location_in, new_location_in);
    }

    public Piece getNewPiece() {
        return this.new_piece;
    }
    public void setNewPiece(int choice, boolean is_white_in) {
        switch (choice) {
            case 1:
                this.new_piece = new Queen(is_white_in);
                break;
            case 2:
                this.new_piece = new Rook(is_white_in);
                break;
            case 3:
                this.new_piece = new Bishop(is_white_in);
                break;
            case 4:
                this.new_piece = new Knight(is_white_in);
                break;
            default:
                throw new RuntimeException("Invalid choice for promoted pawn!");
        }
    }
}
