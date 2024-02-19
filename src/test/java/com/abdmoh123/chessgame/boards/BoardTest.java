package com.abdmoh123.chessgame.boards;

import org.junit.Before;
import org.junit.Test;

import com.abdmoh123.chessgame.pieces.King;
import com.abdmoh123.chessgame.pieces.Pawn;
import com.abdmoh123.chessgame.pieces.Piece;
import com.abdmoh123.chessgame.utils.ChessCSVReader;

import static org.junit.Assert.assertTrue;

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

    @Test
    public void initialiseFENTestCastlingTrue() {
        Board fen_board = new StandardBoard();
        
        fen_board.initialiseFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");
        King white_king = (King) fen_board.getPiece(new Space(4, 0));
        Assert.assertFalse(white_king.hasCastled());
    }

    @Test(expected = RuntimeException.class)
    public void initialiseFENTestCastlingFalse() {
        Board fen_board = new StandardBoard();
        
        fen_board.initialiseFEN("rnbqkbnr/ppppppp1/7p/8/8/4P3/PPPPKPPP/RNBQ1BNR b KQkq - 1 3");
        Assert.fail("FEN test Castling false failed!"); // fail test if exception was not thrown
    }

    @Test
    public void initialiseFENTestEnPassantTrue() {
        Board fen_board = new StandardBoard();

        fen_board.initialiseFEN("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq a3 0 1");
        Pawn white_pawn = (Pawn) fen_board.getPiece(new Space(0, 3));
        Assert.assertTrue(white_pawn.isEnPassant());
    }

    @Test(expected = RuntimeException.class)
    public void initialiseFENTestEnPassantFalse() {
        Board fen_board = new StandardBoard();

        fen_board.initialiseFEN("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq a4 0 1");
        Assert.fail("FEN test En Passsant false failed!"); // fail test if exception was not thrown
    }
}
