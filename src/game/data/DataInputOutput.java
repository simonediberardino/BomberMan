package game.data;

import game.Bomberman;
import game.level.Level;
import game.utils.Paths;

import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;

/**
 * Class that stores and retrieves player data from a file using Serialization;
 */
public class DataInputOutput {
    public static int START_LIVES = 3;
    private PlayerDataObject playerDataObject;

    // Private instance of the class
    private static DataInputOutput instance;

    // Private constructor to prevent instantiation from other classes
    private DataInputOutput() {
        playerDataObject = getStoredPlayerData();
    }

    // public method to get the instance of the singleton class
    public static DataInputOutput getInstance() {
        // Lazy initialization: create the instance only if it doesn't exist
        if (instance == null) {
            instance = new DataInputOutput();
        }
        return instance;
    }

    public void retrieveData() {
        playerDataObject = getStoredPlayerData();
    }

    public void updateStoredPlayerData() {
        updateStoredPlayerData(playerDataObject);
    }

    public void updateStoredPlayerData(PlayerDataObject serObj) {
        try {
            // Creates a data file if still does not exist;
            Files.createDirectories(java.nio.file.Paths.get(Paths.getDataFolder()));

            File dataFile = new File(Paths.getPlayerDataPath());

            dataFile.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(dataFile, false);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PlayerDataObject getStoredPlayerData() {
        try {
            FileInputStream fileIn = new FileInputStream(Paths.getPlayerDataPath());
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            PlayerDataObject obj = (PlayerDataObject) objectIn.readObject();

            objectIn.close();
            return obj;
        } catch (Exception ex) {
            return new PlayerDataObject();
        }
    }

    public PlayerDataObject getPlayerDataObject() {
        return playerDataObject;
    }

    public void setUsername(String name){
        playerDataObject.setName(name.trim());
        updateStoredPlayerData();
    }

    public String getUsername() {
        return playerDataObject.getName();
    }

    public void increaseKills(){
        playerDataObject.setKills(playerDataObject.getKills()+1);
        updateStoredPlayerData();
    }

    public void increaseExplosionLength(){
        setExplosionLength(playerDataObject.getExplosionLength()+1);
    }

    public void resetExplosionLength(){
        setExplosionLength(1);
    }

    public void resetMaxBombs(){
        setObtainedBombs(1);
    }

    public void setExplosionLength(int newExplosionLength){
        playerDataObject.setExplosionLength(newExplosionLength);
        updateStoredPlayerData();
    }

    public int getExplosionLength(){
        return playerDataObject.getExplosionLength();
    }

    public int getObtainedBombs(){
        return playerDataObject.getObtainedBombs();
    }

    public void setObtainedBombs(int newMaxBombs){
        newMaxBombs = Math.max(0, newMaxBombs);
        playerDataObject.setObtainedBombs(newMaxBombs);
        updateStoredPlayerData();
    }

    public void increaseObtainedBombs(){
        setObtainedBombs(getObtainedBombs()+1);
    }

    public void increaseDeaths(){
        playerDataObject.setDeaths(playerDataObject.getDeaths()+1);
        updateStoredPlayerData();
    }

    public int getLives(){
        return playerDataObject.getLives();
    }

    public void resetLives(){
        setLives(START_LIVES);
    }

    public void increaseLives(){
        int nextLives = playerDataObject.getLives()+1;
        setLives(nextLives);
    }

    private void setLives(int nextLives) {
        var controllerLives = Bomberman.getMatch().getInventoryElementControllerLives();
        if(controllerLives != null)
            Bomberman.getMatch().getInventoryElementControllerLives().setNumItems(nextLives);
        playerDataObject.setLives(nextLives);
        playerDataObject.setLives(nextLives);
        updateStoredPlayerData();
    }

    public void decreaseLives() {
        int newLives = Math.max(playerDataObject.getLives() - 1, 0);
        if(newLives <= 0) {
            increaseLost();
            resetLevel();
            resetScore();
        }

        setLives(newLives);
    }

    public void resetLivesIfNecessary() {
        if(getLives() <= 0)
            setLives(START_LIVES);
    }

    public void resetLevel() {
        playerDataObject.setLastWorldId(1);
        playerDataObject.setLastLevelId(1);
        resetExplosionLength();
        resetMaxBombs();
        updateStoredPlayerData();
    }

    public long getScore() {
        return playerDataObject.getPoints();
    }

    public void resetScore() {
        setScore(0);
    }

    public void decreaseScore(int score){
        setScore((int) (playerDataObject.getPoints() - score));
    }

    public void increaseScore(int score){
        setScore((int) (playerDataObject.getPoints() + score));
    }

    private void setScore(int score) {
        score = Math.max(0, score);
        playerDataObject.setPoints(score);
        updateStoredPlayerData();
    }

    public void setName(String name) {
        playerDataObject.setName(name);
        updateStoredPlayerData();
    }

    public void setLastLevel(Level level) {
        if(playerDataObject.getLastWorldId() > level.getWorldId()) return;
        if(playerDataObject.getLastWorldId() == level.getWorldId() && playerDataObject.getLastLevelId() >= level.getLevelId()) return;
        playerDataObject.setLastLevel(level);
        updateStoredPlayerData();
    }

    public void increaseLost(){
        playerDataObject.setLostGames(playerDataObject.getLostGames() + 1);
        updateStoredPlayerData();
    }

    public void increaseRounds(){
        playerDataObject.setRounds(playerDataObject.getRounds() + 1);
        updateStoredPlayerData();
    }

    public String getLeftKeyChar() {
        return KeyEvent.getKeyText(playerDataObject.getLeftKey());
    }

    public String getForwardKeyChar() {
        return KeyEvent.getKeyText(playerDataObject.getForwardKey());
    }

    public String getBackKeyChar() {
        return KeyEvent.getKeyText(playerDataObject.getBackKey());
    }

    public String getRightKeyChar() {
        return KeyEvent.getKeyText(playerDataObject.getRightKey());
    }

    public String getBombKeyChar() {
        return KeyEvent.getKeyText(playerDataObject.getBombKey());
    }



    public int getLeftKey() {
        return playerDataObject.getLeftKey();
    }

    public int getForwardKey() {
        return playerDataObject.getForwardKey();
    }

    public int getBackKey() {
        return playerDataObject.getBackKey();
    }

    public int getRightKey() {
        return playerDataObject.getRightKey();
    }

    public int getBombKey() {
        return playerDataObject.getBombKey();
    }

    public void resetKeys() {
        playerDataObject.resetKeys();
    }

    public void setForwardKey(int forwardKey) {
        playerDataObject.setForwardKey(forwardKey);
        updateStoredPlayerData();
    }

    public void setBackKey(int backKey) {
        playerDataObject.setBackKey(backKey);
        updateStoredPlayerData();
    }

    public void setLeftKey(int leftKey) {
        playerDataObject.setLeftKey(leftKey);
        updateStoredPlayerData();
    }

    public void setRightKey(int rightKey) {
        playerDataObject.setRightKey(rightKey);
        updateStoredPlayerData();
    }

    public void setBombKey(int bombKey) {
        playerDataObject.setBombKey(bombKey);
        updateStoredPlayerData();
    }

    public void setVolume(int volume){
        playerDataObject.setVolume(volume);
        updateStoredPlayerData();
    }

    public int getVolume(){
        return playerDataObject.getVolume();
    }

    public int getLastLevelId() {
        return playerDataObject.getLastLevelId();
    }

    public int getLastWorldId() {
        return playerDataObject.getLastWorldId();
    }

    public int getDeaths() {
        return playerDataObject.getDeaths();
    }

    public int getKills() {
        return playerDataObject.getKills();
    }

    public int getRounds() {
        return playerDataObject.getRounds();
    }

    public int getLostGames() {
        return playerDataObject.getLostGames();
    }
}
