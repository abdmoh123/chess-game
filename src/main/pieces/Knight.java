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
        /* Compute and find all spaces that are visible to the Knight */

        resetVision();

        int x_loc = location.getX();
        int y_loc = location.getY();

        // eight predetermined spaces knight can move to
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
            Space space = new Space(set[0], set[1]);
            if (Move.isLegal(chess_board, location, space)) {
                addVisibleSpace(space);
            }
        }
    }

    @Override
    public List<Move> getPossibleMoves(Space location, Board chess_board) {
        List<Move> possible_moves = new ArrayList<>();

        computeVision(location, chess_board);

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
