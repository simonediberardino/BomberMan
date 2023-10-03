package game.level.world2;

import game.entity.Player;
import game.entity.enemies.boss.Boss;
import game.entity.enemies.boss.clown.Clown;
import game.entity.enemies.npcs.Eagle;
import game.entity.enemies.npcs.FastEnemy;
import game.entity.models.Coordinates;
import game.entity.models.Enemy;
import game.level.Level;
import game.level.WorldSelectorLevel;

public class World2Level5 extends World2Level {
    @Override
    public int getLevelId() {
        return 5;
    }

    @Override
    public Boss getBoss() {
        return new Clown();
    }

    @Override
    public int startEnemiesCount() {
        return 7;
    }

    @Override
    public Class<? extends Enemy>[] availableEnemies() {
        return new Class[]{
                FastEnemy.class,
                Eagle.class,
        };
    }

    @Override
    public boolean isLastLevelOfWorld() {
        return true;
    }

    @Override
    public Class<? extends Level> getNextLevel() {
        return WorldSelectorLevel.class;
    }

    @Override
    public Coordinates getPlayerSpawnCoordinates() {
        return Coordinates.roundCoordinates(new Coordinates(0, 0), Player.SPAWN_OFFSET);
    }
}