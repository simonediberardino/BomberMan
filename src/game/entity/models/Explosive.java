package game.entity.models;

import java.util.List;
import java.util.Set;

public interface Explosive {
    Set<Class<? extends Entity>> getExplosionObstacles();

    default boolean isObstacleOfExplosion(Entity e) {
        return (e == null) || (getExplosionObstacles().stream().anyMatch(c -> c.isInstance(e)));
    }

    Set<Class<? extends Entity>> getExplosionInteractionEntities();

    int getMaxExplosionDistance();
}
