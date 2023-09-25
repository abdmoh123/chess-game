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
        super();

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

    public StandardBoard clone() {
        /* Create a deep copy/clone of a given board (any changes won't affect the original) */

        StandardBoard new_board = new StandardBoard();
        for (Space[] row : new_board.getSpaces()) {
            for (Space space : row) {
                if (!space.isEmpty()) {
                    new_board.updateSpace(space, space.getPiece().clone());
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
