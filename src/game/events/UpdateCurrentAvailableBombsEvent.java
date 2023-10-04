package game.events;

import game.Bomberman;

/**
 * Fires a game event using Strategy Pattern;
 */
public class UpdateCurrentAvailableBombsEvent implements GameEvent {
    @Override
    public void invoke(Object arg) {
        if (Bomberman.getMatch().getPlayer() == null)
            return;

        Bomberman.getMatch().getCurrentLevel().onUpdateCurrentAvailableBombsEvent((int) arg);
        Bomberman.getMatch().updateInventoryWeaponController();
    }
}
