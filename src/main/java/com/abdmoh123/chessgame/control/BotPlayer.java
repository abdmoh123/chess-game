package com.abdmoh123.chessgame.control;

public abstract class BotPlayer extends Player {
    private final int THINKING_TIME_MS; // stored as milliseconds

    protected BotPlayer(boolean is_white_in, int thinking_time_in) {
        super(is_white_in);
        this.THINKING_TIME_MS = thinking_time_in;
    }

    protected int getThinkingTime() {
        return this.THINKING_TIME_MS;
    }
}
