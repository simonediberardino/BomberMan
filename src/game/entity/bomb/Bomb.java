package game.entity.bomb;

import game.BomberMan;
import game.entity.Player;
import game.entity.blocks.DestroyableBlock;
import game.entity.blocks.StoneBlock;
import game.entity.models.*;
import game.models.Coordinates;
import game.models.Direction;
import game.ui.GamePanel;
import game.ui.Paths;

import java.awt.image.BufferedImage;
import java.util.*;

import static game.ui.GamePanel.GRID_SIZE;

public class Bomb extends Block implements Explosive {
    public static final int BOMB_SIZE = GamePanel.COMMON_DIVISOR * 4;
    public static final long PLACE_INTERVAL = 1000;
    private static final int EXPLODE_TIMER = 5000;
    private Runnable onExplodeCallback;
    private BomberEntity caller;

    public Bomb(Coordinates coords) {
        super(coords);
    }

    public Bomb(BomberEntity entity) {
        super(entity.getCoords());
        this.caller = entity;
    }

    @Override
    protected String getBasePath() {
        return Paths.getAssetsFolder() + "/bomb/";
    }

    @Override
    public BufferedImage getImage() {
        final int imagesCount = 3;

        String[] images = new String[imagesCount];

        for (int i = 0; i < images.length; i++) {
            images[i] = String.format("%sbomb_%d.png", getBasePath(), i);
        }

        if (System.currentTimeMillis() - lastImageUpdate < getImageRefreshRate()) {
            return this.image;
        }

        BufferedImage img = loadAndSetImage(images[lastImageIndex]);

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

        new Explosion(getCoords(), Direction.UP, this);
        new Explosion(getCoords(), Direction.RIGHT, this);
        new Explosion(getCoords(), Direction.DOWN, this);
        new Explosion(getCoords(), Direction.LEFT, this);

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
    public List<Class<? extends Entity>> getExplosionObstacles() {
        return Arrays.asList(StoneBlock.class, DestroyableBlock.class);
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
    public boolean canExplosionInteractWith(Entity e) {
        return (e == null) || (getExplosionInteractionEntities().stream().anyMatch(c -> c.isInstance(e)));
    }

    @Override
    public int getMaxExplosionDistance() {
        return caller != null ? caller.getCurrExplosionLength() : BomberMan.getInstance().getCurrentLevel().getExplosionLength();
    }
}
