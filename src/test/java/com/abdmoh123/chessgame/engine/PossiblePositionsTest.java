package com.abdmoh123.chessgame.engine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Assert;
import org.junit.Before;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.control.Player;
import com.abdmoh123.chessgame.control.RandomBot;
import com.abdmoh123.chessgame.moves.Move;

@RunWith(Parameterized.class)
public class PossiblePositionsTest {
    private int num_iterations;
    private int expected_num_positions;
    private Board standard_board;

    @Before
    public void init() {
        standard_board = new StandardBoard();
        standard_board.initialise();
    }

    private static int countNumPositions(Board chess_board, int current_iteration, int max_iterations, Player[] players) {
        if (current_iteration == max_iterations) {
            return 1;
        }

        Player current_player;
        if (current_iteration % 2 == 0) {
            current_player = players[0];
        }
        else {
            current_player = players[1];
        }

        int num_positions = 0;
        List<Space> friendly_spaces = chess_board.getFriendlySpaces(current_player.isWhite());
        for (Space space : friendly_spaces) {
            List<Move> legal_moves = current_player.getLegalMoves(space, chess_board);
            for (Move move : legal_moves) {
                num_positions += countNumPositions(chess_board.after(move), current_iteration + 1, max_iterations, players);
            }
        }

        return num_positions;
    }

    public PossiblePositionsTest(int num_iterations, int expected_num_positions) {
        this.num_iterations = num_iterations;
        this.expected_num_positions = expected_num_positions;
    }

    @Parameterized.Parameters
    public static Collection iterations() {
        return Arrays.asList(new Object[][] {
            { 0, 1 },
            { 1, 20 },
            { 2, 400 },
            { 3, 8902 },
            { 4, 197742 }
        });
    }

    @Test
    public void testPossiblePositions() {
        /*
        n | # of positions
        0 | 1
        1 | 20
        2 | 400
        3 | 8902
        4 | 197742
        5 | 4897256
        6 | 120921506
        7 | 3284294545
        */
        // TODO: Make code pass this test

        Player[] players = {new RandomBot(true), new RandomBot(false)};
        Assert.assertEquals(expected_num_positions, countNumPositions(standard_board, 0, num_iterations, players));
    }
}
