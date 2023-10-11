package game.entity.models;

import game.Bomberman;
import game.entity.bomb.Bomb;
import game.entity.bonus.mysterybox.MysteryBoxPerk;
import game.items.BombItem;
import game.items.UsableItem;
import game.powerups.PowerUp;
import game.powerups.TransparentBombsPowerUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static game.ui.panels.game.PitchPanel.GRID_SIZE;

public abstract class BomberEntity extends Character implements Explosive {
    public static final int MAX_BOMB_CAN_HOLD = 10;
    private final List<Class<? extends Entity>> listInteractWithMouseClick = new ArrayList<>();
    private final List<Class<? extends Entity>> listInteractWithMouseDrag = new ArrayList<>();
    private final List<Class<? extends PowerUp>> activePowerUp = new ArrayList<>();
    private int currExplosionLength = Bomberman.getMatch().getCurrentLevel().getExplosionLength();
    private int placedBombs = 0;
    private long lastPlacedBombTime = 0;
    private int currentBombs;
    private UsableItem weapon;
    private boolean bombsSolid = true;
    private boolean forceBombsSolid = false;

    /**
     * Constructs a new Character with the specified Coordinates.
     *
     * @param coordinates the coordinates of the new Character
     */
    public BomberEntity(Coordinates coordinates) {
        super(coordinates);
        listInteractWithMouseClick.add(MysteryBoxPerk.class);
    }

    @Override
    protected void onSpawn() {
        super.onSpawn();
        Bomberman.getMatch().give(this, new BombItem());
    }

    @Override
    public void move(Coordinates coordinates) {
        super.move(coordinates);
        handleInteractionWithBombs();
    }

    private void handleInteractionWithBombs() {
        if (bombsSolid || forceBombsSolid) {
            return;
        }

        if (getMinDistanceToBomb().orElse(0.0) > GRID_SIZE / 2f)
            setBombsSolid(true);
    }

    protected Optional<Double> getMinDistanceToBomb() {
        List<Bomb> bombs = Bomberman.getMatch().getBombs();
        return bombs.stream().map(e -> e.getCoords().distanceTo(this.getCoords())).min(Double::compare);
    }

    public void forceSetBombsNotSolid(boolean bombsNotSolid) {
        this.forceBombsSolid = bombsNotSolid;
    }

    public void setBombsSolid(boolean bombsSolid) {
        this.bombsSolid = bombsSolid;

        if (!bombsSolid) {
            addWhiteListObstacle(Bomb.class);
        } else if (!forceBombsSolid) {
            removeWhiteListObstacle(Bomb.class);
        }
    }

    public int getCurrExplosionLength() {
        return currExplosionLength;
    }

    public void setCurrExplosionLength(int value) {
        currExplosionLength = value;
    }

    public int getMaxBombs() {
        return Bomberman.getMatch().getCurrentLevel().getMaxBombs();
    }

    public int getPlacedBombs() {
        return placedBombs;
    }

    public void setPlacedBombs(int placedBombs) {
        this.placedBombs = placedBombs;
    }

    public long getLastPlacedBombTime() {
        return lastPlacedBombTime;
    }

    public void setLastPlacedBombTime(long lastPlacedBombTime) {
        this.lastPlacedBombTime = lastPlacedBombTime;
    }

    public UsableItem getWeapon() {
        return weapon;
    }

    public void setWeapon(UsableItem weapon) {
        this.weapon = weapon;
    }

    public int getCurrentBombs() {
        return this.currentBombs;
    }

    public void setCurrentBombs(int bomb) {
        this.currentBombs = bomb;
    }

    public void addClassInteractWithMouseClick(Class<? extends Entity> cls) {
        listInteractWithMouseClick.add(cls);
    }

    public void addClassInteractWithMouseDrag(Class<? extends Entity> cls) {
        listInteractWithMouseDrag.add(cls);
    }

    public void removeClassInteractWithMouse(Class<? extends Entity> cls) {
        listInteractWithMouseClick.remove(cls);
    }

    public void removeClassInteractWithDrag(Class<? extends Entity> cls) {
        listInteractWithMouseDrag.remove(cls);
    }

    public List<Class<? extends PowerUp>> getActivePowerUps() {
        return activePowerUp;
    }

    public void removeActivePowerUp(PowerUp p) {
        getActivePowerUps().removeIf(e -> e.isInstance(p));
    }

    public List<Class<? extends Entity>> getListClassInteractWithMouseClick() {
        return listInteractWithMouseClick;
    }

    public List<Class<? extends Entity>> getListClassInteractWithMouseDrag() {
        return listInteractWithMouseDrag;
    }
}
