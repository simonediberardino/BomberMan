package game.level;

import game.Bomberman;
import game.data.DataInputOutput;
import game.entity.Player;
import game.entity.blocks.DestroyableBlock;
import game.entity.blocks.StoneBlock;
import game.entity.bonus.mysterybox.MysteryBoxPerk;
import game.entity.enemies.boss.Boss;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.entity.models.Enemy;
import game.entity.models.Entity;
import game.events.AllEnemiesEliminatedGameEvent;
import game.events.UpdateCurrentAvailableBombsEvent;
import game.level.world1.*;
import game.level.world2.*;
import game.powerups.PowerUp;
import game.powerups.portal.EndLevelPortal;
import game.sound.AudioManager;
import game.utils.Paths;
import game.utils.Utility;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static game.sound.SoundModel.BONUS_ALERT;
import static game.ui.panels.game.PitchPanel.GRID_SIZE;


/**
 * The abstract Level class represents the general structure and properties of a game level.
 * It includes methods that allow concrete implementations to define the type of blocks and
 * terrain that the level is composed of, as well as the length of the explosion that occurs
 * when bombs are detonated, and the background image of the level.
 */
public abstract class Level {
    public static final Map<Integer, Class<? extends Level>> ID_TO_FIRST_LEVEL_MAP = new HashMap<>() {{
        put(1, World1Level1.class);
        put(2, World2Level1.class);
    }};
    public static final Map<Integer[], Class<? extends Level>> ID_TO_LEVEL = new HashMap<>() {{
        put(new Integer[]{1, 1}, World1Level1.class);
        put(new Integer[]{1, 2}, World1Level2.class);
        put(new Integer[]{1, 3}, World1Level3.class);
        put(new Integer[]{1, 4}, World1Level4.class);
        put(new Integer[]{1, 5}, World1Level5.class);
        put(new Integer[]{2, 1}, World2Level1.class);
        put(new Integer[]{2, 2}, World2Level2.class);
        put(new Integer[]{2, 3}, World2Level3.class);
        put(new Integer[]{2, 4}, World2Level4.class);
        put(new Integer[]{2, 5}, World2Level5.class);
    }};
    private static Level currLevel;
    private Clip currentLevelSound;

    public static Level getCurrLevel() {
        return currLevel;
    }

    public abstract Boss getBoss();

    public int getBossMaxHealth() {
        return 1000;
    }

    public abstract int startEnemiesCount();

    public abstract int getMaxDestroyableBlocks();

    public abstract boolean isArenaLevel();

    public abstract String getDiedMessage();

    public abstract Class<? extends Level> getNextLevel();

    public abstract Class<? extends Enemy>[] availableEnemies();

    public void onAllEnemiesEliminated() {
    }

    public final String getLevelSoundtrack() {
        return getSoundForCurrentLevel("soundtrack.wav");
    }

    public final String getLevelBackgroundSound() {
        return getSoundForCurrentLevel("background_sound.wav");
    }

    public final int getExplosionLength() {
        return DataInputOutput.getInstance().getExplosionLength();
    }

    /**
     * Returns the path to the image file for the stone block.
     *
     * @return a string representing the path to the image file.
     */
    public String getStoneBlockImagePath() {
        return getImageForCurrentLevel("stone.png");
    }

    public String getPitchImagePath() {
        return getImageForCurrentLevel("pitch.png");
    }

    /**
     * Returns the path to the image file for the destroyable block.
     *
     * @return the path to the image file for the destroyable block.
     */
    public String getDestroyableBlockImagePath() {
        return getImageForCurrentLevel("destroyable_block.png");
    }

    public Image[] getBorderImages() {
        final int SIDES = 4;
        Image[] pitch = new Image[SIDES];
        for (int i = 0; i < SIDES; i++) {
            String path = getImageForCurrentLevel(String.format("border_%d.png", i));
            pitch[i] = Utility.loadImage(path);
        }

        return pitch;
    }

    private void updateLastLevel() {
        if (!(this instanceof WorldSelectorLevel))
            currLevel = this;
    }

    /**
     * Starts the game level by generating the terrain and adding the player character to the game panel.
     *
     * @param jPanel the panel on which to start the game level
     */
    public void start(JPanel jPanel) {
        updateLastLevel();
        playSoundTrack();
        Bomberman.getMatch().setGameState(true);
        DataInputOutput.getInstance().resetLivesIfNecessary();
        generateEntities(jPanel);
        playLevelSound();
    }

