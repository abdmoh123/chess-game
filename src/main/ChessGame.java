package main;

import main.control.Human;
import main.control.Player;

import java.util.Scanner;

public class ChessGame {
    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Player player_1, player_2;
        player_1 = new Human(true);
        player_2 = new Human(false);
        Game chess_game = new Game(player_1, player_2);

        chess_game.runGame();

        SCANNER.close();
    }
}
