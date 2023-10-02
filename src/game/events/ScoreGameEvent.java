package game.events;

import game.Bomberman;
import game.data.DataInputOutput;
import game.entity.models.Character;

/**
 * Fires a game event using Strategy Pattern;
 */
public class ScoreGameEvent implements GameEvent{
    @Override
    public void invoke(Object arg) {
        Bomberman.getMatch().getCurrentLevel().onScoreGameEvent((int) arg);
    }
}