    public void playSoundTrack() {
        AudioManager.getInstance().stopBackgroundSong();
        AudioManager.getInstance().playBackgroundSong(getLevelSoundtrack());
    }

    public void generateEntities(JPanel jPanel) {
        generateStone(jPanel);
        generatePlayer();
        startLevel();
    }

    public void startLevel() {
        generateDestroyableBlock();
        spawnBoss();
        spawnEnemies();
    }

    protected Coordinates getPlayerSpawnCoordinates() {
        return Coordinates.generateRandomCoordinates(Player.SPAWN_OFFSET, GRID_SIZE);
    }

    protected void generatePlayer() {
        Coordinates coords = getPlayerSpawnCoordinates();
        Bomberman.getMatch().setPlayer(new Player(coords));
        Bomberman.getMatch().getPlayer().spawn(false, false);
    }

    abstract public void endLevel();

    protected void spawnBoss() {
        Boss boss = getBoss();
        if (boss != null) boss.spawn(true, false);
    }

    // This method spawns enemies in the game.
    protected void spawnEnemies() {
        // Get an array of available enemy classes.
        Class<? extends Enemy>[] availableEnemies = availableEnemies();
        spawnEnemies(availableEnemies);
    }

    protected final void spawnEnemies(Class<? extends Enemy>[] availableEnemies) {
        // Spawn a number of enemies at the start of the game.
        for (int i = 0; i < startEnemiesCount(); i++) {
            // Select a random enemy class from the availableEnemies array.
            Class<? extends Enemy> enemyClass = availableEnemies[new Random().nextInt(availableEnemies.length)];

            // Create an instance of the enemy class using a constructor that takes a Coordinates object as an argument.
            Enemy enemy;
            try {
                enemy = enemyClass.getConstructor().newInstance();

                // Spawn the enemy on the game board.
                enemy.spawn(false, false);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    // This method returns the ID of the world.
    public abstract int getWorldId();

    // This method returns the ID of the level.
    public abstract int getLevelId();

    public abstract void onDeathGameEvent();

    // This method returns the maximum number of bombs that a player can have at one time.
    public int getMaxBombs() {
        return Player.MAX_BOMB_CAN_HOLD;
    }

    /**
     * Generates the stone blocks in the game board for level 1.
     *
     * @param jPanel the JPanel where the stone blocks are to be placed.
     */
    public void generateStone(JPanel jPanel) {
        // Set the current x and y coordinates to the top-left corner of the game board.
        int currX = 0;
        int currY = GRID_SIZE;

        // Loop through the game board, adding stone blocks at every other grid position.
        while (currY < jPanel.getPreferredSize().getHeight() - GRID_SIZE) {
            while (currX < jPanel.getPreferredSize().getWidth() - GRID_SIZE && currX + GRID_SIZE * 2 <= jPanel.getPreferredSize().getWidth()) {
                // Move the current x coordinate to the next grid position.
                currX += GRID_SIZE;

                // Create a new stone block at the current coordinates and spawn it on the game board.
                new StoneBlock(new Coordinates(currX, currY)).spawn();

                // Move the current x coordinate to the next grid position.
                currX += GRID_SIZE;
            }
            // Move the current x coordinate back to the left side of the game board.
            currX = 0;

            // Move the current y coordinate down to the next row of grid positions.
            currY += GRID_SIZE * 2;
        }
    }

    public void playLevelSound() {
        String soundPath = getLevelBackgroundSound();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (soundPath == null) return;

        // Attempt to load the resource as an InputStream
        InputStream soundStream = classLoader.getResourceAsStream(soundPath);

        if (soundStream != null) {
            // If the resource exists, play it using AudioManager
            currentLevelSound = AudioManager.getInstance().play(soundPath, true);
        }
    }


    public void stopLevelSound() {
        if (currentLevelSound == null) {
            return;
        }
        currentLevelSound.stop();
    }

    public void despawnDestroyableBlocks() {
        Bomberman.getMatch()
                .getEntities()
                .stream()
                .filter(entity -> entity instanceof DestroyableBlock)
                .forEach(Entity::despawn);
    }

    public void spawnMisteryBox() {
        Coordinates c = Coordinates.generateCoordinatesAwayFrom(Bomberman.getMatch().getPlayer().getCoords(), GRID_SIZE * 2);
        Entity mysteryBox = new MysteryBoxPerk(Bomberman.getMatch().getPlayer());
        mysteryBox.setCoords(c);
        mysteryBox.spawn();
    }

    // This method generates destroyable blocks in the game board.
    public void generateDestroyableBlock() {
        // Despawn all the previous destroyable blocks;
        despawnDestroyableBlocks();

        DestroyableBlock block = new DestroyableBlock(new Coordinates(0, 0));

        // Initialize a counter for the number of destroyable blocks spawned.
        int i = 0;

        // Loop until the maximum number of destroyable blocks has been spawned.
        while (i < getMaxDestroyableBlocks()) {
            // If the current destroyable block has not been spawned, generate new coordinates for it and spawn it on the game board.
            if (!block.isSpawned()) {
                block.setCoords(Coordinates.generateCoordinatesAwayFrom(Bomberman.getMatch().getPlayer().getCoords(), GRID_SIZE * 2));
                block.spawn();

                // Force the first spawned block to have the End level portal
                if (i == 0 && !isLastLevelOfWorld() && !isArenaLevel()) {
                    block.setPowerUpClass(EndLevelPortal.class);
                } else {
                    block.setPowerUpClass(PowerUp.getRandomPowerUpClass());
                }
            }

            // If the current destroyable block has been spawned, create a new one and increment the spawn counter.
            else {
                block = new DestroyableBlock(new Coordinates(0, 0));
                i++;
            }
        }
    }

    protected String getImageForCurrentLevel(String path) {
        return getFileForCurrentLevel(String.format("images/%s", path));
    }

    protected String getSoundForCurrentLevel(String path) {
        return getFileForCurrentLevel(String.format("sound/%s", path));
    }

    /**
     * @return returns the path to the file: if a specific instance of the file exists for the current level, then return it, else return the current world instance;
     */
    protected String getFileForCurrentLevel(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // First, try to load the resource from the specific level folder
        InputStream specificLevelStream = classLoader.getResourceAsStream(Paths.getCurrentLevelFolder() + "/" + path);
        if (specificLevelStream != null) {
            return Paths.getCurrentLevelFolder() + "/" + path;
        }

        // If not found, try to load from the current world's common folder
        InputStream currentWorldStream = classLoader.getResourceAsStream(Paths.getCurrentWorldCommonFolder() + "/" + path);
        if (currentWorldStream != null) {
            return Paths.getCurrentWorldCommonFolder() + "/" + path;
        }

        // If still not found, load from the common folder in the JAR
        InputStream commonStream = classLoader.getResourceAsStream("common/" + path);
        if (commonStream != null) {
            return "common/" + path;
        }

        // File not found
        return Paths.getWorldsFolder() + "/common/" + path;
    }

    public boolean isLastLevelOfWorld() {
        return false;
    }

    public void onDefeatGameEvent() {
        DataInputOutput.getInstance().increaseLost();
    }

    public void onEnemyDespawned() {
        if (Bomberman.getMatch().getEnemiesAlive() == 0) {
            new AllEnemiesEliminatedGameEvent().invoke(null);
        }
    }

    public void onKilledEnemy() {
        DataInputOutput.getInstance().increaseKills();
    }

    public void onRoundPassedGameEvent() {
        DataInputOutput.getInstance().increaseRounds();
    }

    public void onScoreGameEvent(int arg) {
        DataInputOutput.getInstance().increaseScore(arg);
        Bomberman.getMatch().getInventoryElementControllerPoints().setNumItems((int) DataInputOutput.getInstance().getScore());
    }

    public void onPurchaseItem(int price) {
        AudioManager.getInstance().play(BONUS_ALERT);
        DataInputOutput.getInstance().decreaseScore(price);
        Bomberman.getMatch().getInventoryElementControllerPoints().setNumItems((int) DataInputOutput.getInstance().getScore());
    }

    public void onUpdateCurrentAvailableBombsEvent(int arg) {
        Bomberman.getMatch().getPlayer().setCurrentBombs(arg);
        Bomberman.getMatch().getInventoryElementControllerBombs().setNumItems(arg);
    }

    public void onUpdateMaxBombsGameEvent(int arg) {
        DataInputOutput.getInstance().increaseObtainedBombs();
        new UpdateCurrentAvailableBombsEvent().invoke(arg);
    }

    public void onUpdateBombsLengthEvent(BomberEntity entity, int arg) {
        entity.setCurrExplosionLength(arg);
        DataInputOutput.getInstance().setExplosionLength(arg);
    }

    @Override
    public String toString() {
        return String.format("Level %d, World %d", getLevelId(), getWorldId());
    }
}
