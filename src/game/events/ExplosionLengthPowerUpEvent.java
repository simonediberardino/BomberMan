package game.events;

import game.Bomberman;
import game.entity.models.BomberEntity;
import game.entity.models.Entity;


/**
 * Fires a game event using Strategy Pattern;
 */
public class ExplosionLengthPowerUpEvent implements GameEvent {
    @Override
    public void invoke(Object arg) {
        Bomberman.getMatch().getCurrentLevel().onUpdateBombsLengthEvent((BomberEntity) arg, ((BomberEntity) arg).getCurrExplosionLength() + 1);
    }
}
