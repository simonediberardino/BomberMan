package game.powerups;

import game.Bomberman;
import game.data.DataInputOutput;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.events.UpdateMaxBombsEvent;
import game.utils.Paths;

import java.awt.image.BufferedImage;

public class IncreaseMaxBombsPowerUp extends PowerUp{

    /**
     * Constructs a PowerUp entity with the specified coordinates.
     *
     * @param coordinates the coordinates of the PowerUp entity
     */
    public IncreaseMaxBombsPowerUp(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/increase_max_bombs_powerup.png");
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        new UpdateMaxBombsEvent().invoke(entity.getCurrentBombs()+1);
    }

    @Override
    protected void cancel(BomberEntity entity) {
    }

    @Override
    public boolean canPickUp(BomberEntity entity) {
        return !(DataInputOutput.getInstance().getObtainedBombs() >= Bomberman.getMatch().getCurrentLevel().getMaxBombs());
    }


}
