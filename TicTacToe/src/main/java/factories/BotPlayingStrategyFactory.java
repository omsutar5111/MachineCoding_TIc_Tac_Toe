package factories;

import models.BotLevel;
import strategies.botPlaying.EasyBotPlayingStrategy;
import strategies.botPlaying.botPlayingStrategy;

import static models.BotLevel.EASY;

public class BotPlayingStrategyFactory {

    public static botPlayingStrategy getBotPlayingStrategy(BotLevel botLevel){
        switch (botLevel){
            case EASY:
            case MEDIUM:
            case DIFFICULT:
                return new EasyBotPlayingStrategy();
            default:
                throw new UnsupportedOperationException("The given bot level is not supported");
        }

    }
}
