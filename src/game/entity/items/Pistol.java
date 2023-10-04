package game.entity.items;

import game.entity.blocks.DestroyableBlock;
import game.entity.blocks.HardBlock;
import game.entity.bomb.AbstractExplosion;
import game.entity.bomb.Bomb;
import game.entity.bomb.PistolExplosion;
import game.entity.models.Coordinates;
import game.entity.models.Enemy;
import game.entity.models.Entity;
import game.entity.models.Explosive;

import java.util.HashSet;
import java.util.Set;

import static game.entity.bomb.AbstractExplosion.SIZE;

public class Pistol extends UsableItem implements Explosive {
    public Set<Class<? extends Entity>> getExplosionObstacles() {
        return new HashSet<>() {{
            add(HardBlock.class);
            add(DestroyableBlock.class);
        }};
    }

    @Override
    public Set<Class<? extends Entity>> getExplosionInteractionEntities() {
        return new HashSet<>() {{
            add(Enemy.class);
            add(Bomb.class);
        }};
    }

    @Override
    public int getMaxExplosionDistance() {
        return 3;
    }

    @Override
    public void use() {
        AbstractExplosion explosion = new PistolExplosion(
                getOwner(),
                Coordinates.nextCoords(owner.getCoords(), owner.getCurrDirection(), SIZE),
                getOwner().getCurrDirection(),
                1,
                this
        );


        explosion.explode();
    }
}
