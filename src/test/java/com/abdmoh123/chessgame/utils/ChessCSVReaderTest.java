package com.abdmoh123.chessgame.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.abdmoh123.chessgame.pieces.*;

public class ChessCSVReaderTest {
    Piece[][] expected_standard_board;

    @Before
    public void init() {
        Rook black_rook = new Rook(false);
        Knight black_knight = new Knight(false);
        Bishop black_bishop = new Bishop(false);
        Queen black_queen = new Queen(false);
        King black_king = new King(false);
        Pawn black_pawn = new Pawn(false);
        Rook white_rook = new Rook(true);
        Knight white_knight = new Knight(true);
        Bishop white_bishop = new Bishop(true);
        Queen white_queen = new Queen(true);
        King white_king = new King(true);
        Pawn white_pawn = new Pawn(true);
        expected_standard_board = new Piece[][]{
            {white_rook.copy(), white_knight.copy(), white_bishop.copy(), white_queen.copy(), white_king.copy(), white_bishop.copy(), white_knight.copy(), white_rook.copy()},
            {white_pawn.copy(), white_pawn.copy(), white_pawn.copy(), white_pawn.copy(), white_pawn.copy(), white_pawn.copy(), white_pawn.copy(), white_pawn.copy()},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {black_pawn.copy(), black_pawn.copy(), black_pawn.copy(), black_pawn.copy(), black_pawn.copy(), black_pawn.copy(), black_pawn.copy(), black_pawn.copy()},
            {black_rook.copy(), black_knight.copy(), black_bishop.copy(), black_queen.copy(), black_king.copy(), black_bishop.copy(), black_knight.copy(), black_rook.copy()}
        };
    }

    @Test
    public void readBoardCSVSmokeTest() {
        try {
            Piece[][] actual_standard_board = ChessCSVReader.readBoardCSV(
                "board_layouts/standard_board_layout.csv"
            );
            Assert.assertArrayEquals(expected_standard_board, actual_standard_board);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
