package com.abdmoh123.chessgame.control;

public class BotFactory {
    public static BotPlayer createBot(BotType type, boolean is_white_in) {
        if (type == BotType.RANDOM_BOT) { return new RandomBot(is_white_in); }
        return null;
    }
}
