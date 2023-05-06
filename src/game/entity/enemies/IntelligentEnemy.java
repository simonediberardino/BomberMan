package game.entity.enemies;

import game.BomberMan;
import game.entity.Player;
import game.entity.blocks.DestroyableBlock;
import game.entity.bomb.Bomb;
import game.entity.models.*;
import game.entity.models.Character;
import game.models.Coordinates;
import game.models.Direction;

import java.util.*;

public abstract class IntelligentEnemy extends Enemy implements ICPU {
    public int CHANGE_DIRECTION_RATE = 10; // percentage
    private static final int DIRECTION_REFRESH_RATE = 500;


    public IntelligentEnemy(){
        super(null);
    }

    public IntelligentEnemy(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    protected final void doInteract(Entity e) {

        if (e instanceof BomberEntity) {
            super.doInteract(e);
        }

        else if (isObstacle(e)) {
            changeDirection();
        }
    }



    @Override
    protected void onSpawn() {
        BomberMan.getInstance().getGameTickerObservable().addObserver(this);
    }

    @Override
    protected void onDespawn(){
       // BomberMan.getInstance().getGameTickerObservable().deleteObserver(this);
    }

    @Override
    public float getImageRatio(){
        return 0.73f;
    }

    /**
     * Chooses a new direction for the agent to move in, and sends the corresponding command to the game engine.
     *
     * @param forceChange If true, the agent will be forced to change direction even if it just changed directions.
     *                    If false, there is a chance the agent will keep its current direction.
     */
    @Override
    public void chooseDirection(boolean forceChange) {
        // Get the current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Get a list of all the available directions the agent can move in
        List<Direction> availableDirections = getAvailableDirections();

        // If forceChange is true, remove the current direction from the list of available directions

        // If it hasn't been long enough since the last direction update, keep moving in the same direction, unless last move was blocked
        if (currentTime - lastDirectionUpdate < DIRECTION_REFRESH_RATE && !forceChange) {
            move(currDirection);
            return;
        }

        if (availableDirections.isEmpty()) {
            return;
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
        move(newDirection);
    }

    @Override
    public void changeDirection() {
        chooseDirection(true);
    }

    @Override
    public Set<Class<? extends Entity>> getInteractionsEntities(){
        return new HashSet<>(Arrays.asList(Player.class, game.entity.models.Enemy.class, DestroyableBlock.class, Bomb.class, Block.class));
    }


    public void update(boolean gameState) {
        if(canMove&&gameState) chooseDirection(false);
    }
}