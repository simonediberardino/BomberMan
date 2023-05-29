package game.entity.enemies.npcs;

import game.entity.Player;
import game.entity.models.*;
import game.entity.models.Coordinates;
import game.entity.models.Direction;

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

        // If it hasn't been long enough since the last direction update, keep moving in the same direction, unless last move was blocked
        if (!forceChange && currentTime - lastDirectionUpdate < DIRECTION_REFRESH_RATE) {
            return currDirection;
        }

        // Get a list of available directions
        List<Direction> availableDirections = new ArrayList<>();
        for (Direction direction : getAvailableDirections()) {
            if (getSupportedDirections().contains(direction)) {
                availableDirections.add(direction);
            }
        }

        if (availableDirections.isEmpty()) {
            return currDirection;
        }

        // Choose a new direction randomly, or keep the current direction with a certain probability
        double randomValue = Math.random() * 100;
        if (randomValue > CHANGE_DIRECTION_RATE) {
            return currDirection;
        }

        // Choose a random direction from the available options
        int randomIndex = (int) (Math.random() * availableDirections.size());
        return availableDirections.get(randomIndex);
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
}