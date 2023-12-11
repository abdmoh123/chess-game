package com.abdmoh123.chessgame;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.control.Human;
import com.abdmoh123.chessgame.control.Player;
import com.abdmoh123.chessgame.pieces.Bishop;
import com.abdmoh123.chessgame.pieces.King;
import com.abdmoh123.chessgame.pieces.Piece;
import com.abdmoh123.chessgame.pieces.Queen;

public class GameTest {
    @Before
    public void init() {
    }

    @Test
    public void isCheckmateTrueTest() {
        // initialise contents of board
        King white_king = new King(true);
        King black_king = new King(false);
        Queen black_queen = new Queen(false);
        Bishop black_bishop = new Bishop(false);
        Piece[][] layout = {
            {null, null, null, null, null, null, null, black_king.copy()},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, black_bishop.copy(), null, null, null, null, null},
            {null, black_queen.copy(), null, null, null, null, null, null},
            {white_king.copy(), null, null, null, null, null, null, null}
        };

        // initialise chess board, players and game
        Board test_board = new StandardBoard(layout);
        Player[] players = {new Human(true), new Human(false)};
        Game chess_game = new Game(players, test_board);

        // inputted layout should be checkmate
        boolean is_check_mate = chess_game.isCheckMate(players[0], test_board);
        Assert.assertTrue(is_check_mate);
    }

    @Test
    public void isCheckmateFalseTest() {
        // initialise contents of board
        King white_king = new King(true);
        King black_king = new King(false);
        Queen black_queen = new Queen(false);
        Bishop black_bishop = new Bishop(false);
        Piece[][] layout = {
            {null, null, null, null, null, null, null, black_king.copy()},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, black_bishop.copy(), null, null, null, null, null},
            {black_queen.copy(), null, null, null, null, null, null, null},
            {white_king.copy(), null, null, null, null, null, null, null}
        };

        // initialise chess board, players and game
        Board test_board = new StandardBoard(layout);
        Player[] players = {new Human(true), new Human(false)};
        Game chess_game = new Game(players, test_board);

        // inputted layout should not be checkmate (king can take queen)
        boolean is_check_mate = chess_game.isCheckMate(players[0], test_board);
        Assert.assertFalse(is_check_mate);
    }

    @Test
    public void isStalemateTrueTest() {
        // initialise contents of board
        King white_king = new King(true);
        King black_king = new King(false);
        Queen black_queen = new Queen(false);
        Piece[][] layout = {
            {null, null, null, null, null, null, null, black_king.copy()},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, black_queen.copy(), null, null, null, null, null},
            {white_king.copy(), null, null, null, null, null, null, null}
        };

        // initialise chess board, players and game
        Board test_board = new StandardBoard(layout);
        Player[] players = {new Human(true), new Human(false)};
        Game chess_game = new Game(players, test_board);

        // inputted layout should be stalemate (white king cannot move)
        boolean is_stalemate = chess_game.isStalemate(players[0], test_board);
        Assert.assertTrue(is_stalemate);
    }

    @Test
    public void isStalemateFalseTest() {
        // initialise contents of board
        King white_king = new King(true);
        King black_king = new King(false);
        Piece[][] layout = {
            {null, null, null, null, null, null, null, black_king.copy()},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {white_king.copy(), null, null, null, null, null, null, null}
        };

        // initialise chess board, players and game
        Board test_board = new StandardBoard(layout);
        Player[] players = {new Human(true), new Human(false)};
        Game chess_game = new Game(players, test_board);

        // inputted layout should not be stalemate (white king can move)
        boolean is_stalemate = chess_game.isStalemate(players[0], test_board);
        Assert.assertFalse(is_stalemate);
    }
}
