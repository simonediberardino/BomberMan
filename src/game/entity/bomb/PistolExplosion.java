package game.entity.bomb;

import game.entity.models.*;
import game.utils.Paths;
import game.values.DrawPriority;

import java.util.Set;

public class PistolExplosion extends AbstractExplosion {
    public PistolExplosion(
            Entity owner,
            Coordinates coordinates,
            Direction direction,
            Explosive explosive) {
        super(owner, coordinates, direction,0, explosive);
    }

    public PistolExplosion(
            Entity owner,
            Coordinates coordinates,
            Direction direction,
            int distanceFromBomb,
            Explosive explosive) {
        super(owner, coordinates, direction, distanceFromBomb, explosive);
    }

    public PistolExplosion(
            Entity owner,
            Coordinates coordinates,
            Direction direction,
            int distanceFromExplosive,
            Explosive explosive,
            boolean canExpand) {
        super(owner, coordinates, direction, distanceFromExplosive, explosive, canExpand);
    }

    @Override
    protected String getBasePath() {
        return String.format("%s/bomb/flame", Paths.getEntitiesFolder());
    }

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.DRAW_PRIORITY_1;
    }

    @Override
    protected Class<? extends AbstractExplosion> getExplosionClass() {
        return getClass();
    }

    @Override
    protected boolean shouldHideCenter() {
        return true;
    }
}