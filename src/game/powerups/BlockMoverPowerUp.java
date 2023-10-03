package game.powerups;

import game.entity.blocks.MovableBlock;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.utils.Paths;

import java.awt.image.BufferedImage;

public class BlockMoverPowerUp extends PowerUp {
    public BlockMoverPowerUp(Coordinates coordinates) {
        super(coordinates);
        getIncompatiblePowerUps().add(Hammer.class);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/hand.png");
    }

    @Override
    public int getDuration() {
        return 30;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        entity.addClassInteractWithMouseDrag(MovableBlock.class);
    }

    @Override
    protected void cancel(BomberEntity entity) {
        entity.removeClassInteractWithDrag(MovableBlock.class);
    }
}
