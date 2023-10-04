package game.entity.enemies.boss.clown;

import game.Bomberman;
import game.entity.Player;
import game.entity.blocks.DestroyableBlock;
import game.entity.bomb.Bomb;
import game.entity.bomb.ConfettiExplosion;
import game.entity.enemies.boss.Boss;
import game.entity.enemies.npcs.ClownNose;
import game.entity.enemies.npcs.Orb;
import game.entity.models.*;
import game.entity.models.Character;
import game.sound.AudioManager;
import game.sound.SoundModel;
import game.ui.panels.game.PitchPanel;
import game.utils.Paths;
import game.utils.Utility;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * The Clown class represents a type of Boss entity that implements the Explosive interface.
 * It has a boolean property hasHat that determines whether the Clown is wearing a hat or not.
 * The Clown entity can spawn orbs, enhanced orbs, explosions and throw its hat in random directions.
 */
public class Clown extends Boss implements Explosive {
    private static final float RATIO_HEIGHT_WITH_HAT = 0.7517f;
    private static final float RATIO_HEIGHT = 0.87f;
    private static final float RATIO_WIDTH = 0.8739f;
    private static final String SKIN_PATH_TEMPLATE = "%s/clown/clown_%s_%s.png";
    private final ArrayList<ConfettiExplosion> explosions = new ArrayList<>();
    /**
     * The hasHat property represents whether the Clown entity is wearing a hat or not.
     */
    private boolean hasHat;

    /**
     * Constructor for the Clown entity that takes in the entity's starting coordinates and sets its initial hasHat value to true.
     *
     * @param coordinates The starting coordinates of the Clown entity.
     */
    private Clown(Coordinates coordinates) {
        super(coordinates);
    }

    public Clown() {
        super(null);
        hitboxSizetoWidthRatio = RATIO_WIDTH;
        hasHat = true;

        Dimension panelSize = Bomberman
                .getBombermanFrame()
                .getPitchPanel()
                .getPreferredSize();

        int y = (int) panelSize.getHeight() - getSize();
        int x = (int) (panelSize.getWidth() / 2 - getSize() / 2);

        setCoords(new Coordinates(x, y));
    }

    /**
     * Overrides the getBaseSkins method from the Boss superclass to return an array of skin paths based on the hasHat value.
     *
     * @return A String array of skin paths for the Clown entity.
     */
    @Override
    public String[] getCharacterOrientedImages() {
        return new String[]{getImageFromRageStatus()};
    }

    /**
     * @return A boolean value representing whether the Clown entity is wearing a hat or not.
     */
    public boolean isHatImage(String path) {
        String[] toks = path.split("_");
        if (toks.length <= 1) return false;

        return toks[1].equals("1");
    }

    @Override
    public float getHitboxSizeToHeightRatio(String path) {
        hitboxSizeToHeightRatio = isHatImage(path) ? RATIO_HEIGHT_WITH_HAT : RATIO_HEIGHT;
        return hitboxSizeToHeightRatio;
    }


    @Override
    public int calculateAndGetPaddingTop(double ratio) {

        return super.calculateAndGetPaddingTop(ratio);
    }

    /**
     * Setter method for the hasHat property.
     *
     * @param hasHat A boolean value representing whether the Clown entity is wearing a hat or not.
     */
    public void setHasHat(boolean hasHat) {
        this.hasHat = hasHat;
    }

    /**
     * Overrides the getExplosionObstacles method from the Explosive interface to return an empty list.
     *
     * @return An empty List object.
     */
    @Override
    public Set<Class<? extends Entity>> getExplosionObstacles() {
        return Collections.emptySet();
    }

    /**
     * Returns a list of entity classes that can interact with explosions.
     *
     * @return a list of entity classes that can interact with explosions.
     */
    @Override
    public Set<Class<? extends Entity>> getExplosionInteractionEntities() {
        return new HashSet<>() {{
            add(Player.class);
            add(Bomb.class);
        }};
    }

    /**
     * Returns the maximum distance of an explosion from this entity.
     *
     * @return the maximum distance of an explosion from this entity.
     */
    @Override
    public int getMaxExplosionDistance() {
        return 10;
    }

    /**
     * Returns the chance of this entity shooting a projectile.
     *
     * @return the chance of this entity shooting a projectile.
     */
    private int getShootingChance() {
        return 1;
    }

    /**
     * Spawns orbs in all directions around this entity.
     */
    private void spawnOrbs() {
        for (Direction d : Direction.values()) {
            new ClownNose(
                    Coordinates.fromDirectionToCoordinateOnEntity(
                            this,
                            d,
                            Orb.SIZE,
                            Orb.SIZE
                    ), d
            ).spawn(true, false);
        }
    }

    /**
     * Spawns enhanced orbs in all enhanced directions around this entity.
     */
    private void spawnEnhancedOrbs() {
        for (EnhancedDirection d : EnhancedDirection.values()) {
            new ClownNose(Coordinates.fromDirectionToCoordinateOnEntity(
                    this,
                    d,
                    Orb.SIZE
            ), d).spawn(true, false);
        }
    }

