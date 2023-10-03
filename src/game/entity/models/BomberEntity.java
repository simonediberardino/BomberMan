package game.entity.models;

import game.Bomberman;
import game.entity.Player;
import game.entity.bomb.Bomb;
import game.entity.bonus.mysterybox.MysteryBoxPerk;
import game.events.UpdateCurrentAvailableBombsEvent;
import game.powerups.PowerUp;
import game.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public abstract class BomberEntity extends Character {
    public static final int MAX_BOMB_CAN_HOLD = 10;
    private final List<Class<? extends Entity>> listInteractWithMouseClick = new ArrayList<>();
    private final List<Class<? extends Entity>> listInteractWithMouseDrag = new ArrayList<>();
    private final List<Class<? extends PowerUp>> activePowerUp = new ArrayList<>();
    private int currExplosionLength = Bomberman.getMatch().getCurrentLevel().getExplosionLength();
    private int placedBombs = 0;
    private long lastPlacedBombTime = 0;
    private int currentBombs;

    /**
     * Constructs a new Character with the specified Coordinates.
     *
     * @param coordinates the coordinates of the new Character
     */
    public BomberEntity(Coordinates coordinates) {
        super(coordinates);
        listInteractWithMouseClick.add(MysteryBoxPerk.class);
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

    protected void placeBomb() {
        if (getCurrExplosionLength() == 0) {
            return;
        }

        if (placedBombs >= getMaxBombs()) {
            return;
        }

        if (getCurrentBombs() <= 0) {
            return;
        }

        if (Utility.timePassed(lastPlacedBombTime) < Bomb.PLACE_INTERVAL) {
            return;
        }

        lastPlacedBombTime = System.currentTimeMillis();
        placedBombs++;

        Bomb bomb = new Bomb(this);

        if (this instanceof Player) {
            new UpdateCurrentAvailableBombsEvent().invoke(getCurrentBombs() - 1);
        }

        bomb.setOnExplodeListener(() -> {
            placedBombs--;
            if (this instanceof Player) {
                new UpdateCurrentAvailableBombsEvent().invoke(getCurrentBombs() + 1);
            }
        });

        bomb.spawn(true);
        bomb.trigger();
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
