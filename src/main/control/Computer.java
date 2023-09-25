package main.control;

import main.Space;
import main.boards.Board;
import main.moves.Move;

import java.util.ArrayList;
import java.util.List;

public class Computer extends Player {
    public Computer(boolean is_white_in) {
        super(is_white_in);
    }

    @Override
    public Move startMove(Board chess_board) {
        // TODO: Implement a better chess bot

        List<Move> possible_moves = new ArrayList<>();

        boolean valid_input = false;
        while (!valid_input) {
            Space selected_space = chess_board.getSpace(0, 0);
            if (selected_space.isFriendly(isWhite())) {
                // TODO: Make this safer (no crash)
                possible_moves = selected_space.getPiece().getPossibleMoves(selected_space, chess_board);
            }
        }
        return possible_moves.get(0);
    }
}
