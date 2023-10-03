package game.level.world1;

import game.entity.enemies.npcs.Helicopter;
import game.entity.enemies.npcs.YellowBall;
import game.entity.models.Enemy;
import game.level.Level;

public class World1Level3 extends World1Level {
    @Override
    public int getLevelId() {
        return 3;
    }

    @Override
    public int startEnemiesCount() {
        return 7;
    }

    @Override
    public Class<? extends Enemy>[] availableEnemies() {
        return new Class[]{
                YellowBall.class,
                Helicopter.class
        };
    }

    @Override
    public Class<? extends Level> getNextLevel() {
        return World1Level4.class;
    }

}
