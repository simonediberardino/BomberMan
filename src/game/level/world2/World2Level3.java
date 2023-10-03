package game.level.world2;

import game.entity.enemies.npcs.FastEnemy;
import game.entity.enemies.npcs.TankEnemy;
import game.entity.models.Enemy;
import game.level.Level;

public class World2Level3 extends World2Level {
    @Override
    public int getLevelId() {
        return 3;
    }

    @Override
    public int startEnemiesCount() {
        return 13;
    }

    @Override
    public Class<? extends Enemy>[] availableEnemies() {
        return new Class[]{
                TankEnemy.class,
                FastEnemy.class,
        };
    }

    @Override
    public Class<? extends Level> getNextLevel() {
        return World2Level4.class;
    }
}
