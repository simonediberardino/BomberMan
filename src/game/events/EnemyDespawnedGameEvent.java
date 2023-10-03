package game.events;

import game.Bomberman;

public class EnemyDespawnedGameEvent implements GameEvent {
    @Override
    public void invoke(Object arg) {
        if (!Bomberman.getMatch().getGameState()) return;

        Bomberman.getMatch().getCurrentLevel().onEnemyDespawned();
    }
}
