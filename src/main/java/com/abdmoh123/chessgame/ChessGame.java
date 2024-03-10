package com.abdmoh123.chessgame;

import java.util.Scanner;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.players.Human;
import com.abdmoh123.chessgame.players.Player;
import com.abdmoh123.chessgame.players.bots.EngineBot;
import com.abdmoh123.chessgame.players.bots.RandomBot;

public class ChessGame {
    public static final Scanner SCANNER = new Scanner(System.in);

    private static boolean isModeInputValid(String choice_in, int min_value, int max_value) {
        if (choice_in.length() != 1) {
            return false;
        }
        char c = choice_in.charAt(0);
        if (c - '0' < min_value || c - '0' > max_value) {
            return false;
        }
        return true;
    }

    private static int choosePlayer(boolean is_white_in) {
        String player_colour = "white";
        if (!is_white_in) player_colour = "black";

        int player_type = 0;
        while (true) {
            System.out.printf("Select %s player:\n1 - Human\n2 - Random bot\n3 - Engine bot\n", player_colour);
            String choice_string = SCANNER.nextLine();

            if (isModeInputValid(choice_string, 1, 3)) {
                player_type = Integer.parseInt(choice_string);
                break;
            }
            System.out.println("Invalid input!");
        }
        return player_type;
    }
    private static Player setPlayer(int player_type_in, boolean is_white_in) {
        switch (player_type_in) {
            case 1:
                return new Human(is_white_in);
            case 2:
                return new RandomBot(is_white_in);
            case 3:
                return new EngineBot(is_white_in, 2);
            default:
                throw new RuntimeException("Invalid player type!");
        }
    }
    public static void main(String[] args) {
        int white_player_type = choosePlayer(true);

        int black_player_type = choosePlayer(false);
        
        Player[] players = new Player[2];
        players[0] = setPlayer(white_player_type, true);
        players[1] = setPlayer(black_player_type, false);
        
        Board chess_board = new StandardBoard();
        chess_board.initialise();

        Game chess_game = new Game(players, chess_board);
        chess_game.runGame();

        SCANNER.close();
    }
}
