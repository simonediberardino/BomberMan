package game.events;

import game.Bomberman;

/**
 * Fires a game event using Strategy Pattern;
 */
public class RoundPassedGameEvent implements GameEvent {
    @Override
    public void invoke(Object arg) {
        Bomberman.getMatch().getCurrentLevel().onRoundPassedGameEvent();
    }
}
