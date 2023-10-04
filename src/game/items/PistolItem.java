package game.items;

import game.entity.blocks.DestroyableBlock;
import game.entity.blocks.HardBlock;
import game.entity.bomb.AbstractExplosion;
import game.entity.bomb.Bomb;
import game.entity.bomb.PistolExplosion;
import game.entity.models.Coordinates;
import game.entity.models.Enemy;
import game.entity.models.Entity;
import game.entity.models.Explosive;
import game.sound.AudioManager;
import game.sound.SoundModel;
import game.utils.Paths;
import game.utils.Utility;
import java.util.HashSet;
import java.util.Set;

import static game.entity.bomb.AbstractExplosion.SIZE;

public class PistolItem extends UsableItem implements Explosive {
    private int bullets = 5;

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
        if (Utility.timePassed(owner.getLastPlacedBombTime()) < Bomb.PLACE_INTERVAL) {
            return;
        }

        owner.setLastPlacedBombTime(System.currentTimeMillis());
        bullets--;

        AbstractExplosion explosion = new PistolExplosion(
                getOwner(),
                Coordinates.nextCoords(owner.getCoords(), owner.getCurrDirection(), SIZE),
                getOwner().getCurrDirection(),
                1,
                this
        );

        AudioManager.getInstance().play(SoundModel.EXPLOSION);
        explosion.explode();

        if (bullets == 0) {
            remove();
        }
    }

    @Override
    public String getImagePath() {
        return Paths.getItemsPath() + "/pistol.png";
    }

    @Override
    public int getCount() {
        return bullets;
    }
}
