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

    public PromotePawnMove(Space old_location_in, Space new_location_in, Pawn moving_piece_in, int promotion_choice) {
        super(old_location_in, new_location_in, moving_piece_in);
        setNewPiece(promotion_choice, moving_piece_in.isWhite());
    }
    public PromotePawnMove(
        Space old_location_in, Space new_location_in, Pawn moving_piece_in, int promotion_choice, Piece piece_killed
    ) {
        super(old_location_in, new_location_in, moving_piece_in, piece_killed);
        setNewPiece(promotion_choice, moving_piece_in.isWhite());
    }

    public Piece getNewPiece() {
        return this.new_piece;
    }
    private void setNewPiece(int choice, boolean is_white_in) {
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
    public String getNotation(boolean is_enemy_checked, boolean be_precise) {
        /* Overriden due to special case with promotion */

        String move_string = getNewLocation().toString();

        // add x if enemy piece is killed
        if (getKillPoints() > 0) {
            move_string = "x" + move_string;
        }

        // if multiple pieces of same type exist on the same row, column of moving piece is added to the string
        if (be_precise) {
            String old_location_x_axis = String.valueOf(getOldLocation().toString().charAt(0));
            move_string = old_location_x_axis + move_string;
        }

        // add promotion notation (e.g. a8=Q)
        char symbol;
        if (getNewPiece() instanceof Knight) {
            symbol = 'N';
        }
        else {
            symbol = getNewPiece().getName().charAt(0);
        }
        move_string += "=" + symbol;

        if (is_enemy_checked) {
            move_string += "+";
        }

        return move_string;
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
