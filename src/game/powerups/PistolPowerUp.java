package game.powerups;

import game.Bomberman;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.items.PistolItem;
import game.utils.Paths;

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
        return loadAndSetImage(Paths.getItemsPath() + "/pistol.png");
    }

    @Override
    public int getDuration() {
        return 30;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        Bomberman.getMatch().give(entity, new PistolItem());
    }

    @Override
    protected void cancel(BomberEntity entity) {
        Bomberman.getMatch().removeItem(entity);
    }

    @Override
    public boolean isDisplayable() {
        return false;
    }
}
