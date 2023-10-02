package game.entity.bomb;

import game.Bomberman;
import game.entity.Player;
import game.entity.blocks.DestroyableBlock;
import game.entity.models.BomberEntity;
import game.entity.models.Enemy;
import game.entity.models.Entity;

import java.util.Arrays;
import java.util.List;

public class AllyBomb extends Bomb{
    public AllyBomb(BomberEntity entity) {
        super(entity);
    }
    @Override
    public List<Class<? extends Entity>> getExplosionInteractionEntities() {
        return Arrays.asList(DestroyableBlock.class, Enemy.class, Player.class, Bomb.class);
    }
    @Override
    public int getMaxExplosionDistance() {
        return (getCaller() != null && (getCaller() instanceof BomberEntity)) ? ((BomberEntity)getCaller()).getCurrExplosionLength() : Bomberman.getMatch().getCurrentLevel().getExplosionLength();
    }
}
