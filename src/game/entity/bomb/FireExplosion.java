package game.entity.bomb;

import game.entity.models.Coordinates;
import game.entity.models.Direction;
import game.entity.models.Explosive;
import game.utils.Paths;

import java.awt.image.BufferedImage;

import static game.utils.Utility.loadImage;

public class FireExplosion extends AbstractExplosion{
    public FireExplosion(Coordinates coordinates, Direction direction, Explosive explosive) {
        super(coordinates, direction, explosive);
    }


    public FireExplosion(Coordinates coordinates, Direction direction, int distanceFromBomb, Explosive explosive) {
        super(coordinates, direction, distanceFromBomb, explosive);
    }

    public FireExplosion(Coordinates coordinates, Direction direction, int distanceFromExplosive, Explosive explosive, boolean canExpand) {
        super(coordinates, direction, distanceFromExplosive, explosive, canExpand);
    }

    @Override
    protected String getBasePath() {
        return String.format("%s/bomb/flame", Paths.getEntitiesFolder());
    }

    @Override
    protected Class<? extends AbstractExplosion> getExplosionClass() {
        return getClass();
    }
}
