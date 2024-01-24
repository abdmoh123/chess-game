package com.abdmoh123.chessgame.pieces;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.moves.Move;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    private final boolean IS_WHITE; // true = white, false = black
    private final int VALUE;
    private List<Space> visible_spaces;

    public Piece(boolean is_white_in, int value_in) {
        this.IS_WHITE = is_white_in;
        this.VALUE = value_in;
        this.visible_spaces = new ArrayList<>();
    }

    public boolean canSee(Space current_location_in, Space new_location_in, Board chess_board) {
        /* Check if a piece can move to a given location */

        computeVision(current_location_in, chess_board);

        List<Space> visible_spaces = getVisibleSpaces();
        for (Space visible_space : visible_spaces) {
            if (visible_space.equals(new_location_in)) {
                return true;
            }
        }
        return false;
    };

    public boolean isWhite() {
        return this.IS_WHITE;
    }
    public int getValue() {
        return this.VALUE;
    }

    public List<Space> getVisibleSpaces() {
        return this.visible_spaces;
    }
    public void setVisibleSpaces(List<Space> visible_spaces_in) {
        this.visible_spaces.clear();
        this.visible_spaces.addAll(visible_spaces_in);
    }
    public void addVisibleSpaces(List<Space> visible_spaces_in) {
        this.visible_spaces.addAll(visible_spaces_in);
    }
    public void addVisibleSpace(Space space_in) {
        this.visible_spaces.add(space_in);
    }
    public void resetVision() {
        this.visible_spaces.clear();
    }

    /* Compute and find all spaces that are visible to the piece */
    public abstract void computeVision(Space location, Board chess_board);
    /* Generate a list holding all possible moves the piece can take */
    public abstract List<Move> getPossibleMoves(Space location, Board chess_board);

    public abstract String getName();
    public abstract Piece copy();

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Piece)) {
            return false;
        }

        Piece piece_in = (Piece) obj;
        return isWhite() == piece_in.isWhite() && getValue() == piece_in.getValue() && getVisibleSpaces().equals(piece_in.getVisibleSpaces());
    }
}
