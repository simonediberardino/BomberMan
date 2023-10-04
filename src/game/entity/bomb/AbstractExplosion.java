package game.entity.bomb;

import game.Bomberman;
import game.entity.models.*;
import game.ui.panels.game.PitchPanel;
import game.values.DrawPriority;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static game.utils.Utility.loadImage;

/**
 * An abstract class for in-game explosions;
 */
public abstract class AbstractExplosion extends MovingEntity {
    public static final int SIZE = PitchPanel.COMMON_DIVISOR * 2;
    public static final int SPAWN_OFFSET = (PitchPanel.GRID_SIZE - SIZE) / 2;
    protected static final int BOMB_STATES = 3;
    public static int MAX_EXPLOSION_LENGTH = 5;
    // The distance from the bomb where the explosion was created.
    protected final int distanceFromExplosive;
    // The maximum distance from the bomb that the explosion can travel.
    protected final int maxDistance;
    // The direction of the explosion.
    protected final Direction direction;
    private final Explosive explosive;
    private final Entity owner;
    protected boolean canExpand;
    protected boolean appearing = true;
    protected int explosionState = 1;
    protected long lastRefresh = 0;

    public AbstractExplosion(Entity owner,
                             Coordinates coordinates,
                             Direction direction,
                             Explosive explosive) {
        this(owner, coordinates, direction, 0, explosive);
    }

    public AbstractExplosion(Entity owner,
                             Coordinates coordinates,
                             Direction direction,
                             Integer distanceFromBomb,
                             Explosive explosive) {
        this(owner, coordinates, direction, distanceFromBomb, explosive, true);
    }

    public AbstractExplosion(Entity owner,
                             Coordinates coordinates,
                             Direction direction,
                             Integer distanceFromExplosive,
                             Explosive explosive,
                             boolean canExpand
    ) {
        super(coordinates);

        this.owner = owner;
        this.direction = direction;
        this.distanceFromExplosive = distanceFromExplosive;
        this.explosive = explosive;
        this.maxDistance = explosive.getMaxExplosionDistance();
        this.canExpand = canExpand;

        //on first (center) explosion
        if (distanceFromExplosive == 0) {
            List<Coordinates> desiredCoords = getAllCoordinates();
            Bomberman.getMatch().getEntities()
                    .parallelStream()
                    .filter(e -> desiredCoords.stream().anyMatch(coord -> Coordinates.doesCollideWith(coord, e)))
                    .forEach(this::interact);
        }

        if (getCanExpand())
            expandBomb(direction, getSize());
    }


    protected abstract Class<? extends AbstractExplosion> getExplosionClass();

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.DRAW_PRIORITY_4;
    }

    /**
     * Interacts with another entity in the game.
     *
     * @param e The entity to interact with.
     */
    @Override
    protected void doInteract(Entity e) {
        e.onExplosion(this);
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
        Coordinates nextTopLeftCoords = Coordinates.nextCoords(getCoords(), direction, getSize());

        try {
            Constructor<? extends AbstractExplosion> constructor = getExplosionClass().getConstructor(
                    Entity.class,
                    Coordinates.class,
                    Direction.class,
                    int.class,
                    Explosive.class
            );

            constructor.newInstance(
                    owner,
                    nextTopLeftCoords,
                    direction,
                    distanceFromExplosive + 1,
                    getExplosive()
            ).explode();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Set<Class<? extends Entity>> getBasePassiveInteractionEntities() {
        return new HashSet<>();
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
    public Set<Class<? extends Entity>> getInteractionsEntities() {
        return new HashSet<>(getExplosive().getExplosionInteractionEntities());
    }

    @Override
    public BufferedImage getImage() {
        if (distanceFromExplosive == 0) {
            return loadImage(String.format("%s_central" + getState() + ".png", getBasePath()));
        }

        String isLast = canExpand ? "" : "_last";
        String imageFileName = String.format("_%s", direction.toString().toLowerCase());

        // Load and set the image of the flame.
        String imagePath = String.format("%s%s%s%s.png", getBasePath(), imageFileName, isLast, getState());
        return loadAndSetImage(imagePath);
    }

    public boolean getCanExpand() {
        if (distanceFromExplosive >= maxDistance)
            canExpand = false;

        return canExpand;
    }

    protected boolean shouldHideCenter() {
        return false;
    }

    public void onObstacle(Coordinates coordinates) {
        try {
            Constructor<? extends AbstractExplosion> constructor = getExplosionClass().getConstructor(
                    Entity.class,
                    Coordinates.class,
                    Direction.class,
                    int.class,
                    Explosive.class,
                    boolean.class
            );

            constructor.newInstance(
                    owner,
                    coordinates,
                    direction,
                    distanceFromExplosive + 1,
                    explosive,
                    false
            ).explode();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Explosive getExplosive() {
        return explosive;
    }

    public Entity getOwner() {
        return owner;
    }

    public int getState() {
        if (explosionState == 0 && !appearing) {
            despawn();
            appearing = true;
            return 0;
        }

        if (explosionState == BOMB_STATES)
            appearing = false;

        int appearingConstant = !appearing ? -1 : 1;

        int prevState = explosionState;
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastRefresh >= getImageRefreshRate()) {
            explosionState += appearingConstant;
            lastRefresh = currentTime;
        }

        return prevState;
    }

    private void expandBomb(Direction d, int stepSize) {
        moveOrInteract(d, stepSize, true);
    }

    public void explode() {
        spawn(true, false);
    }
}