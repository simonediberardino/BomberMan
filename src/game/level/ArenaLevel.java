package game.level;

import game.Bomberman;
import game.data.DataInputOutput;
import game.data.PlayerDataObject;
import game.entity.models.Enemy;
import game.events.RoundPassedGameEvent;
import game.events.ScoreGameEvent;
import game.localization.Localization;
import game.ui.viewelements.misc.ToastHandler;

import javax.swing.*;

import static game.localization.Localization.ARENA_DIED;
import static game.localization.Localization.YOU_DIED;

public abstract class ArenaLevel extends Level {
    private static int CURR_ROUND = 0;
    private final static int MIN_ENEMIES_COUNT = 3;
    private final static int MAX_ENEMIES_COUNT = 20;
    private final static int ARENA_ROUND_LOADING_TIMER = 5000;
    private int currentRound = 0;

    public abstract Class<? extends Enemy>[] getSpecialRoundEnemies();

    @Override
    public void start(JPanel jPanel) {
        super.start(jPanel);
        currentRound = 0;
    }

    @Override
    public void startLevel() {
        super.startLevel();
        currentRound++;
        CURR_ROUND = currentRound;
        new RoundPassedGameEvent().invoke(null);
    }

    @Override
    public boolean isArenaLevel() {
        return true;
    }

    @Override
    public void generateDestroyableBlock() {
        if(currentRound == 0) {
            spawnMisteryBox();
            super.generateDestroyableBlock();
        }
    }

    @Override
    protected void spawnBoss() {
        if(shouldSpawnBoss())
            super.spawnBoss();
    }

    @Override
    public String getDiedMessage() {
        return Localization.get(ARENA_DIED).replace("%rounds%", Integer.toString(CURR_ROUND));
    }

    @Override
    public int getBossMaxHealth() {
        return super.getBossMaxHealth() / 4;
    }

    @Override
    public int startEnemiesCount() {
        return MIN_ENEMIES_COUNT + currentRound;
    }

    @Override
    public void onAllEnemiesEliminated() {
        new RoundPassedGameEvent().invoke(null);

        Timer t = new Timer(ARENA_ROUND_LOADING_TIMER, e -> startLevel());

        t.setRepeats(false);
        t.start();
    }

    @Override
    public void onRoundPassedGameEvent() {
        if(currentRound > 1)
            super.onRoundPassedGameEvent();

        Bomberman.getMatch().getInventoryElementControllerRounds().setNumItems(currentRound);
    }

    @Override
    protected void spawnEnemies() {
        if(isSpecialRound())
            spawnEnemies(getSpecialRoundEnemies());
        else super.spawnEnemies();
    }

    @Override
    public void onDeathGameEvent() {
        DataInputOutput.increaseDeaths();
        DataInputOutput.decreaseScore(1000);
    }

    protected boolean isSpecialRound() {
        return currentRound % 5 == 0 && currentRound > 1 && !shouldSpawnBoss();
    }

    protected boolean shouldSpawnBoss() {
        return currentRound % 10 == 0 && currentRound > 1;
    }
}
