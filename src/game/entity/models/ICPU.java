package game.entity.models;

public interface ICPU {
    Direction chooseDirection(boolean forceChange);

    void changeDirection();
}
