package main.moves;

import main.Space;
import main.boards.Board;
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

    public static boolean is_legal(Board chess_board, Space old_location_in, Space new_location_in) {
        // ensure moves stay within the predefined board coordinates and are not null
        if (!chess_board.isSpaceValid(old_location_in)) {
            return false;
        }
        if (!chess_board.isSpaceValid(new_location_in)) {
            return false;
        }

        // exclude moving piece to the same square
        if (old_location_in.getX() == new_location_in.getX() && old_location_in.getY() == new_location_in.getY()) {
            return false;
        }

        // prevent piece from friendly fire
        if (chess_board.isSpaceFriendly(new_location_in, chess_board.getPiece(old_location_in).isWhite())) {
            return false;
        }

        // if all criteria is met, move is considered legal
        return true;
    }

    public Piece getChessPiece() {
        /* Return chess piece by value (cannot change attributes of original piece) */
        return CHESS_PIECE.clone();
    }
    public Space getOldLocation() {
        return OLD_LOCATION;
    }
    public Space getNewLocation() {
        return NEW_LOCATION;
    }
    public int getKillPoints() {
        return kill_points;
    }
    public void setKillPoints(int points_in) {
        this.kill_points = points_in;
    }
    
    public String getMoveAsString(boolean is_check_in, boolean be_precise) {
        /* Get the move in algebraic notation (for human readability) */

        String move_string = getNewLocation().toString();

        // add plus if enemy piece is checked
        if (is_check_in) {
            move_string += "+";
        }
        // add x if enemy piece is killed
        else if (getKillPoints() > 0) {
            String old_location_x_axis = "";
            if (be_precise) {
                old_location_x_axis = String.valueOf(getOldLocation().toString().charAt(0));
            }
            move_string = old_location_x_axis + "x" + move_string;
        }

        // knight piece has different letter to differentiate it from king
        if (getChessPiece() instanceof Knight) {
            move_string = "N" + move_string;
        }
        else if (!(getChessPiece() instanceof Pawn)) {
            move_string = getChessPiece().getName().charAt(0) + move_string;
        }

        return move_string;
    }
}
