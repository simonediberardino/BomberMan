package game.level.world1;

import game.entity.enemies.npcs.Helicopter;
import game.entity.models.Enemy;
import game.level.Level;

public class World1Level2 extends World1Level {
    @Override
    public int getLevelId() {
        return 2;
    }

    @Override
    public int startEnemiesCount() {
        return 7;
    }

    @Override
    public Class<? extends Enemy>[] availableEnemies() {
        return new Class[]{
                Helicopter.class
        };
    }

    @Override
    public Class<? extends Level> getNextLevel() {
        return World1Level3.class;
    }
}
