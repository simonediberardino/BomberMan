package game.events;

import game.Bomberman;
import game.data.DataInputOutput;

public class DeathGameEvent implements GameEvent{
    @Override
    public void invoke(Object arg) {
        Bomberman.getMatch().getCurrentLevel().onDeathGameEvent();
    }
}
