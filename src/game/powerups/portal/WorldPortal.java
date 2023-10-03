package game.powerups.portal;

import game.Bomberman;
import game.data.DataInputOutput;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.level.Level;
import game.level.world1.World1Level1;
import game.utils.Paths;
import game.utils.Utility;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

import static game.level.Level.ID_TO_FIRST_LEVEL_MAP;
import static game.level.Level.ID_TO_LEVEL;

public abstract class WorldPortal extends Portal {
    private final int worldId;

    public WorldPortal(int worldId) {
        this(null, worldId);
    }

    public WorldPortal(Coordinates coordinates, int worldId) {
        super(coordinates);
        this.worldId = worldId;
        setCoords(getDefaultCoords());
    }

    @Override
    public int getSize() {
        return super.getSize() * 3;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public BufferedImage getImage() {
        return Utility.loadImage(Paths.getWorldSelectorPortalPath(worldId));
    }

    @Override
    protected void doApply(BomberEntity entity) {
        super.doApply(entity);

        try {
            // Get the ID of the last level and last world from the player data object
            int savedLastLevelId = DataInputOutput.getInstance().getLastLevelId();
            int savedLastWorldId = DataInputOutput.getInstance().getLastWorldId();

            // Get the class of the first level for the current world from the ID_TO_FIRST_LEVEL_MAP
            Class<? extends Level> firstLevelOfCurrWorld = ID_TO_FIRST_LEVEL_MAP.getOrDefault(worldId, World1Level1.class);

            // Initialize the level to start
            Level levelToStart;

            // Check if the saved last world ID matches the current world ID
            if (savedLastWorldId == worldId) {
                // Find the class of the last level using the saved last world ID and last level ID
                Optional<? extends Class<? extends Level>> lastLevelOpt = findLastLevel(savedLastWorldId, savedLastLevelId);
                Class<? extends Level> levelClass = lastLevelOpt.isPresent() ? lastLevelOpt.get() : firstLevelOfCurrWorld;

                // Create an instance of the level class
                levelToStart = levelClass.getConstructor().newInstance();
            } else {
                // Use the class of the first level for the current world
                levelToStart = firstLevelOfCurrWorld.getConstructor().newInstance();
            }

            // Start the level with the obtained level instance
            Bomberman.startLevel(levelToStart);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            // Print the stack trace if there is an exception
            e.printStackTrace();
        }
    }

    /**
     * Finds the class of the last level based on the saved last world ID and last level ID.
     *
     * @param savedLastWorldId the saved last world ID
     * @param savedLastLevelId the saved last level ID
     * @return an optional containing the class of the last level, if found
     */
    private Optional<? extends Class<? extends Level>> findLastLevel(int savedLastWorldId, int savedLastLevelId) {
        return ID_TO_LEVEL.entrySet()
                .stream()
                .filter(e -> e.getKey()[0] == savedLastWorldId && e.getKey()[1] == savedLastLevelId)
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Override
    protected void cancel(BomberEntity entity) {

    }

    abstract Coordinates getDefaultCoords();
}
