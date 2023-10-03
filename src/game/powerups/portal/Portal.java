package game.powerups.portal;

import game.Bomberman;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.powerups.PowerUp;

public abstract class Portal extends PowerUp {
    public Portal(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public boolean isDisplayable() {
        return false;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        Bomberman.getMatch().toggleGameState();
    }
}
