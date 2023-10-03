package game.entity.blocks;

import game.Bomberman;
import game.entity.bomb.AbstractExplosion;
import game.entity.bomb.ConfettiExplosion;
import game.entity.models.Entity;
import game.entity.models.Coordinates;
import game.powerups.PowerUp;
import game.powerups.portal.EndLevelPortal;
import game.utils.Utility;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class DestroyableBlock extends MovableBlock {
    private static final int POWER_UP_SPAWN_CHANGE = 33;
    private Class<? extends PowerUp> powerUpClass;

    public DestroyableBlock(Coordinates coordinates, Class<PowerUp> powerUpClass) {
        super(coordinates);
        this.powerUpClass = powerUpClass;

    }

    public DestroyableBlock(Coordinates coordinates) {
        this(coordinates, null);
    }

    /**
     * Performs an interaction between this entity and another entity.
     *
     * @param e the other entity to interact with
     */
    @Override
    protected void doInteract(Entity e) {
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Bomberman.getMatch().getCurrentLevel().getDestroyableBlockImagePath());
    }

    @Override
    protected void onDespawn() {
        super.onDespawn();

        if (powerUpClass == null) {
            return;
        }

        int spawnPercentage = powerUpClass == EndLevelPortal.class ? 100 : POWER_UP_SPAWN_CHANGE;

        Utility.runPercentage(spawnPercentage, () -> {
            PowerUp powerUp;
            try {
                powerUp = powerUpClass.getConstructor(Coordinates.class).newInstance(getCoords());
                powerUp.spawn(true, true);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected Set<Class<? extends Entity>> getBasePassiveInteractionEntities() {
        return new HashSet<>(Collections.singletonList(AbstractExplosion.class));
    }

    @Override
    public void onExplosion(AbstractExplosion explosion) {
        explosion.attack(this);
    }

    public Class<? extends PowerUp> getPowerUpClass() {
        return powerUpClass;
    }

    public void setPowerUpClass(Class<? extends PowerUp> powerUpClass) {
        this.powerUpClass = powerUpClass;
    }
}
