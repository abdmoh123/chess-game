package com.abdmoh123.chessgame.control;

import com.abdmoh123.chessgame.moves.Move;

public abstract class Player {
    private final boolean IS_WHITE;
    private int points;

    protected Player(boolean is_white_in) {
        this.IS_WHITE = is_white_in;
    }

    public boolean isWhite() {
        return this.IS_WHITE;
    }
    public int getPoints() {
        return this.points;
    }
    public void addPoints(int points_in) {
        this.points += points_in;
    }

    public abstract Move startMove(Engine chess_engine);
}
