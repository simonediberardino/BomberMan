package game.level.world1;

import game.entity.enemies.boss.Boss;
import game.entity.enemies.boss.ghost.GhostBoss;
import game.entity.enemies.npcs.Helicopter;
import game.entity.enemies.npcs.TankEnemy;
import game.entity.enemies.npcs.YellowBall;
import game.entity.enemies.npcs.Zombie;
import game.entity.models.Enemy;
import game.level.ArenaLevel;
import game.level.Level;

public class World1Arena extends ArenaLevel {
    @Override
    public Boss getBoss() {
        return new GhostBoss();
    }

    @Override
    public final int getMaxDestroyableBlocks() {
        return 10;
    }

    @Override
    public Class<? extends Level> getNextLevel() {
        return null;
    }

    @Override
    public Class<? extends Enemy>[] availableEnemies() {
        return new Class[]{
                YellowBall.class,
                Helicopter.class,
                Zombie.class
        };
    }

    @Override
    public int getWorldId() {
        return 1;
    }

    @Override
    public int getLevelId() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Arena World %d", getWorldId());
    }

    @Override
    public Class<? extends Enemy>[] getSpecialRoundEnemies() {
        return new Class[] {
                TankEnemy.class,
                Zombie.class
        };
    }
}
