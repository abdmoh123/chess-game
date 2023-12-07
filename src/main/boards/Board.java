package main.boards;

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
    public abstract Board copy();
    /* Return a Space object based on inputted string (e.g. a1 = Space(0, 0)) */
    public abstract Space getSpaceByString(String input_string);

    public Piece[][] getAllSpaces() {
        return spaces;
    }
    public List<Space> getAllSpacesWithPieces() {
        List<Space> spaces_with_pieces = new ArrayList<>();

        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
                Space space = new Space(i, j);
                if (!isSpaceEmpty(space)) {
                    spaces_with_pieces.add(space);
                }
            }
        }
        return spaces_with_pieces;
    }
    public List<Space> getFriendlySpaces(boolean is_white) {
        List<Space> friendly_spaces = new ArrayList<>();

        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
                Space space = new Space(i, j);
                if (isSpaceFriendly(space, is_white)) {
                    friendly_spaces.add(space);
                }
            }
        }
        return friendly_spaces;
    }
    public List<Space> getEnemySpaces(boolean is_white) {
        List<Space> enemy_spaces = new ArrayList<>();

        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
                Space space = new Space(i, j);
                if (isSpaceEnemy(space, is_white)) {
                    enemy_spaces.add(space);
                }
            }
        }
        return enemy_spaces;
    }
    public int getLength() {
        return LENGTH;
    }

    public Piece getPiece(Space space_in) {
        /* Return piece by reference (changes made to a piece affect the piece on the board) */

        if (!isSpaceValid(space_in)) {
            throw new ArrayIndexOutOfBoundsException("Invalid coordinate! (" + space_in.getX() + ", " + space_in.getY() + ")");
        }
        return spaces[space_in.getY()][space_in.getX()];
    }
    public Piece[] getRow(int y) {
        return this.spaces[y];
    }
    public Piece[] getColumn(int x) {
        Piece[] column = new Piece[8];

        for (int i = 0; i < getLength(); ++i) {
            Space space = new Space(x, i);
            column[i] = getPiece(space);
        }
        return column;
    }
    public List<Space> getCheckedSpaces(boolean is_white_turn) {
        /* Find all spaces attacked by the enemy */

        List<Space> checked_spaces = new ArrayList<>();
        List<Space> enemy_spaces = getEnemySpaces(is_white_turn);

        for (Space space : enemy_spaces) {
            // update vision of each enemy piece
            getPiece(space).computeVision(space, this);
            List<Space> visible_spaces = getPiece(space).getVisibleSpaces();

            for (Space visible_space : visible_spaces) {
                // prevent duplicate spaces
                if (!checked_spaces.contains(visible_space)) {
                    checked_spaces.add(visible_space);
                }
            }
        }

        return checked_spaces;
    }
    public List<Space> getAllSpacesByPieceName(String piece_name) {
        /* Search through board and return all spaces that hold a given piece type */

        List<Space> selected_pieces = new ArrayList<>();

        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
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
    public List<Space> getFriendlySpacesByPieceName(String piece_name, boolean is_white) {
        /* Search through board and return all friendly spaces that hold a given piece type */

        List<Space> selected_pieces = new ArrayList<>();

        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
                Space space = new Space(i, j);
                if (isSpaceFriendly(space, is_white)) {
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
        if (space_in.getX() > getLength() - 1 || space_in.getX() < 0) {
            return false;
        }
        if (space_in.getY() > getLength() - 1 || space_in.getY() < 0) {
            return false;
        }
        return true;
    }
    public boolean isSpaceValid(Space space_in) {
        if (space_in == null) {
            return false;
        }
        if (!isSpaceWithinBoard(space_in)) {
            return false;
        }
        return true;
    }
    public boolean isSpaceFriendly(Space space_in, boolean player_is_white) {
        if (!isSpaceValid(space_in)) {
            return false;
        }
        if (isSpaceEmpty(space_in)) {
            return false;
        }
        return getPiece(space_in).isWhite() == player_is_white;
    }
    public boolean isSpaceEnemy(Space space_in, boolean player_is_white) {
        if (!isSpaceValid(space_in)) {
            return false;
        }
        if (isSpaceEmpty(space_in)) {
            return false;
        }
        return getPiece(space_in).isWhite() != player_is_white;
    }
    public boolean isPieceUniqueOnRow(Space space_in) {
        Piece[] row = getRow(space_in.getY());
        
        String piece_name = getPiece(space_in).getName();

        int count = 0;
        for (Piece piece : row) {
            if (count > 1) {
                break;
            }
            if (piece != null) {
                if (piece.getName().equals(piece_name) && piece.isWhite() == getPiece(space_in).isWhite()) {
                    ++count;
                }
            }
        }
        return count == 1;
    }

    public void updateSpace(Space space_in, Piece piece_in) {
        if (isSpaceValid(space_in)) {
            spaces[space_in.getY()][space_in.getX()] = piece_in;
        }
    }

    public void display() {
        /* Display board layout on terminal */

        for (int j = getLength() - 1; j >= 0; --j) {
            for (int i = 0; i < getLength(); ++i) {
                Space space = new Space(i, j);

                if (isSpaceEmpty(space)) {
                    System.out.print("o ");
                }
                else {
                    char piece_symbol = ' ';
                    if (getPiece(space) instanceof Knight) {
                        piece_symbol = 'N';
                    }
                    else {
                        piece_symbol = getPiece(space).getName().charAt(0);
                    }
                    System.out.print(piece_symbol + " ");
                }
            }
            System.out.print("\n");
        }
    }

    public Board after(Move move_in) {
        /* Create a temporary board with the move applied. Useful for handling checks and pinned pieces. */

        Board new_board = copy();
        move_in.apply(new_board);
        return new_board;
    }
}
