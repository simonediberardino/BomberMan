package game.powerups;

import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;

import java.awt.image.BufferedImage;

public class PistolPowerUp extends PowerUp {
    /**
     * Constructs a PowerUp entity with the specified coordinates.
     *
     * @param coordinates the coordinates of the PowerUp entity
     */
    public PistolPowerUp(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public BufferedImage getImage() {
        return null;
    }

    @Override
    public int getDuration() {
        return 15;
    }

    @Override
    protected void doApply(BomberEntity entity) {

    }

    @Override
    protected void cancel(BomberEntity entity) {

    }
}
