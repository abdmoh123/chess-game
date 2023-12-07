package main.pieces;

import main.moves.Move;
import main.boards.Board;
import main.boards.Space;

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

    public abstract String getName();
    public abstract Piece copy();

    /* Compute and find all spaces that are visible to the piece */
    public abstract void computeVision(Space location, Board chess_board);
    /* Generate a list holding all possible moves the piece can take */
    public abstract List<Move> getPossibleMoves(Space location, Board chess_board);

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
}
