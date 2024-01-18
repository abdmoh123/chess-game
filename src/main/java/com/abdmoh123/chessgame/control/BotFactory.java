package com.abdmoh123.chessgame.control;

public class BotFactory {
    public static BotPlayer createBot(BotType bot_type_in, boolean is_white_in) {
        if (bot_type_in == BotType.RANDOM_BOT) { return new RandomBot(is_white_in); }
        return null;
    }
}
