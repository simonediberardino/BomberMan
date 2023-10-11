package game.items;

import game.Bomberman;
import game.entity.bomb.Bomb;
import game.events.UpdateCurrentAvailableBombsEvent;
import game.utils.Paths;
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
        owner.setBombsSolid(false);

        new UpdateCurrentAvailableBombsEvent().invoke(owner.getCurrentBombs() - 1);
        bombEntity = new Bomb(owner);
        Bomberman.getMatch().addBomb(bombEntity);

        bombEntity.setOnExplodeListener(() -> {
            owner.setPlacedBombs(owner.getPlacedBombs() - 1);
            Bomberman.getMatch().removeBomb(bombEntity);
            new UpdateCurrentAvailableBombsEvent().invoke(owner.getCurrentBombs() + 1);
        });

        bombEntity.spawn(true);
        bombEntity.trigger();
    }

    @Override
    public String getImagePath() {
        return String.format("%s/bomb/bomb_0.png", Paths.getEntitiesFolder());
    }

    @Override
    public int getCount() {
        return owner.getCurrentBombs();
    }
}
