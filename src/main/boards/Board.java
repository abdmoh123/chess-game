package main.boards;

import main.Space;
import main.moves.*;
import main.pieces.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Board {
    protected Space[][] spaces;

    public Board() {
        // initialise 2D array
        initialiseSpaces();
    }

    private void initialiseSpaces() {
        // fills board with empty spaces
        this.spaces = new Space[8][8];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                this.spaces[i][j] = new Space(j, i);
            }
        }
    }
    public List<Space> getCheckedSpaces(boolean is_white_turn) {
        List<Space> checked_spaces = new ArrayList<>();
        List<Space> enemy_spaces = new ArrayList<>();

        // get and separate white and black spaces into 2 lists
        for (Space[] row : getSpaces()) {
            for (Space space : row) {
                // skip space if empty
                if (!space.isEmpty()) {
                    if (space.getPiece().isWhite() != is_white_turn) {
                        enemy_spaces.add(space);
                    }
                }
            }
        }

        // search through all spaces that are covered by enemy (if king is in one, it is in check)
        for (Space space : enemy_spaces) {
            // update vision of each enemy piece
            space.getPiece().computeVision(space, this);
            List<Space> piece_vision = space.getPiece().getVisibleSpaces();
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
        List<Space> selected_pieces = new ArrayList<>();

        for (Space[] row : getSpaces()) {
            for (Space space : row) {
                if (!space.isEmpty()) {
                    if (space.getPiece().getName().equals(piece_name)) {
                        selected_pieces.add(space);
                    }
                }
            }
        }

        return selected_pieces;
    }

    public void display() {
        /* Display board layout on terminal */

        for (int j = 7; j >= 0; --j) {
            for (int i = 0; i < 8; ++i) {
                Space space = getSpace(i, j);
                if (space.isEmpty()) {
                    System.out.print("o ");
                }
                else {
                    char piece_letter = ' ';
                    if (space.getPiece() instanceof Knight) {
                        piece_letter = 'N';
                    }
                    else {
                        piece_letter = space.getPiece().getName().charAt(0);
                    }
                    System.out.print(piece_letter + " ");
                }
            }
            System.out.print("\n");
        }
    }

    public Space[][] getSpaces() {
        return spaces;
    }
    public Space getSpace(int x, int y) {
        // only returns a space within the board coordinates
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            return spaces[y][x];
        }
        else throw new ArrayIndexOutOfBoundsException("Invalid coordinate! " + x + " " + y);
    }
    public Space getSpaceByString(String input_string) {
        /* Returns a space on the board based on Chess notation (a-h and 1-8 for row and
        column). For example space "d2" corresponds to space[1][3]. */

        int x_loc, y_loc;

        // convert 1st character (a-h) to corresponding integer value (0-7)
        switch (input_string.charAt(0)) {
            case 'a':
                x_loc = 0;
                break;
            case 'b':
                x_loc = 1;
                break;
            case 'c':
                x_loc = 2;
                break;
            case 'd':
                x_loc = 3;
                break;
            case 'e':
                x_loc = 4;
                break;
            case 'f':
                x_loc = 5;
                break;
            case 'g':
                x_loc = 6;
                break;
            case 'h':
                x_loc = 7;
                break;
            default:
                throw new RuntimeException("Invalid input!");
        }
        // convert 2nd character (1-8) to corresponding integer value (0-7)
        y_loc = Character.getNumericValue(input_string.charAt(1)) - 1;

        return getSpace(x_loc, y_loc);
    }
    public Space[] getRow(int y) {
        /* Return row of chess board */
        return this.spaces[y];
    }
    public Space[] getColumn(int x) {
        /* Return column of chess board */

        Space[] column = new Space[8];
        for (int i = 0; i < 8; ++i) {
            column[i] = getSpace(x, i);
        }
        return column;
    }
    public void updateSpace(Space space_in, Piece piece_in) {
        // only allows spaces within the board coordinates
        if (space_in.isWithinBoard()) {
            spaces[space_in.getY()][space_in.getX()].setPiece(piece_in);
        }
    }

    public boolean isSpacePieceUniqueOnRow(Space space_in) {
        /* Search through row to see if a given piece is unique within the row */

        // get row of piece by getting the space coordinates
        Space[] row = getRow(space_in.getY());
        
        String piece_name = space_in.getPiece().getName();

        int count = 0;
        for (Space space : row) {
            // stop searching if found more than one of the same type of piece
            if (count == 2) {
                break;
            }
            if (!space.isEmpty()) {
                // check if piece is the same type and same team as given piece
                if (space.getPiece().getName().equals(piece_name) && space.getPiece().isWhite() == space_in.getPiece().isWhite()) {
                    ++count;
                }
            }
        }
        return count == 1;
    }

    public abstract Board after(Move move_in);
}
