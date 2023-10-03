package game.events;

import game.Bomberman;
import game.entity.models.BomberEntity;

public class UpdateCurrentBombsLengthEvent implements GameEvent {
    @Override
    public void invoke(Object arg) {
        Bomberman.getMatch().getCurrentLevel().onUpdateBombsLengthEvent(Bomberman.getMatch().getPlayer(), (int) arg);
    }
}
