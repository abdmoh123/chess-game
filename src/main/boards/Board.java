package main.boards;

import main.Space;
import main.moves.*;
import main.pieces.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Board {
    private Piece[][] spaces;
    private final int LENGTH;

    public Board(int length_in) {
        this.spaces = new Piece[length_in][length_in]; // length_in x length_in grid
        this.LENGTH = length_in;
    }

    /* Fill the board with pieces */
    public abstract void initialise();
    /* Create a deep copy/clone of a given board (any changes won't affect the original) */
    public abstract Board clone();
    /* Return a Space object based on inputted string (e.g. a1 = Space(0, 0)) */
    public abstract Space getSpaceByString(String input_string);

    public Piece[][] getSpaces() {
        return spaces;
    }
    public int getLength() {
        return LENGTH;
    }

    public Piece getPiece(Space space_in) {
        /* Return piece by value (copy of original piece) */
        return getPieceByReference(space_in).clone();
    }
    public Piece getPieceByReference(Space space_in) {
        if (!isSpaceWithinBoard(space_in)) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinate! (" + space_in.getX() + ", " + space_in.getY() + ")");
        }
        return spaces[space_in.getY()][space_in.getX()];
    }
    public Piece[] getRow(int y) {
        /* Return row of chess board */
        return this.spaces[y];
    }
    public Piece[] getColumn(int x) {
        /* Return column of chess board */

        Piece[] column = new Piece[8];
        for (int i = 0; i < 8; ++i) {
            Space space = new Space(x, i);
            column[i] = getPiece(space);
        }
        return column;
    }
    public List<Space> getCheckedSpaces(boolean is_white_turn) {
        /* Find all spaces attacked by the enemy (helps with finding checks) */

        List<Space> checked_spaces = new ArrayList<>();
        List<Space> enemy_spaces = new ArrayList<>();

        // get and separate white and black spaces into 2 lists
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                Space space = new Space(i, j);
                // skip space if empty
                if (!isSpaceEmpty(space)) {
                    if (getPiece(space).isWhite() != is_white_turn) {
                        enemy_spaces.add(space);
                    }
                }
            }
        }

        // search through all spaces that are covered by enemy (if king is in one, it is in check)
        for (Space space : enemy_spaces) {
            // update vision of each enemy piece
            getPiece(space).computeVision(space, this);
            List<Space> piece_vision = getPiece(space).getVisibleSpaces();
            for (Space visible_space : piece_vision) {
                // prevent duplicate spaces
                if (!checked_spaces.contains(visible_space)) {
                    checked_spaces.add(visible_space);
                }
            }
        }

        return checked_spaces;
    }
    public List<Space> getSpacesByPieceName(String piece_name) {
        /* Search through board and return all spaces that hold a given piece type */
        List<Space> selected_pieces = new ArrayList<>();

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                Space space = new Space(i, j);
                if (!isSpaceEmpty(space)) {
                    if (getPiece(space).getName().equals(piece_name)) {
                        selected_pieces.add(space);
                    }
                }
            }
        }

        return selected_pieces;
    }

    public boolean isSpaceEmpty(Space space_in) {
        if (getPiece(space_in) == null) {
            return true;
        }
        return false;
    }
    public boolean isSpaceWithinBoard(Space space_in) {
        if (space_in.getX() > 7 || space_in.getX() < 0) {
            return false;
        }
        if (space_in.getY() > 7 || space_in.getY() < 0) {
            return false;
        }
        return true;
    }
    public boolean isSpaceFriendly(Space space_in, boolean player_is_white) {
        if (!isSpaceWithinBoard(space_in)) {
            return false;
        }
        if (isSpaceEmpty(space_in)) {
            return false;
        }
        return getPiece(space_in).isWhite() == player_is_white;
    }
    public boolean isPieceUniqueOnRow(Space space_in) {
        // get row of piece by getting the space coordinates
        Piece[] row = getRow(space_in.getY());
        
        String piece_name = getPiece(space_in).getName();

        int count = 0;
        for (Piece piece : row) {
            // stop searching if found more than one of the same type of piece
            if (count == 2) {
                break;
            }
            if (piece != null) {
                // check if piece is the same type and same team as given piece
                if (piece.getName().equals(piece_name) && piece.isWhite() == getPiece(space_in).isWhite()) {
                    ++count;
                }
            }
        }
        return count == 1;
    }

    public void updateSpace(Space space_in, Piece piece_in) {
        if (isSpaceWithinBoard(space_in)) {
            spaces[space_in.getY()][space_in.getX()] = piece_in;
        }
    }

    public void display() {
        /* Display board layout on terminal */

        for (int j = 7; j >= 0; --j) {
            for (int i = 0; i < 8; ++i) {
                Space space = new Space(i, j);

                if (isSpaceEmpty(space)) {
                    System.out.print("o ");
                }
                else {
                    char piece_letter = ' ';
                    if (getPiece(space) instanceof Knight) {
                        piece_letter = 'N';
                    }
                    else {
                        piece_letter = getPiece(space).getName().charAt(0);
                    }
                    System.out.print(piece_letter + " ");
                }
            }
            System.out.print("\n");
        }
    }

    public Board after(Move move_in) {
        /* Create a temporary board with the move applied. Useful for handling checks and pinned pieces. */

        Board new_board = clone();
        move_in.apply(new_board);
        return new_board;
    }
}