    /**
     * Calculates the explosion offsets based on the given direction.
     *
     * @param d the direction
     * @return the explosion offsets as an int array
     */
    private int[] calculateExplosionOffsets(Direction d) {
        int inwardOffset = getSize() / 4;
        int parallelOffset = -ConfettiExplosion.SIZE / 2;

        switch (d) {
            case RIGHT:
            case LEFT:
                parallelOffset = 0;
                inwardOffset = getSize() / 3 - PitchPanel.GRID_SIZE / 2;
                break;
        }

        return new int[]{inwardOffset, parallelOffset};
    }

    /**
     * Spawns an explosion in a random direction.
     */
    private void spawnExplosion() {
        Direction[] dirs = Direction.values();

        LinkedList<Direction> directions = new LinkedList<>(Arrays.asList(dirs));
        directions.remove(Direction.DOWN);

        Direction d = directions.get((int) (Math.random() * directions.size()));

        int[] offsets = calculateExplosionOffsets(d);
        AudioManager.getInstance().play(SoundModel.EXPLOSION_CONFETTI);

        Coordinates explosionCoordinates = Coordinates.fromDirectionToCoordinateOnEntity(
                this,
                d,
                offsets[0],
                offsets[1],
                ConfettiExplosion.SIZE
        );

        new ConfettiExplosion(
                this,
                explosionCoordinates,
                d,
                this
        );
    }

    /**
     * Throws a hat in a random enhanced direction.
     */
    public void throwHat() {
        EnhancedDirection d = EnhancedDirection.randomDirectionTowardsCenter(this);
        Entity hat = new Hat(
                Coordinates.fromDirectionToCoordinateOnEntity(
                        this,
                        d,
                        0),
                d
        );

        hat.spawn(true, false);
        hasHat = false;
    }

    /**
     * Updates this entity's state.
     *
     * @param gamestate the current gamestate
     */
    public void doUpdate(boolean gamestate) {
        // Check if the entity should shoot orbs
        Utility.runPercentage(getShootingChance(), () -> {
            spawnEnhancedOrbs();
            spawnOrbs();
        });

        // Check if the entity should shoot an explosion
        Utility.runPercentage(getShootingChance(), this::spawnExplosion);

        if (hasHat) {
            Utility.runPercentage(getShootingChance(), this::throwHat);
        }

        super.doUpdate(gamestate);
    }

    /**
     * Returns the image path of the Boss corresponding to its current rage status.
     *
     * @return the image path of the Boss.
     */
    @Override
    protected String getImageFromRageStatus() {
        // Format the skin path template with the appropriate values.
        return String.format(SKIN_PATH_TEMPLATE, Paths.getEnemiesFolder(), hasHat ? 1 : 0, currRageStatus);
    }

    /**
     * Returns a list with the supported directions of the Boss.
     *
     * @return the list with the supported directions.
     */
    @Override
    public List<Direction> getSupportedDirections() {
        return Arrays.asList(Direction.LEFT, Direction.RIGHT);
    }

    /**
     * Handles the Boss getting hit by the player, updating its rage status if necessary.
     *
     * @param damage the amount of damage the Boss received.
     */
    @Override
    protected void onHit(int damage) {
        // Get the current health percentage of the Boss.
        int hpPercentage = getHpPercentage();
        // Get the entry from the health status map whose key is the lowest greater than or equal to hpPercentage.
        Map.Entry<Integer, Integer> entry = healthStatusMap.ceilingEntry(hpPercentage);

        // If there is an entry, update the rage status of the Boss.
        if (entry != null) {
            updateRageStatus(entry.getValue());
        }
    }

    /**
     * Returns a map with the health percentages and their corresponding rage statuses for the Boss.
     * Note: avoid calling this method to prevent unnecessary memory usage, use the property instead.
     *
     * @return the health status map.
     */
    @Override
    protected Map<Integer, Integer> healthStatusMap() {
        // Create a new TreeMap with reverse order.
        TreeMap<Integer, Integer> map = new TreeMap<>(Collections.reverseOrder());
        // Add the health percentages and their corresponding rage statuses to the map.
        map.put(75, 0);
        map.put(60, 1);
        map.put(50, 2);
        map.put(25, 3);
        return map;
    }

    /**
     * Returns the top padding of the Boss hitbox.
     *
     * @return the top padding of the hitbox.
     */


    /**
     * Returns the height to hitbox size ratio of the Boss.
     *
     * @return the height to hitbox size ratio.
     */
    @Override
    public float getHitboxSizeToHeightRatio() {
        // Set the height to hitbox size ratio based on whether the Boss has a hat or not.
        hitboxSizeToHeightRatio = hasHat ? RATIO_HEIGHT_WITH_HAT : RATIO_HEIGHT;
        return hitboxSizeToHeightRatio;
    }


    @Override
    protected Set<Class<? extends Entity>> getBasePassiveInteractionEntities() {
        List<Class<? extends Entity>> list = new ArrayList<>(super.getBasePassiveInteractionEntities());
        list.add(Hat.class);
        return new HashSet<>(list);
    }
}

