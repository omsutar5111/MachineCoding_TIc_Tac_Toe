package strategies.botPlaying;

import models.Board;
import models.Pair;

public interface botPlayingStrategy {
    public Pair<Integer, Integer> makeMove(Board board);
}
