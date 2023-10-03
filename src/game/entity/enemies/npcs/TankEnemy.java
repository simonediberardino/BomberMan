package game.entity.enemies.npcs;

import game.entity.Player;
import game.entity.blocks.DestroyableBlock;
import game.entity.bomb.AbstractExplosion;
import game.entity.bomb.Bomb;
import game.entity.bomb.FireExplosion;
import game.entity.models.*;
import game.entity.models.Character;
import game.entity.models.Coordinates;
import game.entity.models.Direction;
import game.sound.AudioManager;
import game.sound.SoundModel;
import game.utils.Paths;
import game.utils.Utility;

import java.util.*;

public class TankEnemy extends IntelligentEnemy implements Explosive {
    private static final int STANDING_STILL_PERIOD = 1000;
    private static final int PROBABILITY_OF_SHOOTING = 30;
    private static final int SHOOTING_REFRESH_RATE = 2000;
    private boolean canShoot = false;
    private long lastUpdate = 0;

    public TankEnemy() {
        super();
    }

    public TankEnemy(Coordinates coordinates) {
        super(coordinates);
    }

    public String[] getCharacterOrientedImages() {
        return new String[]{String.format("%s/tank/tank_%s.png", Paths.getEnemiesFolder(), imageDirection.toString().toLowerCase())};
    }

    /**
     * Ran every game tick;
     *
     * @param arg
     */
    @Override
    public void doUpdate(boolean arg) {
        long currentTime = System.currentTimeMillis();

        // Check if it's time to update the shooting behavior
        if (Utility.timePassed(lastUpdate) > SHOOTING_REFRESH_RATE) {
            lastUpdate = currentTime;

            // Check if the entity can shoot and if a random probability allows shooting
            if (canShoot && Math.random() * 100 < PROBABILITY_OF_SHOOTING) {
                // Calculate new coordinates with an explosion offset for vertical directions
                Coordinates newCoords = getNewTopLeftCoordinatesOnDirection(currDirection, AbstractExplosion.SIZE);
                if (currDirection == Direction.UP || currDirection == Direction.DOWN) {
                    int x = newCoords.getX() + AbstractExplosion.SPAWN_OFFSET;
                    newCoords = new Coordinates(x, newCoords.getY());
                }

                // Play the explosion sound and create a new FireExplosion
                AudioManager.getInstance().play(SoundModel.EXPLOSION);

                new FireExplosion(this, newCoords, currDirection, this);

                canMove = false;
            }
            canShoot = true;
        }

        // Check if it's time to allow movement again after standing still
        if (Utility.timePassed(lastUpdate) > STANDING_STILL_PERIOD) {
            canMove = true;
        }

        // Call the superclass's doUpdate method
        super.doUpdate(arg);
    }

    @Override
    public Set<Class<? extends Entity>> getExplosionObstacles() {
        return Collections.emptySet();
    }

    @Override
    public boolean isObstacleOfExplosion(Entity e) {
        return (e == null) || (getExplosionObstacles().stream().anyMatch(c -> c.isInstance(e)));
    }

    @Override
    public Set<Class<? extends Entity>> getExplosionInteractionEntities() {
        return new HashSet<>() {{
            add(Player.class);
            add(Bomb.class);
        }};
    }

    @Override
    public int getMaxExplosionDistance() {
        return 4;
    }


}
