package game.entity.models;


import game.ui.panels.game.PitchPanel;
import game.values.DrawPriority;

public abstract class Block extends Entity {
    public final static int SIZE = PitchPanel.GRID_SIZE;

    public Block(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public int getSize() {
        return SIZE;
    }
    @Override
    public void eliminated(){
        destroy();
    }
    public void destroy(){
        despawn();
    }

}
