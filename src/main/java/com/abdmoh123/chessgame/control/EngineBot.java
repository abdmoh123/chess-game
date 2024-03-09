package com.abdmoh123.chessgame.control;

import com.abdmoh123.chessgame.control.engine.Engine;
import com.abdmoh123.chessgame.moves.Move;

public class EngineBot extends BotPlayer {
    private int search_depth;
    public EngineBot(boolean is_white_in, int depth_in) {
        super(is_white_in);
        this.search_depth = depth_in;
    }

    @Override
    public Move startMove(Engine chess_engine) {
        return chess_engine.getBestMove(isWhite(), search_depth);
    }
}
