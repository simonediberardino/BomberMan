package game.utils;

import game.events.RunnablePar;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicReference;

public class GradientCallbackHandler {
    private final RunnablePar p;
    private final float start;
    private final float end;
    private final float step;
    private Timer t;

    public GradientCallbackHandler(RunnablePar p, final float start, final float end, final float step) {
        this.p = p;
        this.start = start;
        this.end = end;
        this.step = step;
    }

    public void execute() {
        AtomicReference<Float> currValue = new AtomicReference<>(start);

        t = new Timer(1, (l) -> {
            currValue.updateAndGet(v -> v - step);
            p.execute(currValue.get());

            boolean hasFinished = step >= 0 ? currValue.get() <= end : currValue.get() >= end;
            if (hasFinished) t.stop();
        });

        t.start();
    }
}
