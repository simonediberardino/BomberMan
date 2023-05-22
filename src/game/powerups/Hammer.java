package game.powerups;

import game.entity.blocks.DestroyableBlock;
import game.entity.models.BomberEntity;
import game.models.Coordinates;

import java.awt.image.BufferedImage;

public class Hammer extends PowerUp{
    /**
     * Constructs a PowerUp entity with the specified coordinates.
     *
     * @param coordinates the coordinates of the PowerUp entity
     */
    public Hammer(Coordinates coordinates) {
        super(coordinates);
        incompatiblePowerUps.add(BlockMoverPowerUp.class);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage("assets/entities/player/player_left_1_died.png");
    }

    @Override
    public int getDuration() {
        return 30;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        entity.getListClassInteractWithMouse().add(DestroyableBlock.class);
    }

    @Override
    protected void cancel(BomberEntity entity) {
        System.out.println("REMOVED");
        entity.getListClassInteractWithMouse().remove(DestroyableBlock.class);
    }
}