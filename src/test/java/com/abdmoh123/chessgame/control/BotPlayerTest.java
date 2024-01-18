package com.abdmoh123.chessgame.control;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.StandardBoard;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class BotPlayerTest {
    List<BotPlayer> bot_list = new ArrayList<>();
    Board chess_board = new StandardBoard();

    @Before
    public void init() {
        for (BotType bot_type : BotType.values()) {
            bot_list.add(BotFactory.createBot(bot_type, true));
        }
        chess_board.initialise();
    }

    @Test
    public void testThinkingTime() {
        for (BotPlayer bot : bot_list) {
            if (bot.getThinkingTime() < 0) {
                Assert.fail("Time taken to think cannot be negative!");
            }
            else if (bot.getThinkingTime() > 0) {
                long start_time = System.currentTimeMillis();
                bot.startMove(chess_board);
                long end_time = System.currentTimeMillis();
                long time_taken = end_time - start_time;

                // actual time taken should never be greater than double the given thinking time (leeway added)
                Assert.assertTrue(time_taken < 2 * bot.getThinkingTime());
            }
        }
    }
}