package com.abdmoh123.chessgame.players;

import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.engine.Engine;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.pieces.Pawn;

import static com.abdmoh123.chessgame.ChessGame.SCANNER;

import java.util.ArrayList;
import java.util.List;

public class Human extends Player {
    public Human(boolean is_white_in) {
        super(is_white_in);
    }

    private List<String> convertMoveListToStringList(List<Move> move_list, Engine chess_engine) {
        /*
         * Convert a list of moves into a list of strings representing moves based on
         * algebraic chess notation
         */

        List<String> move_string_list = new ArrayList<>();
        for (Move move : move_list) {
            String move_name = move.getNotation(
                    // inverting isWhite() allows searching if enemy is checked
                    chess_engine.isCheckAfterMove(move, !isWhite()),
                    chess_engine.canMultiplePiecesMoveToSameSpace(move.getOldLocation(), move.getNewLocation()));
            move_string_list.add(move_name);
        }
        return move_string_list;
    }

    @Override
    public Move startMove(Engine chess_engine) {
        while (true) {
            Space first_space_input = null;
            Move chosen_move = null;

            List<Move> possible_moves = new ArrayList<>();

            while (!chess_engine.getBoard().isSpaceFriendly(first_space_input, isWhite())
                    || possible_moves.size() == 0) {
                System.out.println("Please select space from the board (e.g. d2):");
                String first_space_input_string = SCANNER.nextLine();

                try {
                    first_space_input = chess_engine.getBoard().getSpaceByString(first_space_input_string);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }

                possible_moves = chess_engine.generateLegalMoves(first_space_input, isWhite());
                if (!chess_engine.getBoard().isSpaceFriendly(first_space_input, isWhite())) {
                    System.out.println("Space does not contain a piece in your team!");
                } else if (possible_moves.size() == 0) {
                    System.out.println("Piece cannot move!");
                }
            }

            // convert move list to algebraic notation string format
            List<String> possible_moves_string = convertMoveListToStringList(possible_moves, chess_engine);

            boolean go_back = false;
            while (chosen_move == null && !go_back) {
                System.out.println("Please select a move from the list or type 'Q'/'q' to go back:");
                for (String move_name : possible_moves_string) {
                    System.out.printf("- %s\n", move_name);
                }

                String move_input_string = SCANNER.nextLine();

                for (int i = 0; i < possible_moves_string.size(); ++i) {
                    if (move_input_string.equals("Q") || move_input_string.equals("q")) {
                        go_back = true;
                        break;
                    }
                    if (move_input_string.equals(possible_moves_string.get(i))) {
                        chosen_move = possible_moves.get(i);
                        break;
                    }
                }

                if (chosen_move == null && !go_back) {
                    System.out.println("Invalid move!");
                }
            }

            // check if move involves pawn promotion before returning it
            if (!go_back) {
                return chosen_move;
            }
        }
    }
}
