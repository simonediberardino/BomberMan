package game.level.world2;

import game.entity.enemies.boss.Boss;
import game.entity.enemies.boss.clown.Clown;
import game.entity.enemies.npcs.Eagle;
import game.entity.enemies.npcs.FastEnemy;
import game.entity.enemies.npcs.TankEnemy;
import game.entity.enemies.npcs.Zombie;
import game.entity.models.Enemy;
import game.level.ArenaLevel;
import game.level.Level;

public class World2Arena extends ArenaLevel {
    @Override
    public Class<? extends Enemy>[] getSpecialRoundEnemies() {
        return new Class[]{
                TankEnemy.class,
                Zombie.class
        };
    }

    @Override
    public Boss getBoss() {
        return new Clown();
    }

    @Override
    public int getMaxDestroyableBlocks() {
        return 10;
    }

    @Override
    public Class<? extends Level> getNextLevel() {
        return null;
    }

    @Override
    public Class<? extends Enemy>[] availableEnemies() {
        return new Class[]{
                FastEnemy.class,
                TankEnemy.class,
                Eagle.class
        };
    }

    @Override
    public int getWorldId() {
        return 2;
    }

    @Override
    public int getLevelId() {
        return 0;
    }
}
