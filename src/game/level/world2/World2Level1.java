package game.level.world2;

import game.entity.enemies.boss.Boss;
import game.entity.enemies.npcs.*;
import game.entity.models.Enemy;
import game.level.Level;
import game.level.world1.World1Level2;

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
