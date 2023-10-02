package game.powerups;

import game.entity.blocks.DestroyableBlock;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.utils.Paths;

import java.awt.image.BufferedImage;

public class Hammer extends PowerUp{
    /**
     * Constructs a PowerUp entity with the specified coordinates.
     *
     * @param coordinates the coordinates of the PowerUp entity
     */
    public Hammer(Coordinates coordinates) {
        super(coordinates);
        getIncompatiblePowerUps().add(BlockMoverPowerUp.class);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/hammer.png");
    }

    @Override
    public int getDuration() {
        return 30;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        entity.getListClassInteractWithMouseClick().add(DestroyableBlock.class);
    }

    @Override
    protected void cancel(BomberEntity entity) {
        entity.getListClassInteractWithMouseClick().remove(DestroyableBlock.class);
    }
}
