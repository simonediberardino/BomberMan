package game.entity.blocks;

import game.entity.models.Block;
import game.entity.models.Coordinates;

public abstract class MovableBlock extends Block {
    public MovableBlock(Coordinates coordinates) {
        super(coordinates);
    }
}
