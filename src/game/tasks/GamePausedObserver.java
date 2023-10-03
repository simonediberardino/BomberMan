package game.tasks;

import game.Bomberman;
import game.events.Observer2;
import game.hardwareinput.Command;

public class GamePausedObserver implements Observer2 {
    @Override
    public void update(Object arg) {
        if (!(arg instanceof Command)) {
            return;
        }

        if (arg == Command.PAUSE) {
            Bomberman.getMatch().toggleGameState();
        }
    }
}
