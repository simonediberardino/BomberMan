package game.level.world2;

import game.entity.enemies.boss.Boss;
import game.entity.enemies.npcs.*;
import game.entity.models.Enemy;
import game.level.Level;
import game.level.world1.World1Level;

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
