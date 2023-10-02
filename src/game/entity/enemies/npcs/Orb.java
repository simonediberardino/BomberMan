package game.entity.enemies.npcs;

import game.entity.Player;
import game.entity.bomb.Bomb;
import game.entity.models.Enemy;
import game.entity.models.Entity;
import game.entity.models.Coordinates;
import game.entity.models.Direction;
import game.entity.models.EnhancedDirection;
import game.ui.panels.game.PitchPanel;
import game.utils.Paths;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/**

 The Orb class represents a little enemy entity that moves in a specific direction.
 It can be instantiated with either an EnhancedDirection or a Direction, but not both.
 The Orb class implements the Transparent and Particle interfaces.
 */
public abstract class Orb extends Enemy {

    /**
     The size of the Orb.
     */
    public static final int SIZE = PitchPanel.COMMON_DIVISOR * 2;

    /**
     Only one field between enhancedDirection and direction can be instantiated at a time.
     The enhancedDirection represents a direction that has been enhanced with additional directions.
     */
    protected EnhancedDirection enhancedDirection = null;

    /**
     The direction represents the basic direction that the Orb moves in.
     */
    protected Direction direction = null;

    public Orb() {
        super();
    }
    /**
     Constructs an Orb with the given coordinates and enhanced direction.
     @param coordinates the coordinates of the Orb
     @param enhancedDirection the enhanced direction of the Orb
     */
    public Orb(Coordinates coordinates, EnhancedDirection enhancedDirection) {
        super(coordinates);
        this.enhancedDirection = enhancedDirection;
    }

    /**
     Constructs an Orb with the given coordinates and direction.
     @param coordinates the coordinates of the Orb
     @param direction the direction of the Orb
     */
    public Orb(Coordinates coordinates, Direction direction) {
        super(coordinates);
        this.direction = direction;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    /**
     Performs an interaction with the given entity.
     @param e the entity to interact with
     */
    @Override
    //
    protected void doInteract(Entity e) {
        if (canInteractWith(e)) {
            attack(e);
        }

        if (isObstacle(e)) {
            attack(this);
        }
    }

    /**
     Returns the set of interaction entities for the Orb.
     @return the set of interaction entities for the Orb
     */
    @Override
    public Set<Class<? extends Entity>> getInteractionsEntities() {
        return new HashSet<>(Arrays.asList(Player.class, Bomb.class));
    }
    /**

     Returns whether the given entity is an obstacle.
     @param e the entity to check
     @return true if the entity is null, false otherwise
     */
    //
    @Override
    public boolean isObstacle(Entity e){
        return e == null;
    }

    /**
     Moves the Orb in the appropriate direction or directions.
     */
    private void moveOrb(){
        if(!canMove || !isAlive) return;

        if(enhancedDirection == null){
            move(direction);
            return;
        }

        for (Direction d : enhancedDirection.toDirection()) {
            moveOrInteract(d);
        }
    }

    @Override
    public float getSpeed() {
        return 1.5f;
    }

    public void doUpdate(boolean gameState) {
        if(gameState) moveOrb();
    }
}
