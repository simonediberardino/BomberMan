package game.level.world2;

import game.entity.enemies.boss.Boss;
import game.entity.enemies.npcs.FlyingEnemy;
import game.entity.enemies.npcs.YellowBall;
import game.entity.enemies.npcs.Zombie;
import game.entity.models.Enemy;
import game.level.Level;
import game.level.world1.World1Level;

public class World2Level2 extends World2Level {
    public World2Level2() {
        super(2);
    }

    @Override
    public int startEnemiesCount() {
        return 15;
    }

    @Override
    public Class<? extends Enemy>[] availableEnemies() {
        return new Class[]{
                Zombie.class,
                FlyingEnemy.class
        };
    }

    @Override
    public Class<? extends Level> getNextLevel() {
        return World2Level3.class;
    }
}
