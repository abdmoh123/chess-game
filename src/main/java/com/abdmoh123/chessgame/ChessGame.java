package com.abdmoh123.chessgame;

import java.util.Scanner;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.control.Human;
import com.abdmoh123.chessgame.control.Player;

public class ChessGame {
    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Player[] players = {new Human(true), new Human(false)};

        Board chess_board = new StandardBoard();
        chess_board.initialise();

        Game chess_game = new Game(players, chess_board);
        chess_game.runGame();

        SCANNER.close();
    }
}
