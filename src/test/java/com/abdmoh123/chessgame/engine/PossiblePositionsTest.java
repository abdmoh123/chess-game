package com.abdmoh123.chessgame.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Assert;

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
    private int depth;
    private int expected_num_positions;
    private Board standard_board;
    private Player[] players;

    @Before
    public void init() {
        this.standard_board = new StandardBoard();
        this.standard_board.initialise();
        this.players = new Player[]{new RandomBot(true), new RandomBot(false)};
    }

    private int countNumPositions(int current_depth) {
        if (current_depth == 0) {
            return 1;
        }

        Player current_player;
        if ((this.depth - current_depth) % 2 == 0) {
            current_player = this.players[0];
        }
        else {
            current_player = this.players[1];
        }

        int num_positions = 0;
        List<Space> friendly_spaces = this.standard_board.getFriendlySpaces(current_player.isWhite());
        for (Space space : friendly_spaces) {
            List<Move> legal_moves = current_player.getLegalMoves(space, this.standard_board);
            for (Move move : legal_moves) {
                this.standard_board = this.standard_board.after(move);
                num_positions += countNumPositions(current_depth - 1);
                this.standard_board = this.standard_board.before(move);
            }
        }

        return num_positions;
    }

    public PossiblePositionsTest(int depth_in, int expected_num_positions_in) {
        this.depth = depth_in;
        this.expected_num_positions = expected_num_positions_in;
    }

    @Parameterized.Parameters
    public static Collection iterations() {
        return Arrays.asList(new Object[][] {
            { 0, 1 },
            { 1, 20 },
            { 2, 400 },
            { 3, 8902 },
            { 4, 197742 },
            { 5, 4897256 }
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

        Assert.assertEquals(expected_num_positions, countNumPositions(this.depth));
    }
}
