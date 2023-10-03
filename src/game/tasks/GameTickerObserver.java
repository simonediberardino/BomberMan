package game.tasks;

import game.events.Observer2;

public abstract class GameTickerObserver implements Observer2 {
    private static final float DEFAULT_DELAY_OBSERVER = 40;
    protected long lastUpdate = 0L;

    @Override
    public void update(Object arg) {
        lastUpdate = System.currentTimeMillis();
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    protected float getDelayObserverUpdate() {
        return DEFAULT_DELAY_OBSERVER;
    }
}
