package game.entity.bomb;

import game.entity.Player;
import game.entity.blocks.DestroyableBlock;
import game.entity.models.BomberEntity;
import game.entity.models.Character;
import game.entity.models.Enemy;
import game.entity.models.Entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnemyBomb extends Bomb {
    public EnemyBomb(BomberEntity entity) {
        super(entity);
    }

    @Override
    public Set<Class<? extends Entity>> getExplosionInteractionEntities() {
        return new HashSet<>() {{
            add(DestroyableBlock.class);
            add(Player.class);
            add(Bomb.class);
        }};
    }
}
