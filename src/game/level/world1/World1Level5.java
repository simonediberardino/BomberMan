package game.level.world1;

import game.entity.enemies.boss.Boss;
import game.entity.enemies.boss.ghost.GhostBoss;
import game.entity.enemies.npcs.Helicopter;
import game.entity.enemies.npcs.YellowBall;
import game.entity.enemies.npcs.Zombie;
import game.entity.models.Coordinates;
import game.entity.models.Enemy;
import game.level.Level;
import game.level.world2.World2Level1;
import game.ui.panels.game.PitchPanel;

import java.awt.*;

import static game.ui.panels.game.PitchPanel.GRID_SIZE;

public class World1Level5 extends World1Level {
    @Override
    public int getLevelId() {
        return 5;
    }


    @Override
    public Boss getBoss() {
        return new GhostBoss();
    }

    @Override
    public int startEnemiesCount() {
        return 0;
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
    public boolean isLastLevelOfWorld() {
        return true;
    }

    @Override
    public Class<? extends Level> getNextLevel() {
        return World2Level1.class;
    }

    @Override
    public Coordinates getPlayerSpawnCoordinates() {
        return Coordinates.fromRowAndColumnsToCoordinates(new Dimension(0, (int) PitchPanel.DIMENSION.getHeight() / GRID_SIZE - 1));
    }
}