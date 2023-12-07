package main;

import main.control.Human;
import main.control.Player;

import java.util.Scanner;

public class ChessGame {
    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Player[] players = {new Human(true), new Human(false)};

        Game chess_game = new Game(players);
        chess_game.runGame();

        SCANNER.close();
    }
}
