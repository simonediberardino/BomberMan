package game.entity.models;

import game.hardwareinput.Command;

public enum Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN;

    public Command toCommand() {
        switch (this) {
            case UP:
                return Command.MOVE_UP;
            case DOWN:
                return Command.MOVE_DOWN;
            case LEFT:
                return Command.MOVE_LEFT;
            case RIGHT:
                return Command.MOVE_RIGHT;
        }
        return Command.MOVE_UP;
    }

    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return UP;
        }

    }


}
