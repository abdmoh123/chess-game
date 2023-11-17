package main.boards;

import main.Space;
import main.moves.Move;
import main.pieces.Bishop;
import main.pieces.King;
import main.pieces.Knight;
import main.pieces.Pawn;
import main.pieces.Queen;
import main.pieces.Rook;

public class StandardBoard extends Board {
    
    public StandardBoard() {
        super(8); // create 8 by 8 board
    }

    public void initialise() {
        // places the white pieces
        updateSpace(new Space(0, 0), new Rook(true));
        updateSpace(new Space(1, 0), new Knight(true));
        updateSpace(new Space(2, 0), new Bishop(true));
        updateSpace(new Space(3, 0), new Queen(true));
        updateSpace(new Space(4, 0), new King(true));
        updateSpace(new Space(5, 0), new Bishop(true));
        updateSpace(new Space(6, 0), new Knight(true));
        updateSpace(new Space(7, 0), new Rook(true));

        // places the black pieces
        updateSpace(new Space(0, 7), new Rook(false));
        updateSpace(new Space(1, 7), new Knight(false));
        updateSpace(new Space(2, 7), new Bishop(false));
        updateSpace(new Space(3, 7), new Queen(false));
        updateSpace(new Space(4, 7), new King(false));
        updateSpace(new Space(5, 7), new Bishop(false));
        updateSpace(new Space(6, 7), new Knight(false));
        updateSpace(new Space(7, 7), new Rook(false));

        // places white and black pawns
        for (int i = 0; i < 8; ++i) {
            updateSpace(new Space(i, 1), new Pawn(true));
            updateSpace(new Space(i, 6), new Pawn(false));
        }
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
                throw new RuntimeException("Invalid input: Must be within board!");
        }
        // convert 2nd character (1-8) to corresponding integer value (0-7)
        y_loc = Character.getNumericValue(input_string.charAt(1)) - 1;
        if (y_loc > 7 || y_loc < 0) {
            throw new RuntimeException("Invalid input: Must be within board!");
        }

        return new Space(x_loc, y_loc);
    }

    public StandardBoard clone() {
        /* Create a deep copy/clone of a given board (any changes won't affect the original) */

        StandardBoard new_board = new StandardBoard();
        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getLength(); ++j) {
                Space space = new Space(i, j);
                if (!isSpaceEmpty(space)) {
                    new_board.updateSpace(space, getPiece(space).clone());
                }
            }
        }
        return new_board;
    }

    @Override
    public StandardBoard after(Move move_in) {
        /* Create a temporary board with the move applied. Useful for handling checks and pinned pieces. */

        StandardBoard new_board = clone();
        move_in.apply(new_board);
        return new_board;
    }
}
