package game.entity.bomb;

import game.Bomberman;
import game.entity.Player;
import game.entity.blocks.DestroyableBlock;
import game.entity.blocks.HardBlock;
import game.entity.blocks.MovableBlock;
import game.entity.models.*;
import game.entity.models.Coordinates;
import game.entity.models.Direction;
import game.sound.AudioManager;
import game.sound.SoundModel;
import game.utils.Paths;
import game.ui.panels.game.PitchPanel;
import game.utils.Utility;

import java.awt.image.BufferedImage;
import java.util.*;

public class Bomb extends MovableBlock implements Explosive {
    public static final int BOMB_SIZE = PitchPanel.COMMON_DIVISOR * 2;
    public static final long PLACE_INTERVAL = 1000;
    private static final int EXPLODE_TIMER = 5000;
    private final Entity caller;
    private Runnable onExplodeCallback;

    public Bomb(BomberEntity entity) {
        super(Coordinates.getCenterCoordinatesOfEntity(entity));
        this.caller = entity;
    }

    @Override
    protected String getBasePath() {
        return String.format("%s/bomb/", Paths.getEntitiesFolder());
    }

    @Override
    public BufferedImage getImage() {
        final int imagesCount = 3;

        String[] images = new String[imagesCount];

        for (int i = 0; i < images.length; i++) {
            images[i] = String.format("%sbomb_%d.png", getBasePath(), i);
        }

        if (Utility.timePassed(lastImageUpdate) < getImageRefreshRate()) {
            return this.image;
        }

        BufferedImage img = loadAndSetImage(images[lastImageIndex]);

        AudioManager.getInstance().play(SoundModel.BOMB_CLOCK);

        lastImageIndex++;
        if (lastImageIndex >= images.length) {
            lastImageIndex = 0;
        }

        return img;
    }

    public void setOnExplodeListener(Runnable runnable) {
        onExplodeCallback = runnable;
    }

    /**
     * Performs an interaction between this entity and another entity.
     *
     * @param e the other entity to interact with
     */
    @Override
    protected void doInteract(Entity e) {
    }

    public void explode() {
        if (!isSpawned()) {
            return;
        }

        despawn();

        AudioManager.getInstance().play(SoundModel.EXPLOSION);

        new FireExplosion(caller, getCoords(), Direction.UP, this).spawn(true, false);
        new FireExplosion(caller, getCoords(), Direction.RIGHT, this).spawn(true, false);
        new FireExplosion(caller, getCoords(), Direction.DOWN, this).spawn(true, false);
        new FireExplosion(caller, getCoords(), Direction.LEFT, this).spawn(true, false);

        if (onExplodeCallback != null) onExplodeCallback.run();
    }

    public void trigger() {
        TimerTask explodeTask = new TimerTask() {
            public void run() {
                explode();
            }
        };

        Timer timer = new Timer();
        timer.schedule(explodeTask, EXPLODE_TIMER);
    }


    @Override
    public int getSize() {
        return BOMB_SIZE;
    }

    @Override
    public Set<Class<? extends Entity>> getExplosionObstacles() {
        return new HashSet<>() {{
            add(HardBlock.class);
            add(DestroyableBlock.class);
        }};
    }

    @Override
    public boolean isObstacleOfExplosion(Entity e) {
        return (e == null) || (getExplosionObstacles().stream().anyMatch(c -> c.isInstance(e)));
    }

    @Override
    public List<Class<? extends Entity>> getExplosionInteractionEntities() {
        return Arrays.asList(DestroyableBlock.class, Enemy.class, Player.class, Bomb.class);
    }

    @Override
    public int getMaxExplosionDistance() {
        return Bomberman.getMatch().getCurrentLevel().getExplosionLength();
    }

    @Override
    public void onMouseClickInteraction() {
        eliminated();
    }

    @Override
    public void destroy() {
        explode();
    }

    @Override
    protected Set<Class<? extends Entity>> getBasePassiveInteractionEntities() {
        return new HashSet<>(Arrays.asList(FireExplosion.class, AbstractExplosion.class));
    }

    @Override
    public void onExplosion(AbstractExplosion explosion) {
        explode();
    }
}

