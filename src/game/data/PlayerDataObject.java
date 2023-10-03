package game.data;

import game.level.Level;
import game.localization.Localization;

import java.awt.event.KeyEvent;
import java.io.Serializable;

import static game.data.DataInputOutput.START_LIVES;

class PlayerDataObject implements Serializable {
    private String name;
    private int lostGames;
    private int kills;
    private int deaths;
    private int rounds;
    private long points;
    private int lastLevelId;
    private int lastWorldId;
    private int explosionLength;
    private int obtainedBombs;
    private int lives;
    private int forwardKey;
    private int backKey;
    private int leftKey;
    private int rightKey;
    private int bombKey;
    private int volume;
    
    PlayerDataObject() {
        this(
                Localization.get(Localization.PLAYER),
                0,
                0,
                0,
                0,
                0,
                START_LIVES,
                0,
                1,
                1,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );

        resetKeys();
    }

    PlayerDataObject(String name, int lostGames, int kills, int deaths, int rounds, long points, int lastLevelId, int lastWorldId, int explosionLength,int maxBombs, int lives, int forwardKey, int backKey, int leftKey, int rightKey, int bombKey, int volume) {
        this.name = name;
        this.lostGames = lostGames;
        this.kills = kills;
        this.deaths = deaths;
        this.rounds = rounds;
        this.points = points;
        this.lastLevelId = lastLevelId;
        this.lastWorldId = lastWorldId;
        this.explosionLength = explosionLength;
        this.obtainedBombs = maxBombs;
        this.lives = lives;
        this.forwardKey = forwardKey;
        this.backKey = backKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.bombKey = bombKey;
        this.volume = volume;
    }

    void resetKeys() {
        this.forwardKey = KeyEvent.VK_W;
        this.backKey = KeyEvent.VK_S;
        this.leftKey = KeyEvent.VK_A;
        this.rightKey = KeyEvent.VK_D;
        this.bombKey = KeyEvent.VK_SPACE;
    }

    void setVolume(int volume){
        this.volume = volume;
    }
    
    int getVolume() {
        return volume;
    }
    
    int getForwardKey() {
        return forwardKey;
    }

    void setForwardKey(int forwardKey) {
        this.forwardKey = forwardKey;
    }

    int getBackKey() {
        return backKey;
    }

    void setBackKey(int backKey) {
        this.backKey = backKey;
    }

    int getLeftKey() {
        return leftKey;
    }

    void setLeftKey(int leftKey) {
        this.leftKey = leftKey;
    }

    int getRightKey() {
        return rightKey;
    }

    void setRightKey(int rightKey) {
        this.rightKey = rightKey;
    }

    int getBombKey() {
        return bombKey;
    }

    void setBombKey(int bombKey) {
        this.bombKey = bombKey;
    }

    String getName() {
        return name;
    }

    int getLives() {
        return lives;
    }

    void setLives(int lives) {
        this.lives = lives;
    }

    void setExplosionLength(int explosionLength){
        this.explosionLength = explosionLength;
    }

    int getExplosionLength() {
        return explosionLength;
    }

    void setObtainedBombs(int obtainedBombs
    ){
        this.obtainedBombs = obtainedBombs;
    }

    int getObtainedBombs(){
        return obtainedBombs;
    }

    int getLostGames() {
        return lostGames;
    }

    int getKills() {
        return kills;
    }

    int getDeaths() {
        return deaths;
    }

    long getPoints() {
        return points;
    }

    void setName(String name) {
        this.name = name;
    }

    void setLostGames(int lostGames) {
        this.lostGames = lostGames;
    }

    void setLastLevel(Level lastLevel) {
        this.lastLevelId = lastLevel.getLevelId();
        this.lastWorldId = lastLevel.getWorldId();
    }

    int getLastLevelId() {
        return lastLevelId;
    }

    int getLastWorldId() {
        return lastWorldId;
    }

    void setKills(int kills) {
        this.kills = kills;
    }

    void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    void setPoints(long points) {
        this.points = points;
    }

    int getRounds() {
        return rounds;
    }

    void setRounds(int rounds) {
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        return "PlayerDataObject{" +
                "name='" + name + '\'' +
                ", lostGames=" + lostGames +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", rounds=" + rounds +
                ", points=" + points +
                ", lastLevelId=" + lastLevelId +
                ", lastWorldId=" + lastWorldId +
                ", explosionLength=" + explosionLength +
                ", obtainedBombs=" + obtainedBombs +
                ", lives=" + lives +
                ", forwardKey=" + forwardKey +
                ", backKey=" + backKey +
                ", leftKey=" + leftKey +
                ", rightKey=" + rightKey +
                ", bombKey=" + bombKey +
                ", volume=" + volume +
                '}';
    }

    void setLastWorldId(int lastWorldId) {
        this.lastWorldId = lastWorldId;
    }

    void setLastLevelId(int i) {
        this.lastLevelId = i;
    }
}
