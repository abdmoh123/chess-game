package com.abdmoh123.chessgame;

import java.util.Scanner;

import com.abdmoh123.chessgame.control.Human;
import com.abdmoh123.chessgame.control.Player;

public class ChessGame {
    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Player[] players = {new Human(true), new Human(false)};

        Game chess_game = new Game(players);
        chess_game.runGame();

        SCANNER.close();
    }
}
