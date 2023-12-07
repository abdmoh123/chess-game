package main.control;

import main.boards.Board;
import main.boards.Space;
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
            Space selected_space = new Space(0, 0);
            if (chess_board.isSpaceFriendly(selected_space, isWhite())) {
                // TODO: Make this safer (no crash)
                possible_moves = chess_board.getPiece(selected_space).getPossibleMoves(selected_space, chess_board);
            }
        }
        return possible_moves.get(0);
    }
}
