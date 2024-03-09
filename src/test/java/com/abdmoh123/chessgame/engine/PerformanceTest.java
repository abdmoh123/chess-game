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
import com.abdmoh123.chessgame.moves.Move;
import com.abdmoh123.chessgame.players.Player;
import com.abdmoh123.chessgame.players.bots.RandomBot;

@RunWith(Parameterized.class)
public class PerformanceTest {
    private int depth;
    private int[] expected_results;
    private Engine chess_engine;
    private Player[] players;

    public PerformanceTest(int depth_in, int[] expected_results_in) {
        this.depth = depth_in;
        this.expected_results = expected_results_in;
    }

    @Parameterized.Parameters
    public static List<Object[]> iterations() {
        List<Object[]> parameters = new ArrayList<>();

        parameters.add(new Object[]{0, new int[]{1, 0, 0, 0}});
        parameters.add(new Object[]{1, new int[]{20, 0, 0, 0}});
        parameters.add(new Object[]{2, new int[]{400, 0, 0, 0}});
        parameters.add(new Object[]{3, new int[]{8902, 34, 12, 0}});
        // TODO: Make tests below pass
        // parameters.add(new Object[]{4, new int[]{197281, 1576, 469, 8}});
        // parameters.add(new Object[]{5, new int[]{4865609, 82719, 27351, 347}});

        return parameters;
    }

    @Before
    public void init() {
        Board chess_board = new StandardBoard();
        chess_board.initialise();
        this.chess_engine = new Engine(chess_board);

        this.players = new Player[]{new RandomBot(true), new RandomBot(false)};
    }

    private int[] runPerfTest(int depth_in) {
        /* Count all possible combinations of legal moves of a given depth */

        // update node count
        if (depth_in == 0) {
            return new int[]{1, 0, 0, 0};
        }

        boolean is_white_turn;
        if ((this.depth - depth_in) % 2 == 0) {
            is_white_turn = true;
        }
        else {
            is_white_turn = false;
        }

        int nodes = 0;
        int captures = 0;
        int checks = 0;
        int check_mates = 0;

        List<Space> friendly_spaces = chess_engine.getBoard().getFriendlySpaces(is_white_turn);
        for (Space space : friendly_spaces) {
            List<Move> legal_moves = chess_engine.generateLegalMoves(space, is_white_turn);
            for (Move move : legal_moves) {
                if (move.getKillPoints() > 0) { ++captures; } // update captures count
                if (chess_engine.isCheckAfterMove(move, !is_white_turn)) { ++checks; } // update checks count
                if (chess_engine.isCheckMateAfterMove(move, !is_white_turn)) { ++check_mates; } // update checkmate count

                chess_engine.applyMoveToBoard(move);
                int[] results_array = runPerfTest(depth_in - 1);
                nodes += results_array[0];
                captures += results_array[1];
                checks += results_array[2];
                check_mates += results_array[3];
                chess_engine.undoMoveToBoard(move);
            }
        }

        return new int[]{nodes, captures, checks, check_mates};
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

        List<Space> friendly_spaces = chess_engine.getBoard().getFriendlySpaces(current_player.isWhite());
        List<Move> legal_moves = new ArrayList<>();
        for (Space space : friendly_spaces) {
            legal_moves.addAll(chess_engine.generateLegalMoves(space, current_player.isWhite()));
        }

        // print header of test results
        System.out.println("Divide test depth " + depth_in);

        if (depth_in == 1) {
            displayDivideResults(legal_moves, null);
        }

        List<Integer> nodes_list = new ArrayList<>();
        for (Move move : legal_moves) {
            chess_engine.applyMoveToBoard(move);
            nodes_list.add(runPerfTest(depth_in - 1)[0]); // 0 index = number of possible move combinations
            chess_engine.undoMoveToBoard(move);
        }
        displayDivideResults(legal_moves, nodes_list);
    }

    @Test
    public void testEngine() {
        /* Test accuracy/performance of the engine */

        int[] actual_results = runPerfTest(this.depth);
        System.out.printf(
            "[Depth %d]: Nodes = %d/%d, Captures = %d/%d, Checks = %d/%d, Checkmates = %d/%d\n",
            this.depth,
            actual_results[0], expected_results[0],
            actual_results[1], expected_results[1],
            actual_results[2], expected_results[2],
            actual_results[3], expected_results[3]
        );

        /* [Depth 4]: Nodes = 196165/197281, Captures = 1416/1576, Checks = 481/469
         * Divide test depth 4
         * a2a3 8419/8457
         * a2a4 9287/9345
         * b1c3 9711/9755
         * b1a3 8845/8885
         * b2b3 9303/9345
         * b2b4 9290/9332
         * c2c3 9230/9272
         * c2c4 9700/9744
         * d2d3 11905/11959
         * d2d4 12379/12435
         * e2e3 13074/13134
         * e2e4 13100/13160
         * f2f3 8419/8457
         * f2f4 8889/8929
         * g1h3 8841/8881
         * g1f3 9704/9748
         * g2g3 9303/9345
         * g2g4 9286/9328
         * h2h3 8419/8457
         * h2h4 9287/9329
         * 
         * [Depth 5]: Nodes = 4829627/4865609, Captures = 78669/82719, Checks = 27696/27351
         * Divide test depth 5
         * a2a3: 180043
         * a2a4: 215603
         * b1c3: 233347
         * b1a3: 197489
         * b2b3: 213934
         * b2b4: 214930
         * c2c3: 220528
         * c2c4: 238264
         * d2d3: 325509
         * d2d4: 358492
         * e2e3: 399339
         * e2e4: 401692
         * f2f3: 177870
         * f2f4: 197283
         * g1h3: 197307
         * g1f3: 232090
         * g2g3: 215889
         * g2g4: 212768
         * h2h3: 180019
         * h2h4: 217231
         */

        // runPerfTestDivide(depth);

        Assert.assertArrayEquals(expected_results, actual_results);
    }
}
