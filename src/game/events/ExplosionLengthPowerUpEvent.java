package game.events;

import game.Bomberman;


/**
 * Fires a game event using Strategy Pattern;
 */
public class ExplosionLengthPowerUpEvent implements GameEvent {
    @Override
    public void invoke(Object arg) {
        Bomberman.getMatch().getCurrentLevel().explosionLengthPowerUpEvent();
    }
}
