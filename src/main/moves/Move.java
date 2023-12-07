package main.moves;

import main.boards.Board;
import main.boards.Space;
import main.pieces.Knight;
import main.pieces.Pawn;
import main.pieces.Piece;

public abstract class Move {
    private final Piece CHESS_PIECE;
    private final Space OLD_LOCATION;
    private final Space NEW_LOCATION;
    private int kill_points;

    public Move(Space old_location_in, Space new_location_in, Piece piece_in) {
        // Only use for quiet moves (no piece killed)
        this.OLD_LOCATION = old_location_in;
        this.NEW_LOCATION = new_location_in;
        this.CHESS_PIECE = piece_in;
        this.kill_points = 0;
    }
    public Move(Space old_location_in, Space new_location_in, Piece piece_in, Piece piece_killed) {
        // Useful when capturing material (piece killed)
        this.OLD_LOCATION = old_location_in;
        this.NEW_LOCATION = new_location_in;
        this.CHESS_PIECE = piece_in;
        if (piece_killed != null) {
            this.kill_points = piece_killed.getValue();
        }
        else {
            this.kill_points = 0;
        }
    }

    public abstract void apply(Board chess_board);

    public static boolean isLegal(Board chess_board, Space old_location_in, Space new_location_in) {
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

    public Piece getChessPiece() {
        /* Return chess piece by value (cannot change attributes of original piece) */
        return this.CHESS_PIECE.copy();
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
        if (getChessPiece() instanceof Knight) {
            move_string = "N" + move_string;
        }
        // only pawn doesn't have a symbol
        else if (!(getChessPiece() instanceof Pawn)) {
            move_string = getChessPiece().getName().charAt(0) + move_string;
        }

        return move_string;
    }
}
