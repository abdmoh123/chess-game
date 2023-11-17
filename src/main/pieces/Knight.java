package main.pieces;

import main.moves.Move;
import main.moves.StandardMove;
import main.Space;
import main.boards.Board;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(boolean is_white_in) {
        super(is_white_in, 3);
    }

    @Override
    public void computeVision(Space location, Board chess_board) {
        resetVision();

        int x_loc = location.getX();
        int y_loc = location.getY();

        // max 8 possible spaces
        int[][] space_coordinates = {
            {x_loc + 1, y_loc + 2},
            {x_loc + 2, y_loc + 1},
            {x_loc + 2, y_loc - 1},
            {x_loc + 1, y_loc - 2},
            {x_loc - 1, y_loc - 2},
            {x_loc - 2, y_loc - 1},
            {x_loc - 2, y_loc + 1},
            {x_loc - 1, y_loc + 2}
        };

        for (int[] set : space_coordinates) {
            // only adds spaces within the chess board
            if (set[0] >= 0 && set[0] < 8 && set[1] >= 0 && set[1] < 8) {
                Space space = new Space(set[0], set[1]);
                // ensure move follows the rules
                if (Move.is_legal(chess_board, location, space)) {
                    addVisibleSpace(space);
                }
            }
        }
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        computeVision(location, chess_board);

        // only adds legal moves (within chess board coords)
        for (Space visible_space : getVisibleSpaces()) {
            possible_moves.add(new StandardMove(location, visible_space, this, chess_board.getPiece(visible_space)));
        }
        return possible_moves;
    }

    @Override
    public Piece clone() {
        Knight new_knight = new Knight(isWhite());
        new_knight.setVisibleSpaces(getVisibleSpaces());
        return new_knight;
    }

    @Override
    public String getName() {
        return "Knight";
    }
}
