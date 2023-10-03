package game.powerups;

import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.utils.Paths;

import java.awt.image.BufferedImage;

public class EmptyPowerup extends PowerUp {
    /**
     * Constructs a PowerUp entity with the specified coordinates.
     *
     * @param coordinates the coordinates of the PowerUp entity
     */
    public EmptyPowerup(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/no_powerup.png");
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    protected void doApply(BomberEntity entity) {
    }

    @Override
    protected void cancel(BomberEntity entity) {
    }
}
