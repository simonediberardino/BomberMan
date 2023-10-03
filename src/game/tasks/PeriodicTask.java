package game.tasks;

import javax.swing.*;

public class PeriodicTask {
    private final Runnable callback;
    private final int delay;
    private final Timer timer;

    public PeriodicTask(Runnable callback, int delay) {
        this.callback = callback;
        this.delay = delay;
        this.timer = new Timer(delay, e -> callback.run());
    }

    public void start() {
        timer.start();
    }

    public void setDelay(int delay) {
        timer.setDelay(delay);
    }

    public void resume() {
        try {
            if (timer != null) timer.start();
        } catch (Exception ignored) {
        }
    }

    public void stop() {
        try {
            if (timer != null) timer.stop();
        } catch (Exception ignored) {
        }
    }
}
