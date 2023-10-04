package game.entity.items;

import game.entity.models.BomberEntity;

public abstract class UsableItem {
    protected BomberEntity owner;

    public abstract void use();

    public void setOwner(BomberEntity owner) {
        this.owner = owner;
    }

    public void give() {
        owner.give(this);
    }

    public void remove() {
        owner.removeItem();
    }

    public BomberEntity getOwner() {
        return owner;
    }
}
