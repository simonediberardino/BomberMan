package game.entity.enemies.boss;

import game.Bomberman;
import game.entity.enemies.npcs.IntelligentEnemy;
import game.entity.models.Entity;
import game.entity.models.Coordinates;
import game.powerups.PowerUp;
import game.powerups.portal.EndLevelPortal;
import game.sound.AudioManager;
import game.sound.SoundModel;
import game.values.DrawPriority;

import java.util.*;

import static game.ui.panels.game.PitchPanel.GRID_SIZE;


/**
 * An abstract class for enemy bosses;
 */
public abstract class Boss extends IntelligentEnemy {
    protected static int SIZE = GRID_SIZE * 4;
    protected int currRageStatus = 0;
    protected TreeMap<Integer, Integer> healthStatusMap = new TreeMap<>(healthStatusMap());

    public Boss() {
        this(null);

        setCoords(Coordinates.randomCoordinatesFromPlayer(getSize(), getSize() * 2));
    }

    public Boss(Coordinates coordinates) {
        super(coordinates);
        super.setMaxHp(Bomberman.getMatch().getCurrentLevel().getBossMaxHealth());
        super.setHp(getMaxHp());
        super.setAttackDamage(1000);
    }

    @Override
    protected void onEliminated() {
        super.onEliminated();
        AudioManager.getInstance().play(SoundModel.BOSS_DEATH);
    }

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.DRAW_PRIORITY_3;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public Set<Class<? extends Entity>> getObstacles() {
        return getInteractionsEntities();
    }

    protected String getImageFromRageStatus() {
        return getImagePath();
    }

    protected abstract Map<Integer, Integer> healthStatusMap();

    @Override
    protected void onDespawn() {
        super.onDespawn();
        PowerUp endLevelPortal = new EndLevelPortal(Coordinates.generateCoordinatesAwayFromPlayer());
        endLevelPortal.spawn(true, true);
    }

    /**
     * Updates the rage status of the Boss, loading and setting the corresponding image.
     *
     * @param status the new rage status to be set.
     */
    protected void updateRageStatus(int status) {
        // If the new rage status is the same as the current one, nothing to update.
        if (status == currRageStatus) return;

        currRageStatus = status;
        // Get the corresponding image path from the current rage status.
        String imagePath = getImageFromRageStatus();
        // Load and set the image.
        loadAndSetImage(imagePath);
    }
}
