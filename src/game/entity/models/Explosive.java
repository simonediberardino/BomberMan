package game.entity.models;

import java.util.List;
import java.util.Set;

public interface Explosive {
    Set<Class<? extends Entity>> getExplosionObstacles();
    boolean isObstacleOfExplosion(Entity e);
    List<Class<? extends Entity>> getExplosionInteractionEntities();
    int getMaxExplosionDistance();
}
