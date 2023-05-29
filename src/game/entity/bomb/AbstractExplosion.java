package game.entity.bomb;

import game.Bomberman;
import game.entity.blocks.DestroyableBlock;
import game.entity.models.*;
import game.ui.panels.game.PitchPanel;
import game.utils.Paths;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static game.utils.Utility.loadImage;

public abstract class AbstractExplosion extends MovingEntity {
    public static int MAX_EXPLOSION_LENGTH = 5;
    public static final int SIZE = PitchPanel.COMMON_DIVISOR * 2;
    public static final int SPAWN_OFFSET = (PitchPanel.GRID_SIZE-SIZE) / 2;
    // The distance from the bomb where the explosion was created.
    protected final int distanceFromExplosive;

    // The maximum distance from the bomb that the explosion can travel.
    protected final int maxDistance;
    protected static final int BOMB_STATES = 3;
    protected boolean canExpand;

    // The direction of the explosion.
    protected final Direction direction;
    protected boolean appearing = true;
    protected int explosionState = 1;
    protected long lastRefresh = 0;
    protected final Explosive explosive;

    public AbstractExplosion(Coordinates coordinates, Direction direction, Explosive explosive) {
        this(coordinates, direction, 0, explosive);
    }

    public AbstractExplosion(Coordinates coordinates, Direction direction, Integer distanceFromBomb, Explosive explosive){
        this(coordinates,direction,distanceFromBomb,explosive,true);
    }

    /**
     * Constructs a new explosion.
     *
     * @param coordinates           The starting coordinates of the explosion.
     * @param direction             The direction of the explosion.
     * @param distanceFromExplosive The distance from the bomb where the explosion was created.
     */
    public AbstractExplosion(Coordinates coordinates, Direction direction, Integer distanceFromExplosive, Explosive explosive, boolean canExpand) {
        super(coordinates);

        this.direction = direction;
        this.distanceFromExplosive = distanceFromExplosive;
        this.explosive = explosive;
        this.maxDistance = explosive.getMaxExplosionDistance();
        this.canExpand = canExpand;

        //on first (center) explosion
        if (distanceFromExplosive == 0) {
            List<Coordinates> desiredCoords = getAllCoordinates();
            for (Entity e: Bomberman.getMatch().getEntities())
                if (desiredCoords.stream().anyMatch(coord -> Coordinates.doesCollideWith(coord, e)))
                    interact(e);
        }

        if (getCanExpand())
            moveOrInteract(direction, getSize(), true);
    }

    @Override
    public int getDrawPriority() {
        return 21;
    }

    /**
     * Interacts with another entity in the game.
     *
     * @param e The entity to interact with.
     */
    @Override
    protected void doInteract(Entity e) {
        if (e instanceof BomberEntity || e instanceof Enemy || e instanceof DestroyableBlock) {
            attack(e);
        } else if (e instanceof Bomb) {
            ((Bomb) e).explode();
        }
    }

    /**
     * Returns the size of the explosion.
     *
     * @return The size of the explosion.
     */
    @Override
    public int getSize() {
        return SIZE;
    }

    /**
     * Sets the coordinates of the explosion and creates new explosions based on its distance from the bomb.
     *
     * @param coordinates The new coordinates of the explosion.
     */
    @Override
    public void move(Coordinates coordinates) {
        // Calculate the coordinates of the next top-left position based on the current direction and size.
        Coordinates nextTopLeftCoords = nextCoords(direction, getSize());

        try {
            // Get the constructor of the explosion class and specify its parameter types.
            Constructor<? extends AbstractExplosion> constructor = getExplosionClass().getConstructor(Coordinates.class, Direction.class, int.class, Explosive.class);

            // Create a new instance of the explosion using the constructor and provide the necessary arguments.
            constructor.newInstance(
                    nextTopLeftCoords,  // The calculated next top-left coordinates
                    direction,         // The current direction
                    distanceFromExplosive + 1,  // Increase the distance from the explosive by 1
                    getExplosive()     // The explosive object associated with this instance
            ).forceSpawn();  // Spawn the explosion with the specified parameters
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // Print the stack trace if any exceptions occur during the instantiation process.
            e.printStackTrace();
        }
    }

    /**
     * Returns the image of the explosion.
     *
     * @return The image of the explosion.
     */
    @Override
    public BufferedImage getImage() {
        if (distanceFromExplosive == 0) {
            String centralStateImage = String.format("%s_central%s.png", getBasePath(), getState());
            return loadImage(centralStateImage);
        }

        String lastStateSuffix = canExpand ? "" : "_last";
        String directionImageSuffix = String.format("_%s", direction.toString().toLowerCase());

        String flameImagePath = String.format("%s%s%s%s.png", getBasePath(), directionImageSuffix, lastStateSuffix, getState());
        return loadAndSetImage(flameImagePath);
    }

    public int getState() {
        if (explosionState == 0 && !appearing) {
            despawn();
            appearing = true;
            return 0;
        }

        if (explosionState == BOMB_STATES) appearing = false;

        int appearingConstant = !appearing ? -1 : 1;

        int prevState = explosionState;
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastRefresh >= getImageRefreshRate()) {
            explosionState += appearingConstant;
            lastRefresh = currentTime;
        }

        return prevState;
    }

    @Override
    public int getImageRefreshRate() {
        return 100;
    }

    @Override
    public boolean isObstacle(Entity e) {
        return (e == null) || getExplosive().isObstacleOfExplosion(e);
    }

    @Override
    public Set<Class<? extends Entity>> getObstacles() {
        return new HashSet<>(getExplosive().getExplosionObstacles());
    }

    @Override
    public Set<Class<? extends Entity>> getInteractionsEntities(){
        return new HashSet<>(getExplosive().getExplosionInteractionEntities());
    }

    public boolean getCanExpand() {
        if (distanceFromExplosive >= maxDistance) canExpand = false;
        return canExpand;
    }

    public void onObstacle(Coordinates coordinates){
        try {
            Constructor<? extends AbstractExplosion> constructor = getExplosionClass().getConstructor(Coordinates.class, Direction.class, int.class, Explosive.class, boolean.class);

            constructor.newInstance(
                    coordinates,
                    direction,
                    distanceFromExplosive + 1,
                    explosive,
                    false
            ).forceSpawn();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Explosive getExplosive() {
        return explosive;
    }

    protected abstract Class<? extends AbstractExplosion> getExplosionClass();

    @Override
    protected Set<Class<? extends Entity>> getBasePassiveInteractionEntities() {
        return new HashSet<>();
    }
}