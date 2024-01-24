package com.abdmoh123.chessgame.control;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.moves.PromotePawnMove;
import com.abdmoh123.chessgame.pieces.Pawn;
import com.abdmoh123.chessgame.pieces.Piece;

import static com.abdmoh123.chessgame.ChessGame.SCANNER;

import java.util.ArrayList;
import java.util.List;

public class Human extends Player {
    public Human(boolean is_white_in) {
        super(is_white_in);
    }

    private boolean canMultiplePiecesMoveToSameSpace(
        Space chosen_space,
        Space destination_space,
        List<Space> similar_friendly_spaces,
        Board chess_board
    ) {
        /* Check if no other pieces (of same type) can move to the same destination space */

        for (Space space : similar_friendly_spaces) {
            // only check pieces excluding chosen piece
            if (!chosen_space.equals(space)) {
                Piece other_piece = chess_board.getPiece(space);
                List<Move> other_piece_possible_moves = other_piece.getPossibleMoves(
                    space, chess_board
                );
                for (Move other_move : other_piece_possible_moves) {
                    if (other_move.getNewLocation().equals(destination_space)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<String> convertMoveListToStringList(List<Move> move_list, Board chess_board) {
        /* Convert a list of moves into a list of strings representing moves based on algebraic chess notation */

        List<String> move_string_list = new ArrayList<>();
        for (Move move : move_list) {
            List<Space> similar_friendly_spaces = chess_board.getFriendlySpacesByPieceName(
                move.getMovingPiece().getName(), isWhite()
            );

            boolean be_precise = false;
            // if condition below is true, skip checking if move notation should be precise or not
            if (move.getMovingPiece() instanceof Pawn && move.getKillPoints() == 0) {
                be_precise = false;
            }
            else if (similar_friendly_spaces.size() > 1) {
                be_precise = canMultiplePiecesMoveToSameSpace(
                    move.getOldLocation(),
                    move.getNewLocation(),
                    similar_friendly_spaces,
                    chess_board
                );
            }
            
            String move_name = move.getNotation(
                isEnemyCheckAfterMove(move, chess_board), be_precise
            );
            move_string_list.add(move_name);
        }
        return move_string_list;
    }

    private Move promotePawn(PromotePawnMove chosen_move) {
        /* Applies pawn promotion through user input */
        
        int choice = 0;
        while (choice <= 0 || choice > 4) {
            System.out.println("You have promoted a pawn!\nPlease choose a new piece:");
            System.out.println("1: Queen\n2: Rook\n3: Bishop\n4: Knight");

            String str_choice = SCANNER.nextLine();
            try {
                choice = Integer.parseInt(str_choice);
            }
            catch (Exception e) {
                System.out.println("Input must be integer!");
            }
        }
        // apply the chosen piece to the move
        chosen_move.setNewPiece(choice, isWhite());
        return chosen_move;
    }

    @Override
    public Move startMove(Board chess_board) {
        while (true) {
            Space first_space_input = null;
            Move chosen_move = null;

            List<Move> possible_moves = new ArrayList<>();

            while (!chess_board.isSpaceFriendly(first_space_input, isWhite()) || possible_moves.size() == 0) {
                System.out.println("Please select space from the board (e.g. d2):");
                String first_space_input_string = SCANNER.nextLine();

                try {
                    first_space_input = chess_board.getSpaceByString(first_space_input_string);
                }
                catch (RuntimeException e) {
                    System.out.println(e);
                }

                possible_moves = getLegalMoves(first_space_input, chess_board);
                if (!chess_board.isSpaceFriendly(first_space_input, isWhite())) {
                    System.out.println("Space does not contain a piece in your team!");
                }
                else if (possible_moves.size() == 0) {
                    System.out.println("Piece cannot move!");
                }
            }

            // convert move list to algebraic notation string format
            List<String> possible_moves_string = convertMoveListToStringList(possible_moves, chess_board);

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
                if (chosen_move instanceof PromotePawnMove) {
                    chosen_move = promotePawn((PromotePawnMove) chosen_move);
                }
                return chosen_move;
            }
        }
    }
}
