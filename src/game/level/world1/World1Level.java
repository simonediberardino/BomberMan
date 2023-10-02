package game.level.world1;

import game.entity.enemies.boss.Boss;
import game.entity.models.Enemy;
import game.level.Level;
import game.level.StoryLevel;

public abstract class World1Level extends StoryLevel {
    @Override
    public int getWorldId() {
        return 1;
    }

    @Override
    public Boss getBoss() {
        return null;
    }

    @Override
    public final int getMaxDestroyableBlocks() {
        return 10;
    }


}
