package com.abdmoh123.chessgame;

import java.util.Scanner;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.control.Computer;
import com.abdmoh123.chessgame.control.Human;
import com.abdmoh123.chessgame.control.Player;

public class ChessGame {
    public static final Scanner SCANNER = new Scanner(System.in);

    private static boolean isModeInputValid(String choice_in) {
        if (choice_in.length() != 1) {
            return false;
        }
        char c = choice_in.charAt(0);
        if (c < '1' || c > '4') {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int choice = 0;
        while (true) {
            System.out.println(
                "Select gamemode:\n1 - Human vs Human\n2 - Human vs Bot\n3 - Bot vs Bot\n4 - Bot vs Human"
            );
            String choice_string = SCANNER.nextLine();

            if (isModeInputValid(choice_string)) {
                choice = Integer.parseInt(choice_string);
                break;
            }
            System.out.println("Invalid input!");
        }
        
        Player[] players = new Player[2];
        switch (choice) {
            case 1:
                players = new Player[]{new Human(true), new Human(false)};
                System.out.println("Starting Human vs Human...");
                break;
            case 2:
                players = new Player[]{new Human(true), new Computer(false)};
                System.out.println("Starting Human vs Bot...");
                break;
            case 3:
                players = new Player[]{new Computer(true), new Computer(false)};
                System.out.println("Starting Bot vs Bot...");
                break;
            case 4:
                players = new Player[]{new Computer(true), new Human(false)};
                System.out.println("Starting Bot vs Human...");
                break;
            default:
                break;
        }
        
        Board chess_board = new StandardBoard();
        chess_board.initialise();

        Game chess_game = new Game(players, chess_board);
        chess_game.runGame();

        SCANNER.close();
    }
}
