package game.powerups;

import game.entity.bomb.Bomb;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.utils.Paths;

import java.awt.image.BufferedImage;

public class TransparentBombsPowerUp extends PowerUp {
    public TransparentBombsPowerUp(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/transparent_bomb_powerup.png");
    }

    @Override
    public int getDuration() {
        return DEFAULT_DURATION_SEC;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        entity.forceSetBombsNotSolid(true);
        entity.setBombsSolid(false);
    }

    @Override
    protected void cancel(BomberEntity entity) {
        entity.forceSetBombsNotSolid(false);
        entity.setBombsSolid(true);
    }
}
