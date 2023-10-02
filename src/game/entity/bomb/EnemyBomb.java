package game.entity.bomb;

import game.entity.Player;
import game.entity.blocks.DestroyableBlock;
import game.entity.models.BomberEntity;
import game.entity.models.Enemy;
import game.entity.models.Entity;

import java.util.Arrays;
import java.util.List;

public class EnemyBomb extends Bomb{
    public EnemyBomb(BomberEntity entity) {
        super(entity);
    }
    @Override
    public List<Class<? extends Entity>> getExplosionInteractionEntities() {
        return Arrays.asList(DestroyableBlock.class, Player.class, Bomb.class);
    }
}
