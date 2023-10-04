package game.items;

import game.Bomberman;
import game.entity.models.BomberEntity;

public abstract class UsableItem {
    protected BomberEntity owner;

    public abstract void use();
    public abstract String getImagePath();

    public abstract int getCount();

    public void setOwner(BomberEntity owner) {
        this.owner = owner;
    }

    public void give() {
        Bomberman.getMatch().give(owner, this);
    }

    public void remove() {
        Bomberman.getMatch().removeItem(owner);
    }

    public BomberEntity getOwner() {
        return owner;
    }
}
