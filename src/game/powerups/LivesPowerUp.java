package game.powerups;

import game.data.DataInputOutput;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.utils.Paths;

import java.awt.image.BufferedImage;

public class LivesPowerUp extends PowerUp{
    /**
     * Constructs a PowerUp entity with the specified coordinates.
     *
     * @param coordinates the coordinates of the PowerUp entity
     */
    public LivesPowerUp(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/lives_up.png");
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        DataInputOutput.getInstance().increaseLives();
    }

    @Override
    protected void cancel(BomberEntity entity) {}

    @Override
    public boolean isDisplayable() {
        return false;
    }

    @Override
    public boolean canPickUp(BomberEntity entity) {
        return true;
    }
}
