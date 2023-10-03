package game.level;

import game.Bomberman;
import game.data.DataInputOutput;
import game.entity.models.BomberEntity;
import game.entity.models.Enemy;
import game.events.RoundPassedGameEvent;
import game.events.UpdateCurrentAvailableBombsEvent;
import game.events.UpdateCurrentBombsLengthEvent;
import game.events.UpdateMaxBombsEvent;
import game.localization.Localization;
import game.ui.viewelements.misc.ToastHandler;

import javax.swing.*;

import java.util.concurrent.atomic.AtomicReference;

import static game.localization.Localization.ARENA_DIED;
import static game.localization.Localization.STARTING_ROUND;

public abstract class ArenaLevel extends Level {
    private final static int MIN_ENEMIES_COUNT = 3;
    private final static int MAX_ENEMIES_COUNT = 20;
    private final static int ARENA_ROUND_LOADING_TIMER = 5000;
    private static int CURR_ROUND = 0;
    private final AtomicReference<Integer> currentRound = new AtomicReference<>(0);

    public abstract Class<? extends Enemy>[] getSpecialRoundEnemies();

    @Override
    public void start(JPanel jPanel) {
        super.start(jPanel);
        if(currentRound.get() == 1) {
            firstStart();
        }
    }

    @Override
    public void startLevel() {
        super.startLevel();
        currentRound.set(currentRound.get() + 1);
        CURR_ROUND = currentRound.get();
        new RoundPassedGameEvent().invoke(null);
    }

    @Override
    public boolean isArenaLevel() {
        return true;
    }

    @Override
    public void generateDestroyableBlock() {
        if (currentRound.get() != 0) {
            return;
        }
        spawnMisteryBox();
        super.generateDestroyableBlock();
    }

    @Override
    protected void spawnBoss() {
        if (shouldSpawnBoss())
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
        return MIN_ENEMIES_COUNT + currentRound.get();
    }

    @Override
    public void onAllEnemiesEliminated() {
        Timer t = new Timer(ARENA_ROUND_LOADING_TIMER, e -> {
            if(Bomberman.getMatch().getPlayer().isSpawned())
                startLevel();
        });

        t.setRepeats(false);
        t.start();
    }

    @Override
    public void onRoundPassedGameEvent() {
        if (currentRound.get() > 1) {
            super.onRoundPassedGameEvent();
        }

        ToastHandler.getInstance().show(Localization.get(STARTING_ROUND).replace("%round%", String.valueOf(currentRound.get())));
        Bomberman.getMatch().getInventoryElementControllerRounds().setNumItems(currentRound.get());
    }

    @Override
    protected void spawnEnemies() {
        if (isSpecialRound())
            spawnEnemies(getSpecialRoundEnemies());
        else super.spawnEnemies();
    }

    @Override
    public void onDeathGameEvent() {
        currentRound.set(0);
        DataInputOutput.getInstance().increaseDeaths();
        DataInputOutput.getInstance().decreaseScore(1000);
    }

    @Override
    public void onUpdateMaxBombsGameEvent(int arg) {
        new UpdateCurrentAvailableBombsEvent().invoke(arg);
    }

    @Override
    public void onUpdateBombsLengthEvent(BomberEntity entity, int arg) {
        entity.setCurrExplosionLength(arg);
    }

    @Override
    public void endLevel() {}

    @Override
    public String toString() {
        return String.format("Arena World %d", getWorldId());
    }

    protected void firstStart() {
        new UpdateCurrentBombsLengthEvent().invoke(1);
        new UpdateMaxBombsEvent().invoke(1);
    }

    protected boolean isSpecialRound() {
        return currentRound.get() % 5 == 0 && currentRound.get() > 1 && !shouldSpawnBoss();
    }

    protected boolean shouldSpawnBoss() {
        return currentRound.get() % 10 == 0 && currentRound.get() > 1;
    }
}
