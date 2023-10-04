package game.hardwareinput;

import game.entity.models.Direction;

public enum Command {
    MOVE_UP,
    MOVE_DOWN,
    MOVE_RIGHT,
    MOVE_LEFT,
    ATTACK,
    PAUSE,
    INTERACT;

    public Direction commandToDirection() {
        switch (this) {
            case MOVE_UP:
                return Direction.UP;
            case MOVE_DOWN:
                return Direction.DOWN;
            case MOVE_LEFT:
                return Direction.LEFT;
            case MOVE_RIGHT:
                return Direction.RIGHT;
        }
        return null;
    }
}
