package game.events;

import game.Bomberman;
import game.data.DataInputOutput;

/**
 * Fires a game event using Strategy Pattern;
 */
public class UpdateMaxBombsEvent implements GameEvent{
    @Override
    public void invoke(Object arg) {
        Bomberman.getMatch().getCurrentLevel().onUpdateMaxBombsGameEvent((int) arg);
    }
}
