package game;

import game.data.SortedLinkedList;
import game.hardwareinput.ControllerManager;
import game.hardwareinput.MouseControllerManager;
import game.data.DataInputOutput;
import game.tasks.GamePausedObserver;
import game.tasks.GameTickerObservable;
import game.entity.*;
import game.entity.models.*;
import game.level.Level;
import game.ui.panels.game.MatchPanel;
import game.ui.panels.menus.PausePanel;
import game.viewcontrollers.InventoryElementController;
import game.utils.Paths;
import game.utils.Utility;

import java.util.*;

public class BomberManMatch {
    private InventoryElementController inventoryElementControllerPoints;
    private InventoryElementController inventoryElementControllerBombs;
    private InventoryElementController inventoryElementControllerLives;
    private GameTickerObservable gameTickerObservable;
    private long lastGamePauseStateTime = System.currentTimeMillis();
    private final SortedLinkedList<Entity> entities;
    public ControllerManager controllerManager;
    private final MouseControllerManager mouseControllerManager;
    private Level currentLevel;
    private Player player;
    private boolean gameState = false;
    private int enemiesAlive = 0;

    private BomberManMatch(){
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

    private void setupViewControllers() {
        inventoryElementControllerPoints = new InventoryElementController(0, Paths.getInventoryPath() + "/points.png");
        inventoryElementControllerLives = new InventoryElementController(0, Paths.getPowerUpsFolder()  + "/lives_up.png");
        inventoryElementControllerBombs = new InventoryElementController(0, Paths.getEntitiesFolder()  + "/bomb/bomb_0.png");

        inventoryElementControllerPoints.setNumItems((int) DataInputOutput.getScore());
        inventoryElementControllerLives.setNumItems(DataInputOutput.getLives());
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public List<?extends Entity> getEntities(){
        synchronized (entities) {
            return entities;
        }
    }

    public List<? extends Entity> getEntitiesCopy() {
        synchronized (entities) {
            return new LinkedList<>(entities);
        }
    }

    public void addEntity(Entity entity) {
        synchronized (entities){
            entities.add(entity);
        }
    }

    public void removeEntity(Entity e) {
        synchronized (entities){
            entities.removeIf(e1 -> e.getId() == e1.getId());
        }
    }

    public ControllerManager getControllerManager() {
        return controllerManager;
    }

    public MouseControllerManager getMouseControllerManager(){
        return mouseControllerManager;
    }

    public GameTickerObservable getGameTickerObservable() {
        return gameTickerObservable;
    }

    public void toggleGameState(){
        if(Utility.timePassed(lastGamePauseStateTime) < 500) return;

        lastGamePauseStateTime = System.currentTimeMillis();

        if(gameState) pauseGame(); else resumeGame();
    }

    private void pauseGame() {
        gameTickerObservable.stop();
        gameState = false;
        Bomberman.showActivity(PausePanel.class);
    }

    private void resumeGame(){
        gameTickerObservable.resume();
        gameState = true;
        Bomberman.showActivity(MatchPanel.class);
    }

    public int getEnemiesAlive() {
        return enemiesAlive;
    }

    public void decreaseEnemiesAlive(){
        enemiesAlive--;
    }

    public void increaseEnemiesAlive(){
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

    public void destroy() {
        pauseGame();

        List<? extends Entity> list = getEntities();
        for (Entity e: list) {
            e.despawn();
        }

        Bomberman.getBombermanFrame().getPitchPanel().clearGraphicsCallback();

        if(this.currentLevel != null) {
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
