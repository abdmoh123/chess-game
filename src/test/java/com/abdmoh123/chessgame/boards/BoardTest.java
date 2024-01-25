package com.abdmoh123.chessgame.boards;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.abdmoh123.chessgame.pieces.Piece;
import com.abdmoh123.chessgame.utils.ChessCSVReader;

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

        Assert.assertArrayEquals(expected_standard_board_contents, standard_board.getContents());
    }
}
