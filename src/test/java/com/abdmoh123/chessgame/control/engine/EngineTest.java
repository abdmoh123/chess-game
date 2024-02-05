package com.abdmoh123.chessgame.control.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.StandardBoard;

public class EngineTest {
    @Before
    public void init() {
    }

    @Test
    public void isCheckmateTrueTest() {
        try {
            Board test_board = new StandardBoard();
            test_board.initialise("board_layouts/game_test/checkmate_true.csv");

            Engine chess_engine = new Engine(test_board);

            // inputted layout should be checkmate (king cannot move)
            boolean is_check_mate = chess_engine.isCheckMate(true);
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

            Engine chess_engine = new Engine(test_board);

            // inputted layout should not be checkmate (king can take queen)
            boolean is_check_mate = chess_engine.isCheckMate(true);
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

            Engine chess_engine = new Engine(test_board);

            // inputted layout should be stalemate (white king cannot move)
            boolean is_stalemate = chess_engine.isStalemate(true);
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

            Engine chess_engine = new Engine(test_board);

            // inputted layout should not be stalemate (white king can move)
            boolean is_stalemate = chess_engine.isStalemate(true);
            Assert.assertFalse(is_stalemate);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false); // fail the test
        }
    }

    @Test
    public void isDeadPositionTrueTest() {
        try {
            Board test_board = new StandardBoard();
            test_board.initialise("board_layouts/game_test/dead_position_true.csv");

            Engine chess_engine = new Engine(test_board);

            // inputted layout should be a dead position (King + bishop vs king + bishop same colour tile)
            boolean is_dead_position = chess_engine.isDeadPosition();
            Assert.assertTrue(is_dead_position);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false); // fail the test
        }
    }

    @Test
    public void isDeadPositionFalseTest() {
        try {
            Board test_board = new StandardBoard();
            test_board.initialise("board_layouts/game_test/dead_position_false.csv");

            Engine chess_engine = new Engine(test_board);

            // inputted layout should not be a dead position (2 knights can result in a checkmate)
            boolean is_dead_position = chess_engine.isDeadPosition();
            Assert.assertFalse(is_dead_position);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false); // fail the test
        }
    }
}
