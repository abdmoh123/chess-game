package main.java.moves;

import main.java.boards.Board;
import main.java.boards.Space;
import main.java.pieces.Bishop;
import main.java.pieces.Knight;
import main.java.pieces.Pawn;
import main.java.pieces.Piece;
import main.java.pieces.Queen;
import main.java.pieces.Rook;

public class PromotePawnMove extends Move {
    private Piece new_piece;

    public PromotePawnMove(Space old_location_in, Space new_location_in, Pawn piece_in) {
        super(old_location_in, new_location_in, piece_in);
    }

    public Piece getNewPiece() {
        return this.new_piece;
    }
    public void setNewPiece(int choice, boolean is_white_in) {
        /* Set the piece that the pawn promotes to */

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

    @Override
    public void apply(Board chess_board) {
        // update the board
        chess_board.updateSpace(getNewLocation(), getNewPiece());
        chess_board.updateSpace(getOldLocation(), null);
    }
}
