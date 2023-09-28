package game.events;

import game.Bomberman;

public class AllEnemiesEliminatedGameEvent implements GameEvent {
    @Override
    public void invoke(Object arg) {
        Bomberman.getMatch().getCurrentLevel().onAllEnemiesEliminated();
    }
}
