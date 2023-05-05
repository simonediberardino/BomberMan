package game.powerups;

import game.entity.blocks.DestroyableBlock;
import game.entity.models.BomberEntity;
import game.models.Coordinates;
import game.utils.Paths;

import java.awt.image.BufferedImage;

public class TransparentDestroyableBlocksPowerUp extends PowerUp {
    public TransparentDestroyableBlocksPowerUp(Coordinates coords){
        super(coords);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/blocks_up.gif");
    }

    @Override
    public int getDuration() {
        return DEFAULT_DURATION_SEC;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        entity.getWhiteListObstacles().remove(DestroyableBlock.class);
    }

    @Override
    protected void cancel(BomberEntity entity) {
        entity.getWhiteListObstacles().remove(DestroyableBlock.class);
    }
}
