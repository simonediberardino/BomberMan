package game.entity.bonus.mysterybox;

import game.Bomberman;
import game.entity.models.Coordinates;
import game.entity.models.Entity;
import game.level.Level;
import game.powerups.PowerUp;

import java.lang.reflect.InvocationTargetException;

public class MysteryBoxPerk extends MysteryBox {
    public MysteryBoxPerk(Level level, Entity entity) {
        super(level, entity);
    }

    @Override
    int getPrice() {
        return 200;
    }

    @Override
    void onPurchaseConfirm() {
        var powerUpClass = level.getRandomPowerUpClass();
        PowerUp powerUpInstance;
        try {
            powerUpInstance = powerUpClass.getConstructor(Coordinates.class).newInstance(new Coordinates(0, 0));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        powerUpInstance.apply(Bomberman.getMatch().getPlayer());
    }
}
