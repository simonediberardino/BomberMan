package game.events;

import game.Bomberman;

public class DeathGameEvent implements GameEvent {
    @Override
    public void invoke(Object arg) {
        Bomberman.getMatch().getCurrentLevel().onDeathGameEvent();
    }
}
