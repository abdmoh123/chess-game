package com.abdmoh123.chessgame.boards;

import org.junit.Before;
import org.junit.Test;

import com.abdmoh123.chessgame.pieces.Piece;
import com.abdmoh123.chessgame.utils.ChessCSVReader;

import org.junit.Assert;

public class BoardTest {
    Piece[][] expected_standard_board_contents;

    @Before
    public void init() throws Exception {
        expected_standard_board_contents = ChessCSVReader.readBoardCSV(
            "board_layouts/standard_board_layout.csv"
        );
    }

    @Test
    public void standardBoardSmokeTest() {
        Board standard_board = new StandardBoard();
        standard_board.initialise();

        Assert.assertArrayEquals(expected_standard_board_contents, standard_board.getAllSpaces());
    }

    @Test
    public void initialiseFENTest() {
        Board fen_board = new StandardBoard();
        fen_board.initialiseFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");

        Assert.assertArrayEquals(expected_standard_board_contents, fen_board.getAllSpaces());
    }
}
