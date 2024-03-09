package com.abdmoh123.chessgame.control;

public class BotFactory {
    public static BotPlayer createBot(BotType bot_type_in, boolean is_white_in) {
        switch (bot_type_in) {
            case RANDOM_BOT: return new RandomBot(is_white_in);
            case ENGINE_BOT: return new EngineBot(is_white_in, 3);
            default: return null;
        }
    }
}
