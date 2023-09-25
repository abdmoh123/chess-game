package main.pieces;

import main.moves.Move;
import main.Space;
import main.boards.Board;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    private final boolean WHITE_STATE; // true = white, false = black
    private final int VALUE;
    private List<Space> visible_spaces;

    public Piece(boolean is_white_in, int value_in) {
        this.WHITE_STATE = is_white_in;
        this.VALUE = value_in;
        this.visible_spaces = new ArrayList<>();
    }

    public abstract String getName();
    public abstract Piece clone();

    public abstract void computeVision(Space location, Board chess_board);
    public abstract List<Move> getPossibleMoves(Space location, Board chess_board);

    public boolean isWhite() {
        return this.WHITE_STATE;
    }
    public int getValue() {
        return this.VALUE;
    }

    public List<Space> getVisibleSpaces() {
        return this.visible_spaces;
    }
    // replace list of visible spaces with values of another list (overwriting)
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
}
