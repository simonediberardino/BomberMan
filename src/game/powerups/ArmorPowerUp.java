package game.powerups;

import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.utils.Paths;

import java.awt.image.BufferedImage;

public class ArmorPowerUp extends PowerUp {
    /**
     * Constructs an entity with the given coordinates.
     *
     * @param coordinates the coordinates of the entity
     */
    public ArmorPowerUp(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/armor_up.png");
    }

    @Override
    public int getDuration() {
        return 15;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        entity.setImmune(true);
    }

    @Override
    protected void cancel(BomberEntity entity) {
        if(entity.isSpawned())
            entity.setImmune(false);
    }
}
