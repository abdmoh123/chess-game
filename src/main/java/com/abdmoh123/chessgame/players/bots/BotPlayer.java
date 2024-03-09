package com.abdmoh123.chessgame.players.bots;

import com.abdmoh123.chessgame.players.Player;

public abstract class BotPlayer extends Player {
    protected BotPlayer(boolean is_white_in) {
        super(is_white_in);
    }
}
