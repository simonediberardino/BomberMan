package game.entity.bonus.mysterybox;

import game.Bomberman;
import game.data.DataInputOutput;
import game.entity.blocks.HardBlock;
import game.entity.models.Coordinates;
import game.entity.models.Entity;
import game.level.Level;
import game.localization.Localization;
import game.powerups.PowerUp;
import game.ui.panels.game.PitchPanel;
import game.ui.viewelements.misc.ToastHandler;
import game.utils.Paths;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import static game.localization.Localization.MYSTERY_BOX_CONFIRM;
import static game.localization.Localization.MYSTERY_BOX_ERROR_MONEY;
import static game.ui.panels.game.PitchPanel.GRID_SIZE;

abstract class MysteryBox extends HardBlock {
    private static final int OPEN_BOX_TIME = 5000;
    private static final long CONFIRM_DELAY_MS = 5000;
    private Status status = Status.closed;
    private long lastClickInteraction = 0;
    protected Entity buyer;
    protected Level level;

    public MysteryBox(Level level, Entity entity) {
        super(new Coordinates(0, 0));
        this.buyer = entity;
        this.level = level;
    }

    abstract int getPrice();

    abstract void onPurchaseConfirm();

    @Override
    protected void onMouseClickInteraction() {
        if (status == Status.open) {
            return;
        }

        if (!buyer.isSpawned()) {
            return;
        }

        double distanceToUser = this.getCoords().distanceTo(buyer.getCoords());

        if (distanceToUser >= GRID_SIZE * 1.5) {
            return;
        }

        long currentPoints = DataInputOutput.getInstance().getScore();

        if (currentPoints < getPrice()) {
            showErrorMessage();
            return;
        }

        if (isConfirmDelayExpired()) {
            lastClickInteraction = System.currentTimeMillis();
            showConfirmMessage();
            return;
        }

        buy();
    }

    private void showErrorMessage() {
        String errorMessage = Localization.get(MYSTERY_BOX_ERROR_MONEY).replace("%price%", String.valueOf(getPrice()));
        ToastHandler.getInstance().show(errorMessage);
    }

    private void showConfirmMessage() {
        String confirmMessage = Localization.get(MYSTERY_BOX_CONFIRM).replace("%price%", String.valueOf(getPrice()));
        ToastHandler.getInstance().show(confirmMessage, false);
    }

    private boolean isConfirmDelayExpired() {
        return System.currentTimeMillis() - lastClickInteraction > CONFIRM_DELAY_MS;
    }

    private void buy() {
        long currentPoints = DataInputOutput.getInstance().getScore();

        if (currentPoints < getPrice()) {
            return;
        }

        onPurchaseConfirm();
        Bomberman.getMatch().getCurrentLevel().onPurchaseItem(getPrice());
        openBox();
    }

    private void openBox() {
        status = Status.open;
        Timer t = new Timer(OPEN_BOX_TIME, (e) -> closeBox());
        t.setRepeats(false);
        t.start();
    }

    private void closeBox() {
        status = Status.closed;
    }

    @Override
    protected void doInteract(Entity e) {
    }

    @Override
    public int getSize() {
        return GRID_SIZE;
    }

    @Override
    public BufferedImage getImage() {
        return loadAndSetImage(Paths.getPowerUpsFolder() + "/box_" + status.toString() + ".png");
    }

    private enum Status {
        closed,
        open
    }
}
