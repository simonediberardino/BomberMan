package game.entity;

import game.BomberMan;
import game.controller.Command;
import game.models.Coordinates;
import game.ui.GameFrame;
import game.ui.GamePanel;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import static game.models.Direction.*;
import static game.ui.GamePanel.GRID_SIZE;
import static game.ui.GamePanel.PIXEL_UNIT;


public class Player extends Character implements Observer {

    @Override
    public String[] getFrontIcons() {
        return new String[]{
                "assets/player/player_front_0.png",
                "assets/player/player_front_1.png",
                "assets/player/player_front_0.png",
                "assets/player/player_front_2.png"
        };
    }

    @Override
    public String[] getLeftIcons() {
        //TODO
        return new String[]{
                "assets/player/player_right_0.png"
        };
    }

    @Override
    public String[] getBackIcons() {
        //TODO
        return new String[]{
                "assets/player/player_front_1.png",
                "assets/player/player_front_0.png",
                "assets/player/player_front_1.png",
                "assets/player/player_front_2.png"
        };
    }

    @Override
    public String[] getRightIcons() {
        //TODO
        return new String[]{
                "assets/player/player_right_0.png",
                "assets/player/player_right_1.png",
                "assets/player/player_right_2.png",
                "assets/player/player_right_1.png"

        };
    }

    public Player(Coordinates coordinates) {
        super(coordinates);
    }

    public Player() {
        super(new Coordinates(0, 0));
    }

    @Override
    public void despawn() {
        super.despawn();
        isAlive = false;
        BomberMan.getInstance().getKeyEventObservable().deleteObserver(this);
    }

    @Override
    public void spawn() {
        super.spawn();
        isAlive = true;
        BomberMan.getInstance().getKeyEventObservable().deleteObservers();
        BomberMan.getInstance().getKeyEventObservable().addObserver(this);
    }

    /**
     * Performs an interaction between this entity and another entity.
     *
     * @param e the other entity to interact with
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Enemy || e instanceof Explosion) {
            despawn();
        }
    }

    public void placeBomb() {
        Bomb bomb = new Bomb(
                new Coordinates(
                        getCoords().getX() / GRID_SIZE * GRID_SIZE + ((GRID_SIZE- Bomb.size)/2)
                        ,getCoords().getY() / GRID_SIZE * GRID_SIZE + ((GRID_SIZE-Bomb.size)/2)
                )
        );

        bomb.spawn();
        bomb.trigger();
    }

    private void handleAction(Command command) {
        switch (command) {
            case MOVE_UP: move(UP); break;
            case MOVE_DOWN: move(DOWN); break;
            case MOVE_LEFT: move(LEFT); break;
            case MOVE_RIGHT: move(RIGHT); break;
            case PLACE_BOMB: placeBomb(); break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        handleAction((Command) arg);
    }

}
