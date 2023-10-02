package game.events;

import game.Bomberman;
import game.data.DataInputOutput;

import static game.data.DataInputOutput.increaseExplosionLength;
import static game.data.DataInputOutput.updateStoredPlayerData;

/**
 * Fires a game event using Strategy Pattern;
 */
public class ExplosionLengthPowerUpEvent implements GameEvent{
    @Override
    public void invoke(Object arg) {
        Bomberman.getMatch().getCurrentLevel().explosionLengthPowerUpEvent();
    }
}
