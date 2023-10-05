package game;

import game.data.DataInputOutput;
import game.data.SortedLinkedList;
import game.entity.Player;
import game.entity.models.BomberEntity;
import game.entity.models.Entity;
import game.hardwareinput.ControllerManager;
import game.hardwareinput.MouseControllerManager;
import game.items.BombItem;
import game.items.UsableItem;
import game.level.Level;
import game.tasks.GamePausedObserver;
import game.tasks.GameTickerObservable;
import game.ui.panels.game.MatchPanel;
import game.ui.panels.menus.PausePanel;
import game.utils.Utility;
import game.viewcontrollers.*;

import java.util.LinkedList;
import java.util.List;

public class BomberManMatch {
    private final SortedLinkedList<Entity> entities;
    private final MouseControllerManager mouseControllerManager;
    private InventoryElementController inventoryElementControllerPoints;
    private InventoryElementController inventoryElementControllerBombs;
    private InventoryElementController inventoryElementControllerLives;
    private InventoryElementController inventoryElementControllerRounds;
    private GameTickerObservable gameTickerObservable;
    private long lastGamePauseStateTime = System.currentTimeMillis();
    private ControllerManager controllerManager;
    private Level currentLevel;
    private Player player;
    private boolean gameState = false;
    private int enemiesAlive = 0;

    private BomberManMatch() {
        this(null);
    }

    public BomberManMatch(Level currentLevel) {
        this.currentLevel = currentLevel;
        this.entities = new SortedLinkedList<>();

        this.controllerManager = new ControllerManager();
        this.mouseControllerManager = new MouseControllerManager();
        this.gameTickerObservable = new GameTickerObservable();
        this.controllerManager.register(new GamePausedObserver());
        this.setupViewControllers();
        ControllerManager.setDefaultCommandDelay();
    }

    public void assignPlayerToControllerManager() {
        this.controllerManager.setPlayer(getPlayer());
    }

    private void setupViewControllers() {
        inventoryElementControllerPoints = new InventoryElementControllerPoints();
        inventoryElementControllerBombs = new InventoryElementControllerBombs();

        if (currentLevel.isArenaLevel()) {
            inventoryElementControllerRounds = new InventoryElementControllerRounds();
        } else {
            inventoryElementControllerLives = new InventoryElementControllerLives();
            inventoryElementControllerLives.setNumItems(DataInputOutput.getInstance().getLives());
        }

        inventoryElementControllerPoints.setNumItems((int) DataInputOutput.getInstance().getScore());
        updateInventoryWeaponController();
    }

    public void give(BomberEntity owner, UsableItem item) {
        owner.setWeapon(item);
        owner.getWeapon().setOwner(owner);
        updateInventoryWeaponController();
    }

    public void removeItem(BomberEntity owner) {
        owner.setWeapon(new BombItem());
        owner.getWeapon().setOwner(owner);
        updateInventoryWeaponController();
    }

    public void updateInventoryWeaponController() {
        if(player == null)
            return;

        inventoryElementControllerBombs.setImagePath(player.getWeapon().getImagePath());
        inventoryElementControllerBombs.setNumItems(player.getWeapon().getCount());
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<? extends Entity> getEntities() {
        synchronized (entities) {
            return new LinkedList<>(entities);
        }
    }


    public void addEntity(Entity entity) {
        synchronized (entities) {
            entities.add(entity);
        }
    }

    public void removeEntity(Entity e) {
        synchronized (entities) {
            entities.removeIf(e1 -> e.getId() == e1.getId());
        }
    }

    public ControllerManager getControllerManager() {
        return controllerManager;
    }

    public MouseControllerManager getMouseControllerManager() {
        return mouseControllerManager;
    }

    public GameTickerObservable getGameTickerObservable() {
        return gameTickerObservable;
    }

    public void toggleGameState() {
        if (Utility.timePassed(lastGamePauseStateTime) < 500) return;

        lastGamePauseStateTime = System.currentTimeMillis();

        if (gameState) pauseGame();
        else resumeGame();
    }

    private void pauseGame() {
        gameTickerObservable.stop();
        gameState = false;
        Bomberman.showActivity(PausePanel.class);
    }

    private void resumeGame() {
        gameTickerObservable.resume();
        gameState = true;
        Bomberman.showActivity(MatchPanel.class);
        currentLevel.playSoundTrack();
    }

    public int getEnemiesAlive() {
        return enemiesAlive;
    }

    public void decreaseEnemiesAlive() {
        enemiesAlive--;
    }

    public void increaseEnemiesAlive() {
        enemiesAlive++;
    }

    public boolean getGameState() {
        return gameState;
    }

    public void setGameState(boolean gameState) {
        this.gameState = gameState;
    }

    public InventoryElementController getInventoryElementControllerPoints() {
        return inventoryElementControllerPoints;
    }

    public InventoryElementController getInventoryElementControllerBombs() {
        return inventoryElementControllerBombs;
    }

    public InventoryElementController getInventoryElementControllerLives() {
        return inventoryElementControllerLives;
    }

    public InventoryElementController getInventoryElementControllerRounds() {
        return inventoryElementControllerRounds;
    }

    public void destroy() {
        pauseGame();

        List<? extends Entity> list = getEntities();
        for (Entity e : list) {
            e.despawn();
        }

        Bomberman.getBombermanFrame().getPitchPanel().clearGraphicsCallback();

        if (this.currentLevel != null) {
            this.currentLevel.stopLevelSound();
        }


        this.player = null;
        this.currentLevel = null;
        this.entities.clear();
        this.enemiesAlive = 0;
        this.mouseControllerManager.stopPeriodicTask();
        this.gameTickerObservable.unregisterAll();
        this.gameTickerObservable = null;
        this.controllerManager.unregisterAll();
        this.controllerManager = null;

        System.gc();
    }
}
