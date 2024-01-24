package com.abdmoh123.chessgame.moves;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.pieces.Bishop;
import com.abdmoh123.chessgame.pieces.Knight;
import com.abdmoh123.chessgame.pieces.Pawn;
import com.abdmoh123.chessgame.pieces.Piece;
import com.abdmoh123.chessgame.pieces.Queen;
import com.abdmoh123.chessgame.pieces.Rook;

public class PromotePawnMove extends Move {
    private Piece new_piece;

    public PromotePawnMove(Space old_location_in, Space new_location_in, Pawn moving_piece_in) {
        super(old_location_in, new_location_in, moving_piece_in);
    }
    public PromotePawnMove(Space old_location_in, Space new_location_in, Pawn moving_piece_in, Piece piece_killed) {
        super(old_location_in, new_location_in, moving_piece_in, piece_killed);
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

    @Override
    public void reverse(Board chess_board) {
        // update the board
        chess_board.updateSpace(getNewLocation(), getKilledPiece()); // can be null
        chess_board.updateSpace(getOldLocation(), getMovingPiece());
    }
}
