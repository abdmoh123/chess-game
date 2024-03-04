package com.abdmoh123.chessgame.control;

public abstract class BotPlayer extends Player {
    protected BotPlayer(boolean is_white_in, int thinking_time_in) {
        super(is_white_in);
    }
}
