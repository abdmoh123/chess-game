package com.abdmoh123.chessgame.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Assert;

import java.util.List;
import java.util.ArrayList;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.Space;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.control.Player;
import com.abdmoh123.chessgame.control.RandomBot;
import com.abdmoh123.chessgame.moves.Move;

@RunWith(Parameterized.class)
public class PerformanceTest {
    private int depth;
    private int[] expected_results;
    private Board standard_board;
    private Player[] players;

    public PerformanceTest(int depth_in, int[] expected_results_in) {
        this.depth = depth_in;
        this.expected_results = expected_results_in;
    }

    @Parameterized.Parameters
    public static List<Object[]> iterations() {
        List<Object[]> parameters = new ArrayList<>();

        parameters.add(new Object[]{0, new int[]{1, 0, 0}});
        parameters.add(new Object[]{1, new int[]{20, 0, 0}});
        parameters.add(new Object[]{2, new int[]{400, 0, 0}});
        parameters.add(new Object[]{3, new int[]{8902, 34, 12}}); // nodes = 8346/8902, captures = 22/34, checks = 8/12
        parameters.add(new Object[]{4, new int[]{197281, 1576, 469}});
        parameters.add(new Object[]{5, new int[]{4865609, 82719, 27351}});

        return parameters;
    }

    @Before
    public void init() {
        this.standard_board = new StandardBoard();
        this.standard_board.initialise();

        this.players = new Player[]{new RandomBot(true), new RandomBot(false)};
    }

    private int[] runPerfTest(int depth_in) {
        /* Count all possible combinations of legal moves of a given depth */

        // update node count
        if (depth_in == 0) {
            return new int[]{1, 0, 0};
        }

        Player current_player;
        if ((this.depth - depth_in) % 2 == 0) {
            current_player = this.players[0];
        }
        else {
            current_player = this.players[1];
        }

        int nodes = 0;
        int captures = 0;
        int checks = 0;

        List<Space> friendly_spaces = this.standard_board.getFriendlySpaces(current_player.isWhite());
        for (Space space : friendly_spaces) {
            List<Move> legal_moves = current_player.getLegalMoves(space, this.standard_board);
            for (Move move : legal_moves) {
                if (move.getKillPoints() > 0) { ++captures; } // update captures count
                if (current_player.isEnemyCheckAfterMove(move, standard_board)) { ++checks; } // update checks count

                this.standard_board = this.standard_board.after(move);

                int[] results_array = runPerfTest(depth_in - 1);
                nodes += results_array[0];
                captures += results_array[1];
                checks += results_array[2];

                this.standard_board = this.standard_board.before(move);
            }
        }

        return new int[]{nodes, captures, checks};
    }

    private void displayDivideResults(List<Move> moves, List<Integer> perft_results) {
        // if depth = 1, output all the moves with node results = 0
        if (perft_results == null) {
            for (Move move : moves) {
                String move_name = move.toString();
                System.out.printf("%s: 0\n", move_name);
            }
            return;
        }

        if (moves.size() != perft_results.size()) {
            System.out.println("Error! Lists are unequal in length!");
        }

        // output format example: aa3: 20
        for (int i = 0; i < moves.size(); ++i) {
            String move_name = moves.get(i).toString();
            int num_nodes = perft_results.get(i);
            System.out.printf("%s: %d\n", move_name, num_nodes);
        }
    }
    private void runPerfTestDivide(int depth_in) {
        /* List all moves (at depth = 1) and for each move, output the perft result at the given depth. Useful for debugging. */

        if (depth_in < 1) {
            System.out.println("Depth too low! Must be > 1");
            return;
        }

        Player current_player;
        if ((this.depth - depth_in) % 2 == 0) {
            current_player = this.players[0];
        }
        else {
            current_player = this.players[1];
        }

        List<Space> friendly_spaces = this.standard_board.getFriendlySpaces(current_player.isWhite());
        List<Move> legal_moves = new ArrayList<>();
        for (Space space : friendly_spaces) {
            legal_moves.addAll(current_player.getLegalMoves(space, this.standard_board));
        }

        // print header of test results
        System.out.println("Divide test depth " + depth_in);

        if (depth_in == 1) {
            displayDivideResults(legal_moves, null);
        }

        List<Integer> nodes_list = new ArrayList<>();
        for (Move move : legal_moves) {
            this.standard_board = this.standard_board.after(move);
            nodes_list.add(runPerfTest(depth_in - 1)[0]); // 0 index = number of possible move combinations
            this.standard_board = this.standard_board.before(move);
        }
        displayDivideResults(legal_moves, nodes_list);
    }

    @Test
    public void testEngine() {
        // TODO: Make code pass this test

        /* depth 3 errors (compared to stockfish):
            b2b3 380/420
            b2b4 381/421
            d2d3 440/539
            d2d4 461/560
            e2e3 500/599
            e2e4 501/600
            g2g3 380/420
            g2g4 381/421
        */

        int[] actual_results = runPerfTest(this.depth);
        System.out.printf(
            "[Depth %d]: Nodes = %d/%d, Captures = %d/%d, Checks = %d/%d\n",
            this.depth,
            actual_results[0], expected_results[0],
            actual_results[1], expected_results[1],
            actual_results[2], expected_results[2]
        );

        runPerfTestDivide(depth);

        Assert.assertArrayEquals(expected_results, actual_results);
    }
}
