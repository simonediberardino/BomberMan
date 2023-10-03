package game.level.world1;

import game.entity.enemies.npcs.YellowBall;
import game.entity.models.Enemy;
import game.level.Level;

public class World1Level1 extends World1Level {
    public static final int LEVEL_ID = 1;

    @Override
    public int getLevelId() {
        return LEVEL_ID;
    }

    @Override
    public int startEnemiesCount() {
        return 7;
    }

    @Override
    public Class<? extends Enemy>[] availableEnemies() {
        return new Class[]{
                YellowBall.class
        };
    }

    @Override
    public Class<? extends Level> getNextLevel() {
        return World1Level2.class;
    }
}
