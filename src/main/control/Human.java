package main.control;

import main.Space;
import main.boards.Board;
import main.moves.Move;
import main.moves.PromotePawnMove;

import java.util.List;

import static main.ChessGame.SCANNER;

public class Human extends Player {
    public Human(boolean is_white_in) {
        super(is_white_in);
    }

    private boolean verifySpaceInput(String input_string) {
        if (input_string.length() != 2) {
            return false;
        }
        if (!Character.isDigit(input_string.charAt(1))) {
            return false;
        }
        int second_digit = Character.getNumericValue(input_string.charAt(1));
        if (second_digit > 8 || second_digit < 1) {
            return false;
        }

        switch (input_string.charAt(0)) {
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
                break;
            default:
                return false;
        }

        return true;
    }

    public Space convertInputToSpace(Board chess_board) {
        /* Convert inputted string into a space based on standard notation (e.g. e7) */

        String first_space_loc = "";

        boolean is_valid = false;
        while (!is_valid) {
            System.out.println("Please select a space (e.g. d2)");
            first_space_loc = SCANNER.nextLine();

            // ensure input is valid
            is_valid = verifySpaceInput(first_space_loc);
            if (!is_valid) {
                System.out.println("Invalid input!");
            }
        }

        // convert string notation to space object
        return chess_board.getSpaceByString(first_space_loc);
    }

    public List<Move> userSelectSpace(Board chess_board) {
        /* Loop for user to select a space to move a piece */

        while (true) {
            Space selected_space = null;
            while (true) {
                selected_space = convertInputToSpace(chess_board);
                if (selected_space.isFriendly(isWhite())) {
                    if (isCheck(chess_board)) {
                        // check if piece is not pinned
                        if (canSpacePreventCheck(selected_space, chess_board)) {
                            break;
                        }
                    }
                    else {
                        break;
                    }
                }
                System.out.println("No piece to control here!");
            }

            // get moves method automatically filters out moves that lead to check (illegal moves)
            List<Move> possible_moves = getMoves(selected_space, chess_board);
            if (possible_moves.size() > 0) {
                return possible_moves;
            }
            System.out.println("Piece cannot move!");
        }
    }

    private void printMoves(List<Move> possible_moves, Board chess_board) {
        /* Print list of moves user can select from */

        System.out.println("Please select a move (or type 'Q' or 'q' to go back):");
        for (int i = 0; i < possible_moves.size(); ++i) {
            Move move = possible_moves.get(i);
            String move_name = move.getMoveAsString(
                doesMoveCauseEnemyCheck(move, chess_board), !chess_board.isSpacePieceUniqueOnRow(move.getOldLocation())
            );
            System.out.printf("%d: %s\n", i + 1, move_name);
        }
    }

    private Move checkPawnPromotion(Move chosen_move) {
        /* Check if pawn is being promoted and get user input accordingly */
        
        if (chosen_move instanceof PromotePawnMove) {
            int choice = -1; // reset choice variable for another input
            while (choice <= 0 || choice > 4) {
                System.out.println("You have promoted a pawn!\nPlease choose a new piece:");
                System.out.println("1: Queen\n2: Rook\n3: Bishop\n4: Knight");

                String str_choice = SCANNER.nextLine();
                // convert input to integer if possible
                try {
                    choice = Integer.parseInt(str_choice);
                }
                catch (Exception e) {
                    System.out.println("Input must be integer!");
                }
            }
            // apply the chosen piece to the move
            ((PromotePawnMove) chosen_move).setNewPiece(choice, isWhite());
        }
        return chosen_move;
    }

    @Override
    public Move startMove(Board chess_board) {
        while (true) {
            // get all (excluding moves leading to check) moves based on user's selected piece
            List<Move> possible_moves = userSelectSpace(chess_board);

            // allow user to select which move to play
            int choice = -1;
            boolean restart = false;
            while ((choice <= 0 || choice > possible_moves.size()) && !restart) {
                printMoves(possible_moves, chess_board);

                // user chooses one of the printed moves
                String str_choice = SCANNER.nextLine();
                // allow user to go back in the menu
                if (str_choice.equals("Q") || str_choice.equals("q")) {
                    restart = true;
                }
                else {
                    // convert input to integer if possible
                    try {
                        choice = Integer.parseInt(str_choice);
                    }
                    catch (Exception e) {
                        System.out.println("Input must be integer!");
                    }
                }
            }
            if (!restart) {
                // check if move involves pawn promotion before returning it
                return checkPawnPromotion(possible_moves.get(choice - 1));
            }
        }
    }
}
