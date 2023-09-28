package game.events;

import game.Bomberman;

public class EnemyDespawnedGameEvent implements GameEvent {
    @Override
    public void invoke(Object arg) {
        if(!Bomberman.getMatch().getGameState()) return;

        System.out.println("KILLED ENEMY! " + Bomberman.getMatch().getEnemiesAlive());
        if(Bomberman.getMatch().getEnemiesAlive() == 0) {
            new AllEnemiesEliminatedGameEvent().invoke(null);
        }
    }
}
