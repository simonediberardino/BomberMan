package game.entity.bomb;

import game.entity.models.Coordinates;
import game.entity.models.Direction;
import game.entity.models.Entity;
import game.entity.models.Explosive;
import game.utils.Paths;

public class ConfettiExplosion extends AbstractExplosion {
    public ConfettiExplosion(
            Entity owner,
            Coordinates coordinates,
            Direction direction,
            Explosive explosive) {
        super(owner, coordinates, direction, explosive);
    }

    public ConfettiExplosion(
            Entity owner,
            Coordinates coordinates,
            Direction direction,
            int distanceFromBomb,
            Explosive explosive) {
        super(owner, coordinates, direction, distanceFromBomb, explosive);
    }

    public ConfettiExplosion(
            Entity owner,
            Coordinates coordinates,
            Direction direction,
            int distanceFromExplosive,
            Explosive explosive,
            boolean canExpand) {
        super(owner, coordinates, direction, distanceFromExplosive, explosive, canExpand);
    }

    @Override
    protected Class<? extends AbstractExplosion> getExplosionClass() {
        return getClass();
    }

    @Override
    protected String getBasePath() {
        return String.format("%s/clown/clown_explosion/clown_explosion", Paths.getEnemiesFolder());
    }
}
