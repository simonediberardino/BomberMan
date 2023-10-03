package game.entity.models;


import game.Bomberman;
import game.entity.Player;
import game.entity.bomb.AbstractExplosion;
import game.entity.bomb.ConfettiExplosion;
import game.events.EnemyDespawnedGameEvent;
import game.events.KilledEnemyEvent;
import game.events.ScoreGameEvent;
import game.ui.panels.game.PitchPanel;

import java.util.*;

public abstract class Enemy extends Character {
    public Enemy() {
        this(null);
        setCoords(Coordinates.randomCoordinatesFromPlayer(getSize()));
    }

    public Enemy(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    protected void doInteract(Entity e) {
        if (e instanceof BomberEntity) {
            attack(e);
        }
    }

    @Override
    protected void onSpawn() {
        super.onSpawn();
        Bomberman.getMatch().increaseEnemiesAlive();
        Bomberman.getMatch().getGameTickerObservable().register(this);
    }

    @Override
    protected void onDespawn() {
        super.onDespawn();
        Bomberman.getMatch().decreaseEnemiesAlive();
        Bomberman.getMatch().getGameTickerObservable().unregister(this);
        new EnemyDespawnedGameEvent().invoke(null);
    }

    @Override
    public Set<Class<? extends Entity>> getInteractionsEntities() {
        return new HashSet<>(Collections.singletonList(Player.class));
    }

    public final void update(Object gameState) {
        super.update(gameState);

        if ((boolean) gameState)
            doUpdate((Boolean) gameState);
    }

    public abstract void doUpdate(boolean gameState);

    public int getSize() {
        return PitchPanel.GRID_SIZE;
    }

    @Override
    protected Set<Class<? extends Entity>> getBasePassiveInteractionEntities() {
        return new HashSet<>(Collections.singletonList(AbstractExplosion.class));
    }

}
