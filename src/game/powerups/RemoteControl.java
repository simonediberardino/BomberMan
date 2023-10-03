package game.powerups;

import game.entity.bomb.Bomb;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.utils.Paths;

import java.awt.image.BufferedImage;

public class RemoteControl extends PowerUp {
    /**
     * Constructs a PowerUp entity with the specified coordinates.
     *
     * @param coordinates the coordinates of the PowerUp entity
     */
    public RemoteControl(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/remote_up.png");
    }

    @Override
    public int getDuration() {
        return 30;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        entity.addClassInteractWithMouseClick(Bomb.class);
    }

    @Override
    protected void cancel(BomberEntity entity) {
        entity.removeClassInteractWithMouse(Bomb.class);
    }
}
