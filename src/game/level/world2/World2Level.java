package game.level.world2;

import game.entity.enemies.boss.Boss;
import game.entity.enemies.npcs.FlyingEnemy;
import game.entity.enemies.npcs.TankEnemy;
import game.entity.enemies.npcs.YellowBall;
import game.entity.models.Enemy;
import game.level.Level;
import game.level.StoryLevel;

public abstract class World2Level extends StoryLevel {
    @Override
    public int getWorldId() {
        return 2;
    }

    @Override
    public final int getMaxDestroyableBlocks() {
        return 15;
    }

    @Override
    public Boss getBoss() {
        return null;
    }
}
