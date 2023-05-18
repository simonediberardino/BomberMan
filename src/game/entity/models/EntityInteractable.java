package game.entity.models;

import game.Bomberman;
import game.entity.blocks.DestroyableBlock;
import game.entity.blocks.HardBlock;
import game.entity.bomb.Bomb;
import game.entity.bomb.Explosion;
import game.models.Coordinates;
import game.models.Direction;
import game.utils.Utility;

import java.util.*;
import java.util.stream.Collectors;

import static game.models.Coordinates.getEntitiesOnCoordinates;
import static game.ui.panels.game.PitchPanel.GRID_SIZE;
import static game.ui.panels.game.PitchPanel.PIXEL_UNIT;


/**
 * An abstract class representing interactive entities, which can move or interact with other entities in the game.
 */
public abstract class EntityInteractable extends Entity {
    public static final long SHOW_DEATH_PAGE_DELAY_MS = 2500;
    public final static long INTERACTION_DELAY_MS = 500;
    private final Set<Class<? extends Entity>> whitelistObstacles = new HashSet<>();
    private final HashMap<Entity, Long> interactionMap = new HashMap<>();
    protected long lastDamageTime = 0;
    private int attackDamage = 100;

    /**
     * Gets the size of the entity in pixels.
     * @return the size of the entity
     */
    public abstract int getSize();

    /**
     * Constructs an interactive entity with the given coordinates.
     * @param coordinates the coordinates of the entity
     */
    public EntityInteractable(Coordinates coordinates) {
        super(coordinates);
    }

    public final void interact(Entity e) {
        if (e == null)
            return; // If the entity is null, there is nothing to interact with, so return.

        if (canInteractWith(e) && e.canBeInteractedBy(this)) {
            this.doInteract(e); // Interact with the entity.
            this.updateLastInteract(e); // Update the last interaction for this entity.
        }

        else if (e instanceof EntityInteractable && ((EntityInteractable) e).canInteractWith(this) && canBeInteractedBy(e)) {
            e.doInteract(this); // The entity is an EntityInteractable and can be interacted with this (or vice-versa), so interact with it.
            ((EntityInteractable) e).updateLastInteract(e); // Update the last interaction for the EntityInteractable entity.
        }
    }

    public void updateLastInteract(Entity e) {
        interactionMap.put(e, System.currentTimeMillis());
    }

    public long getLastInteraction(Entity e){
        return interactionMap.getOrDefault(e, 0L);
    }

    /**
     * Interacts with the given entity.
     * @param e the entity to interact with
     */
    @Override
    protected abstract void doInteract(Entity e);

    /**
     Gets the next coordinates in the given direction and with the given step size.
     @param d the direction to get the next coordinates in
     @param stepSize the step size to use
     @return the next coordinates in the given direction and with the given step size
     */
    public Coordinates nextCoords(Direction d, int stepSize) {
        int x = 0;
        int y = 0;

        // Determine the direction to move in
        switch (d) {
            case RIGHT: x = stepSize;break;
            case LEFT: x = -stepSize;break;
            case UP: y = -stepSize; break;
            case DOWN: y = stepSize; break;
        }

        // Calculate the next coordinates based on the current direction and step size
        return new Coordinates(x + getCoords().getX(), y + getCoords().getY());
    }

    /**

     Moves or interacts with other entities in the given direction and with the default step size and offset.
     @param d the direction to move or interact in
     @return true if the entity can move in the given direction, false otherwise
     */
    public boolean moveOrInteract(Direction d) {
        // Call the moveOrInteract method with the default step size
        return moveOrInteract(d, PIXEL_UNIT);
    }


    public boolean moveOrInteract(Direction d, int stepSize){
        return moveOrInteract(d, stepSize, false);
    }

    /**
     * Moves or interacts with other entities in the given direction and with the given step size and default offset.
     * @param d the direction to move or interact in
     * @param stepSize the step size to use
     */
    protected boolean moveOrInteract(Direction d, int stepSize, boolean ignoreMapBorders) {
        if(d == null) return false;

        // Get the coordinates of the next positions that will be occupied if the entity moves in a certain direction
        // with a given step size
        List<Coordinates> nextOccupiedCoords = getNewCoordinatesOnDirection(d, stepSize, GRID_SIZE/3 / 2);

        // Calculate the coordinates of the top-left corner of the next position
        Coordinates nextTopLeftCoords = nextCoords(d, stepSize);
        // Get a list of entities that are present in the next occupied coordinates
        List<Entity> interactedEntities = getEntitiesOnCoordinates(nextOccupiedCoords);

        if(!ignoreMapBorders && !nextTopLeftCoords.validate(this)){
            this.interact(null);
            return false;
        }

        //interactedEntities = interactedEntities.stream().filter(e-> canInteractWith(e) || isObstacle(e)).collect(Collectors.toList());

        // If there are no entities present in the next occupied coordinates, update the entity's position
        if (interactedEntities.isEmpty()) {
            setCoords(nextTopLeftCoords);
            return true;
        }

        // Initialize a flag to indicate whether the entity can move
        boolean canMove = true;

        // Loop through the list of entities present in the next occupied coordinates
        if (interactedEntities.stream().anyMatch(this::isObstacle)){
            List<Entity> temp = interactedEntities.stream().filter(this::isObstacle).collect(Collectors.toList());
            for (Entity e: temp) {
                interact(e);
            }
            canMove = false;
        }else{
            for (Entity e: interactedEntities) {
                interact(e);
            }
        }

        // If the entity can move or it is immune to bombs, update the entity's position
        if (canMove) {
            setCoords(nextTopLeftCoords);
        }
        // If the entity is an instance of 'Explosion' and it cannot move further, stop expanding the explosion
        else if (this instanceof Explosion) {
            ((Explosion) this).cantExpandAnymore();
        }


        // Return whether the entity can move or not
        return canMove;
    }

    public Set<Class<? extends Entity>> getWhiteListObstacles() {
        return whitelistObstacles;
    }

    public Set<Class<? extends Entity>> getObstacles() {
        return new HashSet<>(Arrays.asList(HardBlock.class, Bomb.class, Enemy.class, DestroyableBlock.class, BomberEntity.class));
    }

    public abstract Set<Class<? extends Entity>> getInteractionsEntities();

    public boolean isObstacle(Entity e){
        return e == null || getObstacles().stream().anyMatch(c -> c.isInstance(e)) && whitelistObstacles.stream().noneMatch(c -> c.isInstance(e));
    }

    public boolean canInteractWith(Entity e){
        if(e == null) return true;

        if(Utility.timePassed(getLastInteraction(e)) < INTERACTION_DELAY_MS) {
            return false;
        }

        return getInteractionsEntities().stream().anyMatch(c -> c.isInstance(e));
    }

    public int getAttackDamage(){
        return attackDamage;
    }

    public void setAttackDamage(int damage){
        attackDamage = damage;
    }

    public synchronized void attack(Entity e){
        if(e == null || e.isImmune()) return;

        if (e instanceof Character){
            ((Character) e).attackReceived(getAttackDamage());
        }
        else if (e instanceof Block) ((Block) e).destroy();
    }
}
