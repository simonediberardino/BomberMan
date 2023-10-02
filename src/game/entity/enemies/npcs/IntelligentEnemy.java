package game.entity.enemies.npcs;

import game.entity.Player;
import game.entity.models.*;
import game.entity.models.Coordinates;
import game.entity.models.Direction;
import game.events.KilledEnemyEvent;
import game.events.ScoreGameEvent;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class IntelligentEnemy extends Enemy implements ICPU {
    public int CHANGE_DIRECTION_RATE = 10; // percentage
    public static final int DIRECTION_REFRESH_RATE = 500;

    public IntelligentEnemy(){
        super();
        hitboxSizeToHeightRatio = 0.733f;
    }

    public IntelligentEnemy(Coordinates coordinates) {
        super(coordinates);
        hitboxSizeToHeightRatio = 0.733f;
    }

    @Override
    protected final void doInteract(Entity e) {
        if (e instanceof BomberEntity) {
            super.doInteract(e);
        }
        else if (isObstacle(e) || e == null) {
            changeDirection();
        }
    }

    /**
     * Chooses a new direction for the agent to move in, and sends the corresponding command to the game engine.
     *
     * @param forceChange If true, the agent will be forced to change direction even if it just changed directions.
     *                    If false, there is a chance the agent will keep its current direction.
     * @return new direction
     */
    @Override
    public Direction chooseDirection(boolean forceChange) {
        // Get the current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Get a list of all the available directions the agent can move in
        List<Direction> availableDirections = getAvailableDirections()
                .stream()
                .filter(e -> getSupportedDirections().contains(e))
                .collect(Collectors.toList());

        // If forceChange is true, remove the current direction from the list of available directions

        // If it hasn't been long enough since the last direction update, keep moving in the same direction, unless last move was blocked
        if (currentTime - lastDirectionUpdate < DIRECTION_REFRESH_RATE && !forceChange) {
            return currDirection;
        }

        if (availableDirections.isEmpty()) {
            return currDirection;
        }

        // Choose a new direction randomly, or keep the current direction with a certain probability
        Direction newDirection = null;
        if (Math.random() * 100 > CHANGE_DIRECTION_RATE) {
            newDirection = currDirection;
        }

        // If a new direction hasn't been chosen, choose one randomly from the available options
        if (newDirection == null) {
            newDirection = availableDirections.get((int) (Math.random() * availableDirections.size()));
        }

        // Send the command corresponding to the new direction to the game engine
        return newDirection;
    }

    @Override
    public void changeDirection() {
        updateLastDirection(chooseDirection(true));
    }

    @Override
    public Set<Class<? extends Entity>> getInteractionsEntities(){
        return new HashSet<>(Collections.singletonList(Player.class));
    }

    public void doUpdate(boolean gameState) {
        if (!canMove || !gameState) {
            return;
        }

        move(chooseDirection(false));
    }

    @Override
    protected void onEliminated() {
        super.onEliminated();
        new KilledEnemyEvent().invoke(this);
        new ScoreGameEvent().invoke(this.getMaxHp());
    }
}