package game.events;

import java.util.HashSet;
import java.util.Set;

public class Observable2 {
    protected Set<Observer2> observers = new HashSet<>();

    public void notifyObservers(Object arg) {
        for (Observer2 o : observers)
            o.update(arg);
    }

    public void register(Observer2 o) {
        observers.add(o);
    }

    public void unregister(Observer2 o) {
        observers.remove(o);
    }

    public void unregisterAll() {
        observers.clear();
    }

    public void notify(Observer2 o, Object arg) {
        o.update(arg);
    }
}
