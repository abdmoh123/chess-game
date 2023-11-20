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
        // ensure moves stay within the predefined board coordinates
        if (!chess_board.isSpaceWithinBoard(old_location_in)) {
            return false;
        }
        if (!chess_board.isSpaceWithinBoard(new_location_in)) {
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

        String move_string = "";

        // convert x coordinate (0-7) to corresponding notation (a-h)
        switch (getNewLocation().getX()) {
            case 0:
                move_string += "a";
                break;
            case 1:
                move_string += "b";
                break;
            case 2:
                move_string += "c";
                break;
            case 3:
                move_string += "d";
                break;
            case 4:
                move_string += "e";
                break;
            case 5:
                move_string += "f";
                break;
            case 6:
                move_string += "g";
                break;
            case 7:
                move_string += "h";
                break;
            default:
                throw new RuntimeException("Invalid input!");
        }
        // convert y coordinate (0-7) to corresponding notation (1-8)
        switch (getNewLocation().getY()) {
            case 0:
                move_string += "1";
                break;
            case 1:
                move_string += "2";
                break;
            case 2:
                move_string += "3";
                break;
            case 3:
                move_string += "4";
                break;
            case 4:
                move_string += "5";
                break;
            case 5:
                move_string += "6";
                break;
            case 6:
                move_string += "7";
                break;
            case 7:
                move_string += "8";
                break;
            default:
                throw new RuntimeException("Invalid input!");
        }
        if (is_check_in) {
            move_string += "+";
        }
        // add plus to move if enemy piece was killed
        else if (getKillPoints() > 0) {
            String temp_string = "";
            if (be_precise) {
                switch (getOldLocation().getX()) {
                    case 0:
                        temp_string = "a";
                        break;
                    case 1:
                        temp_string = "b";
                        break;
                    case 2:
                        temp_string = "c";
                        break;
                    case 3:
                        temp_string = "d";
                        break;
                    case 4:
                        temp_string = "e";
                        break;
                    case 5:
                        temp_string = "f";
                        break;
                    case 6:
                        temp_string = "g";
                        break;
                    case 7:
                        temp_string = "h";
                        break;
                    default:
                        throw new RuntimeException("Invalid input!");
                }
            }
            move_string = temp_string + "x" + move_string;
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
