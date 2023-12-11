package com.abdmoh123.chessgame;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.control.Human;
import com.abdmoh123.chessgame.control.Player;
import com.abdmoh123.chessgame.pieces.*;

public class GameTest {
    King white_king;
    King black_king;
    Queen black_queen;
    Bishop black_bishop;

    private Piece convertSymbolToPiece(String piece_symbol_in) {
        switch (piece_symbol_in.replaceAll(" ", "")) { // remove whitespace
            case "WK":
                return this.white_king.copy();
            case "BK":
                return this.black_king.copy();
            case "BQ":
                return this.black_queen.copy();
            case "BB":
                return this.black_bishop.copy();
            default:
                return null;
        }
    }
    private Piece[][] readBoardCSV(String file_name) throws URISyntaxException, IOException, CsvValidationException {
        Path path = Paths.get(ClassLoader.getSystemResource("game_test/" + file_name).toURI());

        List<List<String>> layout = new ArrayList<List<String>>();

        try (Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csv_reader = new CSVReader(reader)) {
                String[] row = null;
                while ((row = csv_reader.readNext()) != null) {
                    layout.add(Arrays.asList(row));
                }
            }
        }
        
        Piece[][] board_contents = new Piece[layout.size()][layout.size()];
        for (int i = 0; i < layout.size(); i++) {
            for (int j = 0; j < layout.size(); ++j) {
                String symbol = layout.get(i).get(j);
                board_contents[i][j] = convertSymbolToPiece(symbol);
            }
        }

        return board_contents;
    }

    @Before
    public void init() {
        // initialise pieces for use in tests
        white_king = new King(true);
        black_king = new King(false);
        black_queen = new Queen(false);
        black_bishop = new Bishop(false);
    }

    @Test
    public void isCheckmateTrueTest() {
        try {
            // initialise board layout
            Piece[][] layout = readBoardCSV("checkmate_true.csv");

            // initialise chess board, players and game
            Board test_board = new StandardBoard(layout);
            Player[] players = {new Human(true), new Human(false)};
            Game chess_game = new Game(players, test_board);

            // inputted layout should be checkmate (king cannot move)
            boolean is_check_mate = chess_game.isCheckMate(players[0], test_board);
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
            // initialise board layout
            Piece[][] layout = readBoardCSV("checkmate_false.csv");

            // initialise chess board, players and game
            Board test_board = new StandardBoard(layout);
            Player[] players = {new Human(true), new Human(false)};
            Game chess_game = new Game(players, test_board);

            // inputted layout should not be checkmate (king can take queen)
            boolean is_check_mate = chess_game.isCheckMate(players[0], test_board);
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
            // initialise board layout
            Piece[][] layout = readBoardCSV("stalemate_true.csv");

            // initialise chess board, players and game
            Board test_board = new StandardBoard(layout);
            Player[] players = {new Human(true), new Human(false)};
            Game chess_game = new Game(players, test_board);

            // inputted layout should be stalemate (white king cannot move)
            boolean is_stalemate = chess_game.isStalemate(players[0], test_board);
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
            // initialise board layout
            Piece[][] layout = readBoardCSV("stalemate_false.csv");

            // initialise chess board, players and game
            Board test_board = new StandardBoard(layout);
            Player[] players = {new Human(true), new Human(false)};
            Game chess_game = new Game(players, test_board);

            // inputted layout should not be stalemate (white king can move)
            boolean is_stalemate = chess_game.isStalemate(players[0], test_board);
            Assert.assertFalse(is_stalemate);
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false); // fail the test
        }
    }
}
