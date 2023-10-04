package game.entity.items;

import game.entity.Player;
import game.entity.bomb.Bomb;
import game.entity.models.BomberEntity;
import game.entity.models.Character;
import game.events.UpdateCurrentAvailableBombsEvent;
import game.utils.Utility;


public class BombItem extends UsableItem {
    private Bomb bombEntity;

    @Override
    public void use() {
        if (owner.getCurrExplosionLength() == 0) {
            return;
        }

        if (owner.getPlacedBombs() >= owner.getMaxBombs()) {
            return;
        }

        if (owner.getCurrentBombs() <= 0) {
            return;
        }

        if (Utility.timePassed(owner.getLastPlacedBombTime()) < Bomb.PLACE_INTERVAL) {
            return;
        }

        owner.setLastPlacedBombTime(System.currentTimeMillis());
        owner.setPlacedBombs(owner.getPlacedBombs() + 1);

        new UpdateCurrentAvailableBombsEvent().invoke(owner.getCurrentBombs() - 1);

        bombEntity = new Bomb(owner);

        bombEntity.setOnExplodeListener(() -> {
            owner.setPlacedBombs(owner.getPlacedBombs() - 1);
            new UpdateCurrentAvailableBombsEvent().invoke(owner.getCurrentBombs() + 1);
        });

        bombEntity.spawn(true);
        bombEntity.trigger();
    }
}
