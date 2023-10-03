package game.level.world2;

import game.entity.enemies.npcs.Eagle;
import game.entity.enemies.npcs.FastEnemy;
import game.entity.models.Enemy;
import game.level.Level;

public class World2Level1 extends World2Level {
    @Override
    public int getLevelId() {
        return 1;
    }

    @Override
    public int startEnemiesCount() {
        return 7;
    }

    @Override
    public Class<? extends Enemy>[] availableEnemies() {
        return new Class[]{
                Eagle.class,
                FastEnemy.class
        };
    }

    @Override
    public Class<? extends Level> getNextLevel() {
        return World2Level2.class;
    }
}
