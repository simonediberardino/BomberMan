package game;

import game.data.DataInputOutput;
import game.data.SortedLinkedList;
import game.entity.Player;
import game.entity.bomb.Bomb;
import game.entity.models.BomberEntity;
import game.entity.models.Entity;
import game.hardwareinput.ControllerManager;
import game.hardwareinput.MouseControllerManager;
import game.items.BombItem;
import game.items.UsableItem;
import game.level.Level;
import game.level.actorbehavior.PlayLevelSoundTrackBehavior;
import game.level.online.ClientGameHandler;
import game.level.online.ServerGameHandler;
import game.tasks.GamePausedObserver;
import game.tasks.GameTickerObservable;
import game.ui.panels.game.MatchPanel;
import game.ui.pages.PausePanel;
import game.utils.Utility;
import game.viewcontrollers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BomberManMatch {
    private final SortedLinkedList<Entity> entities;
    private final MouseControllerManager mouseControllerManager;
    private ClientGameHandler clientGameHandler;
    private ServerGameHandler serverGameHandler;
    private InventoryElementController inventoryElementControllerPoints;
    private InventoryElementController inventoryElementControllerBombs;
    private InventoryElementController inventoryElementControllerLives;
    private InventoryElementController inventoryElementControllerRounds;
    private GameTickerObservable gameTickerObservable;
    private long lastGamePauseStateTime = System.currentTimeMillis();
    private ControllerManager controllerManager;
    private Level currentLevel;
    private Player player;
    private final ArrayList<Bomb> bombs = new ArrayList<>();
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
        ControllerManager.Companion.setDefaultCommandDelay();
    }

    public void assignPlayerToControllerManager() {
        this.controllerManager.setPlayer(getPlayer());
    }

    private void setupViewControllers() {
        inventoryElementControllerPoints = new InventoryElementControllerPoints();
        inventoryElementControllerBombs = new InventoryElementControllerBombs();

        if (currentLevel.getInfo().isArenaLevel()) {
            inventoryElementControllerRounds = new InventoryElementControllerRounds();
        } else {
            inventoryElementControllerLives = new InventoryElementControllerLives();
            inventoryElementControllerLives.setNumItems(DataInputOutput.getInstance().getLives());
        }

        inventoryElementControllerPoints.setNumItems((int) DataInputOutput.getInstance().getScore());
        updateInventoryWeaponController();
    }

    public void give(BomberEntity owner, UsableItem item, boolean combineSameItem) {
        if (combineSameItem && owner.getWeapon().getClass() == item.getClass()) {
            owner.getWeapon().combineItems(item);
        } else {
            owner.setWeapon(item);
            owner.getWeapon().owner = owner;
            updateInventoryWeaponController();
        }
    }

    public void give(BomberEntity owner, UsableItem item) {
        give(owner, item, false);
    }

    public void removeItem(BomberEntity owner) {
        owner.setWeapon(new BombItem());
        owner.getWeapon().owner = owner;
        updateInventoryWeaponController();
    }

    public void updateInventoryWeaponController() {
        if (player == null)
            return;

        inventoryElementControllerBombs.setImagePath(player.getWeapon().getImagePath());
        inventoryElementControllerBombs.setNumItems(player.getWeapon().getCount());
    }

    public ClientGameHandler getClientGameHandler() {
        return clientGameHandler;
    }

    public ServerGameHandler getServerGameHandler() {
        return serverGameHandler;
    }

    public boolean isClient() {
        return clientGameHandler != null && clientGameHandler.getConnected();
    }

    public boolean isServer() {
        return serverGameHandler != null && serverGameHandler.getRunning();
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

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
    }

    public void removeBomb(Bomb bomb) {
        bombs.remove(bomb);
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
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
        if (Utility.INSTANCE.timePassed(lastGamePauseStateTime) < 500)
            return;

        lastGamePauseStateTime = System.currentTimeMillis();

        if (gameState) pauseGame();
        else resumeGame();
    }

    private void pauseGame() {
        if (gameTickerObservable != null)
            gameTickerObservable.stop();

        gameState = false;
        Bomberman.showActivity(PausePanel.class);
    }

    private void resumeGame() {
        if (gameTickerObservable != null)
            gameTickerObservable.resume();

        gameState = true;
        Bomberman.showActivity(MatchPanel.class);

        if (currentLevel != null)
            new PlayLevelSoundTrackBehavior(currentLevel).invoke();
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

        if (this.currentLevel != null && this.currentLevel.getCurrentLevelSound() != null) {
            this.currentLevel.getCurrentLevelSound().stop();
        }

        this.player = null;
        this.currentLevel = null;
        this.enemiesAlive = 0;

        if (this.entities != null) {
            this.entities.clear();
        }

        if (this.mouseControllerManager != null) {
            this.mouseControllerManager.stopMovementTask();
        }

        if (this.gameTickerObservable != null) {
            this.gameTickerObservable.unregisterAll();
            this.gameTickerObservable = null;
        }

        if (this.controllerManager != null) {
            this.controllerManager.unregisterAll();
            this.controllerManager = null;
        }

        System.gc();
    }
}
