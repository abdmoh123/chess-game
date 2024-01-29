package com.abdmoh123.chessgame.control.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.abdmoh123.chessgame.Game;
import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.control.Human;
import com.abdmoh123.chessgame.control.Player;

public class EngineTest {
    @Before
    public void init() {
    }

    @Test
    public void isCheckmateTrueTest() {
        try {
            Board test_board = new StandardBoard();
            test_board.initialise("board_layouts/game_test/checkmate_true.csv");

            Player[] players = {new Human(true), new Human(false)};
            Game chess_game = new Game(players, test_board);

            // inputted layout should be checkmate (king cannot move)
            boolean is_check_mate = chess_game.getEngine().isCheckMate(players[0].isWhite());
            Assert.assertTrue(is_check_mate);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false); // fail the test
        }
    }

    @Test
    public void isCheckmateFalseTest() {
        try{
            Board test_board = new StandardBoard();
            test_board.initialise("board_layouts/game_test/checkmate_false.csv");

            Player[] players = {new Human(true), new Human(false)};
            Game chess_game = new Game(players, test_board);

            // inputted layout should not be checkmate (king can take queen)
            boolean is_check_mate = chess_game.getEngine().isCheckMate(players[0].isWhite());
            Assert.assertFalse(is_check_mate);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false); // fail the test
        }
    }

    @Test
    public void isStalemateTrueTest() {
        try {
            Board test_board = new StandardBoard();
            test_board.initialise("board_layouts/game_test/stalemate_true.csv");

            Player[] players = {new Human(true), new Human(false)};
            Game chess_game = new Game(players, test_board);

            // inputted layout should be stalemate (white king cannot move)
            boolean is_stalemate = chess_game.getEngine().isStalemate(players[0].isWhite());
            Assert.assertTrue(is_stalemate);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false); // fail the test
        }
    }

    @Test
    public void isStalemateFalseTest() {
        try {
            Board test_board = new StandardBoard();
            test_board.initialise("board_layouts/game_test/stalemate_false.csv");

            Player[] players = {new Human(true), new Human(false)};
            Game chess_game = new Game(players, test_board);

            // inputted layout should not be stalemate (white king can move)
            boolean is_stalemate = chess_game.getEngine().isStalemate(players[0].isWhite());
            Assert.assertFalse(is_stalemate);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false); // fail the test
        }
    }
}
