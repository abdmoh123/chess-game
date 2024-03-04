package com.abdmoh123.chessgame.control;

import com.abdmoh123.chessgame.boards.Board;
import com.abdmoh123.chessgame.boards.StandardBoard;
import com.abdmoh123.chessgame.control.engine.Engine;

import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class BotPlayerTest {
    List<BotPlayer> bot_list = new ArrayList<>();
    Engine chess_engine;

    @Before
    public void init() {
        for (BotType bot_type : BotType.values()) {
            bot_list.add(BotFactory.createBot(bot_type, true));
        }
        Board chess_board = new StandardBoard();
        chess_board.initialise();
        chess_engine = new Engine(chess_board);
    }
}
