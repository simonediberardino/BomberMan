package game.powerups.portal;

import game.Bomberman;
import game.data.DataInputOutput;
import game.entity.models.BomberEntity;
import game.entity.models.Coordinates;
import game.level.Level;
import game.level.WorldSelectorLevel;
import game.utils.Paths;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

public class EndLevelPortal extends Portal {
    public EndLevelPortal(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/end_game.gif");
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public boolean canPickUp(BomberEntity entity) {
        return Bomberman.getMatch().getEnemiesAlive() <= 0;
    }

    @Override
    protected void doApply(BomberEntity entity) {
        super.doApply(entity);

        Bomberman.getMatch().getCurrentLevel().endLevel();
        DataInputOutput.getInstance().increaseLives();

        try {
            Level level = Bomberman.getMatch().getCurrentLevel();
            Class<? extends Level> nextLevelClass =
                    level.isLastLevelOfWorld()
                            ? WorldSelectorLevel.class
                            : level.getNextLevel();

            Bomberman.startLevel(nextLevelClass.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void cancel(BomberEntity entity) {
    }
}
