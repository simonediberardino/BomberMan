package game.tasks;

import game.Bomberman;
import game.entity.models.Entity;
import game.events.Observable2;
import game.events.Observer2;
import game.utils.Utility;

import java.util.Iterator;

/**
 The GameTickerObservable class is an observable that notifies its observers periodically with a fixed delay
 of DELAY_MS milliseconds, ignoring updates if a specific delay is not passed. It extends the Observable class.
 */
public class GameTickerObservable extends Observable2 {
    private final PeriodicTask periodicTask;
    private final int DELAY_MS = 50;
    /**
     This ActionListener updates observers of the GameTickerObservable periodically based on the specified delay. It loops through
     each observer in the observerSet to check if the delay has passed since the last update. If the delay has passed, it calls the
     update method of the observer with the current GameState object.
     */
    private final Runnable task = () -> {
        // loop through each observer in the observerSet
        for (Iterator<Observer2> iterator = observers.iterator(); iterator.hasNext();) {
            Observer2 observer = iterator.next();

            if (observer instanceof Entity && !((Entity) observer).isSpawned()) {
                iterator.remove();
                continue;
            }

            boolean delayPassed = true;

            if (observer instanceof GameTickerObserver) {
                GameTickerObserver gameTickerObserver = (GameTickerObserver) observer;
                long lastUpdate = gameTickerObserver.getLastUpdate();
                long delayObserverUpdate = (long) gameTickerObserver.getDelayObserverUpdate();

                delayPassed = Utility.timePassed(lastUpdate) >= delayObserverUpdate; // check if the delay has passed since the last update
            }

            if (delayPassed) { // if the delay has passed
                notify(observer, Bomberman.getMatch().getGameState());
            }
        }
    };

    public void resume() {
        periodicTask.resume();
    }

    public void stop() {
        periodicTask.stop();
    }

    public GameTickerObservable() {
        periodicTask = new PeriodicTask(task, DELAY_MS);
        periodicTask.start();
    }
}
