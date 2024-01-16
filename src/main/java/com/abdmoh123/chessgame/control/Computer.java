package com.abdmoh123.chessgame.control;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.moves.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Computer extends Player {
    public Computer(boolean is_white_in) {
        super(is_white_in);
    }

    @Override
    public Move startMove(Board chess_board) {
        Random rand = new Random();

        List<Space> friendly_spaces = chess_board.getFriendlySpaces(isWhite());
        List<Move> possible_moves = new ArrayList<>();

        boolean valid_input = false;
        while (!valid_input) {
            int space_choice = rand.nextInt(friendly_spaces.size());

            Space selected_space = friendly_spaces.get(space_choice);
            friendly_spaces.remove(space_choice); // prevent choosing the same piece multiple times

            // automatically filter out illegal moves (e.g. moving irrelevant piece when king is in check)
            possible_moves = getLegalMoves(selected_space, chess_board);
            // stop searching if selected piece can move
            if (possible_moves.size() > 0) {
                valid_input = true;
            }
        }

        // select a random move from all possible legal moves
        int choice = rand.nextInt(possible_moves.size());
        return possible_moves.get(choice);
    }
}
