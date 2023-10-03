package game.powerups;

import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.hardwareinput.ControllerManager;
import game.utils.Paths;

import java.awt.image.BufferedImage;

public class SpeedPowerUp extends PowerUp {
    /**
     * Constructs an entity with the given coordinates.
     *
     * @param coordinates the coordinates of the entity
     */
    public SpeedPowerUp(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    protected String getBasePath() {
        return null;
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/speed_up.png");
    }

    @Override
    public int getDuration() {
        return DEFAULT_DURATION_SEC;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        ControllerManager.decreaseCommandDelay();
    }

    @Override
    protected void cancel(BomberEntity entity) {
        ControllerManager.setDefaultCommandDelay();
    }
}
