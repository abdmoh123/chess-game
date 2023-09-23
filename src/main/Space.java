package main;

import main.pieces.Piece;

public class Space {
    private final int X; // a to h = 0 to 7
    private final int Y; // 1 to 8 = 0 to 7
    private Piece piece;
    private final boolean IS_WITHIN_BOARD;

    public Space(int x_in, int y_in) {
        this.X = x_in;
        this.Y = y_in;
        this.IS_WITHIN_BOARD = x_in < 8 && y_in < 8 && x_in >= 0 && y_in >= 0;
    }
    public Space(int x_in, int y_in, Piece piece_in) {
        this(x_in, y_in);
        this.piece = piece_in;
    }

    public int getX() {
        return this.X;
    }
    public int getY() {
        return this.Y;
    }

    // checks if space has no piece
    public boolean isEmpty() {
        return getPiece() == null;
    }

    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece piece_in) {
        this.piece = piece_in;
    }

    public boolean isWithinBoard() {
        return this.IS_WITHIN_BOARD;
    }

    public boolean isFriendly(boolean is_white_in) {
        // only returns true if space is not empty and the piece is in the same team as the player
        if (!isEmpty()) {
            return is_white_in == getPiece().isWhite();
        }
        return false;
    }
    public boolean isEnemy(boolean is_white_in) {
        // only returns true if space is not empty and the piece is in the enemy team
        if (!isEmpty()) {
            return is_white_in != getPiece().isWhite();
        }
        return false;
    }
}
