package com.abdmoh123.chessgame.moves;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.pieces.Knight;
import com.abdmoh123.chessgame.pieces.Pawn;
import com.abdmoh123.chessgame.pieces.Piece;

public abstract class Move {
    private final Piece MOVING_PIECE;
    private final Piece KILLED_PIECE;
    private final Space OLD_LOCATION;
    private final Space NEW_LOCATION;
    private int kill_points;

    public Move(Space old_location_in, Space new_location_in, Piece moving_piece_in) {
        // Only use for quiet moves (no piece killed)
        this.OLD_LOCATION = old_location_in;
        this.NEW_LOCATION = new_location_in;
        this.MOVING_PIECE = moving_piece_in;
        this.KILLED_PIECE = null;
        this.kill_points = 0;
    }
    public Move(Space old_location_in, Space new_location_in, Piece moving_piece_in, Piece killed_piece_in) {
        // Useful when capturing material (piece killed)
        this.OLD_LOCATION = old_location_in;
        this.NEW_LOCATION = new_location_in;
        this.MOVING_PIECE = moving_piece_in;
        if (killed_piece_in != null) {
            this.KILLED_PIECE = killed_piece_in;
            this.kill_points = killed_piece_in.getValue();
        }
        else {
            this.KILLED_PIECE = null;
            this.kill_points = 0;
        }
    }

    public boolean isPseudoLegal(Board chess_board) {
        /* More thorough check for legality of move (excludes checks) */
        
        if (!isValid(chess_board, OLD_LOCATION, NEW_LOCATION)) {
            return false;
        }

        if (!getMovingPiece().equals(chess_board.getPiece(getOldLocation()))) {
            return false;
        }
        if (
            getKilledPiece() != null &&
            !(this instanceof EnPassantMove) &&
            !getKilledPiece().equals(chess_board.getPiece(getNewLocation()))
        ) {
            return false;
        }

        return getMovingPiece().canMove(getOldLocation(), getNewLocation(), chess_board);
    }
    public static boolean isValid(Board chess_board, Space old_location_in, Space new_location_in) {
        /* Quick and simple validity check without creating a move */

        // ensure moves stay within the predefined board coordinates and are not null
        if (!chess_board.isSpaceValid(old_location_in)) {
            return false;
        }
        if (!chess_board.isSpaceValid(new_location_in)) {
            return false;
        }

        if (old_location_in.getX() == new_location_in.getX() && old_location_in.getY() == new_location_in.getY()) {
            return false;
        }

        // prevent piece from friendly fire
        if (chess_board.isSpaceFriendly(new_location_in, chess_board.getPiece(old_location_in).isWhite())) {
            return false;
        }

        return true;
    }

    public Piece getMovingPiece() {
        /* Return chess piece by value (cannot change attributes of original piece) unless null */
        
        if (this.MOVING_PIECE == null) { return null; }
        return this.MOVING_PIECE.copy();
    }
    public Piece getKilledPiece() {
        /* Return chess piece by value (cannot change attributes of original piece) unless null */

        if (this.KILLED_PIECE == null) { return null; }
        return this.KILLED_PIECE.copy();
    }
    public Space getOldLocation() {
        return this.OLD_LOCATION;
    }
    public Space getNewLocation() {
        return this.NEW_LOCATION;
    }
    public int getKillPoints() {
        return this.kill_points;
    }
    public void setKillPoints(int points_in) {
        this.kill_points = points_in;
    }
    
    public String getNotation(boolean is_enemy_checked, boolean be_precise) {
        /* Get the move in algebraic notation (for human readability) */

        String move_string = getNewLocation().toString();

        if (is_enemy_checked) {
            move_string += "+";
        }
        // add x if enemy piece is killed
        else if (getKillPoints() > 0) {
            move_string = "x" + move_string;
        }

        // if multiple pieces of same type exist on the same row, column of moving piece is added to the string
        if (be_precise) {
            String old_location_x_axis = String.valueOf(getOldLocation().toString().charAt(0));
            move_string = old_location_x_axis + move_string;
        }

        // knight piece has different symbol to differentiate it from king
        if (getMovingPiece() instanceof Knight) {
            move_string = "N" + move_string;
        }
        // only pawn doesn't have a symbol
        else if (!(getMovingPiece() instanceof Pawn)) {
            move_string = getMovingPiece().getName().charAt(0) + move_string;
        }

        return move_string;
    }

    public abstract void apply(Board chess_board);
    public abstract void undo(Board chess_board);
}
