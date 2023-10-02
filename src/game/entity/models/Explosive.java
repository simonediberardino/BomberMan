package game.entity.models;

import game.entity.bomb.ExplosiveCaller;

import java.util.List;

public interface Explosive {
    List<Class<? extends Entity>> getExplosionObstacles();
    boolean isObstacleOfExplosion(Entity e);
    List<Class<? extends Entity>> getExplosionInteractionEntities();
    int getMaxExplosionDistance();

}
