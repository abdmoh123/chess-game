package main.control;

import main.Board;
import main.Space;
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

        boolean valid_letter = true;
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
                valid_letter = false;
        }

        return valid_letter;
    }

    public Space convertInputToSpace(Board chess_board) {

        String first_space_loc = "";

        // ensure input is valid
        boolean is_valid = false;
        while (!is_valid) {
            System.out.println("Please select a space (e.g. d2)");
            first_space_loc = SCANNER.nextLine();
            is_valid = verifySpaceInput(first_space_loc);
        }

        return chess_board.getSpaceByString(first_space_loc);
    }

    public List<Move> inputLoop(Board chess_board) {
        while (true) {
            Space selected_space = null;
            boolean can_move = false;
            while (!can_move) {
                selected_space = convertInputToSpace(chess_board);
                if (selected_space.isFriendly(isWhite())) {
                    if (isCheck(chess_board)) {
                        // check if piece is not pinned
                        if (canSpacePreventCheck(selected_space, chess_board)) {
                            can_move = true;
                        }
                    }
                    else {
                        can_move = true;
                    }
                }
            }

            // get moves method automatically filters out moves that lead to check (illegal moves)
            List<Move> possible_moves = getMoves(selected_space, chess_board);
            if (possible_moves.size() > 0) {
                return possible_moves;
            }
            System.out.println("Piece cannot move!");
        }
    }

    @Override
    public Move startMove(Board chess_board) {
        // get all (excluding moves leading to check) moves based on user's selected piece
        List<Move> possible_moves = inputLoop(chess_board);

        int choice = -1;
        while (choice <= 0 || choice > possible_moves.size()) {
            // print list of moves user can select from
            System.out.println("Please select a move:");
            for (int i = 0; i < possible_moves.size(); ++i) {
                Move move = possible_moves.get(i);
                String move_name = move.getMoveAsString(
                    doesMoveCauseEnemyCheck(move, chess_board), !chess_board.isSpacePieceUniqueOnRow(move.getOldLocation())
                );
                System.out.printf("%d: %s\n", i + 1, move_name);
            }

            // user chooses one of the printed moves
            String str_choice = SCANNER.nextLine();
            // convert input to integer if possible
            try {
                choice = Integer.parseInt(str_choice);
            }
            catch (Exception e) {
                System.out.println("Input must be integer!");
            }
        }
        Move chosen_move = possible_moves.get(choice - 1);
        
        // allow user to select which piece to promote a pawn to
        if (chosen_move instanceof PromotePawnMove) {
            choice = -1; // reset choice variable for another input
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
}
