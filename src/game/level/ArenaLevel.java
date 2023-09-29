package game.level;

import game.data.DataInputOutput;
import game.events.RoundArenaPassedGameEvent;
import game.events.RoundPassedGameEvent;
import game.localization.Localization;
import game.ui.viewelements.misc.ToastHandler;

import javax.swing.*;

import static game.localization.Localization.WELCOME_TEXT;

public abstract class ArenaLevel extends Level {
    private final static int MIN_ENEMIES_COUNT = 3;
    private final static int MAX_ENEMIES_COUNT = 20;
    private final static int ARENA_ROUND_LOADING_TIMER = 5000;
    private int currentRound = 1;

    protected boolean shouldSpawnBoss() {
        return currentRound % 5 == 0;
    }

    @Override
    public void startLevel() {
        super.startLevel();
        currentRound++;
        new RoundArenaPassedGameEvent().invoke(currentRound);
    }


    @Override
    public void generateDestroyableBlock() {
        if(currentRound == 1)
            super.generateDestroyableBlock();
    }

    @Override
    protected void spawnBoss() {
        if(shouldSpawnBoss())
            super.spawnBoss();
    }

    @Override
    public int startEnemiesCount() {
/*        int maxEnemies = shouldSpawnBoss() ? MAX_ENEMIES_COUNT / 2 : MAX_ENEMIES_COUNT;
        return (int) Math.max(currentRound * 1.5 + MIN_ENEMIES_COUNT, maxEnemies);*/
        return MIN_ENEMIES_COUNT + currentRound;
    }

    @Override
    public void onAllEnemiesEliminated() {
        new RoundArenaPassedGameEvent().invoke(currentRound);

        Timer t = new Timer(ARENA_ROUND_LOADING_TIMER, e -> startLevel());

        t.setRepeats(false);
        t.start();
    }
}
