
package game.hardwareinput;

import game.Bomberman;
import game.data.DataInputOutput;
import game.tasks.PeriodicTask;
import game.events.Observable2;
import game.utils.Utility;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import static java.util.Map.entry;

/**
 This class represents an Observable object that observes key events and notifies its observers of the
 corresponding command that should be executed based on the key that was pressed.
 */
public class ControllerManager extends Observable2 implements KeyListener {
    private static ControllerManager instance;
    private static final int KEY_ESC = KeyEvent.VK_ESCAPE;
    private static int KEY_DELAY_MS = setDefaultCommandDelay();
    private final List<Command> commandQueue = new ArrayList<>();

    // Key-Command mapping
    private Map<Integer, Command> keyAssignment;

    // Stores the time of the last key event for each command
    private final Map<Command, Long> commandEventsTime = new HashMap<>();
    private PeriodicTask task;

    public ControllerManager() {
        instance = this;
        setupTask();
        setKeyMap();
    }

    private void setKeyMap() {
        keyAssignment = Map.of(
                DataInputOutput.getPlayerDataObject().getForwardKey(), Command.MOVE_UP,
                DataInputOutput.getPlayerDataObject().getLeftKey(), Command.MOVE_LEFT,
                DataInputOutput.getPlayerDataObject().getBackKey(), Command.MOVE_DOWN,
                DataInputOutput.getPlayerDataObject().getRightKey(), Command.MOVE_RIGHT,
                DataInputOutput.getPlayerDataObject().getBombKey(), Command.PLACE_BOMB,
                KEY_ESC, Command.PAUSE
        );
    }

    public void onKeyPressed(Command action) {
        if (action != null) {
            long currentTime = System.currentTimeMillis();
            long lastEventTime = commandEventsTime.getOrDefault(action, 0L);

            if (currentTime - lastEventTime >= KEY_DELAY_MS) {
                commandEventsTime.put(action, currentTime);
                commandQueue.add(action);
                resume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Command action = keyAssignment.get(e.getKeyCode());
        if (action != null) {
            onKeyPressed(action);
            // Interrupt mouse movement if needed
            Bomberman.getMatch().getMouseControllerManager().stopPeriodicTask();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Command action = keyAssignment.get(e.getKeyCode());
        if (action != null) {
            commandQueue.remove(action);
            if (commandQueue.isEmpty()) {
                stop();
            }
        }
    }

    public void onKeyReleased(Command action){
        commandQueue.remove(action);
        if(commandQueue.isEmpty()) stop();
    }

    private void setupTask() {
        task = new PeriodicTask(() -> {
            Set<Command> commandsToNotify = new HashSet<>(commandQueue); // Batch notifications
            for (Command command : commandsToNotify) {
                notifyObservers(command);
            }
        }, KEY_DELAY_MS);

        task.start();
    }

    private void resume() {
        try{
            if(task != null) {
                task.resume();
            }
        }catch (Exception ignored){}
    }

    private void stop() {
        try{
            if(task != null) task.stop();
        }catch (Exception ignored){}
    }

    public boolean isCommandPressed(Command c) {
        return commandQueue.contains(c);
    }

    private void updateDelay() {
        instance.task.setDelay(KEY_DELAY_MS);
    }

    public static int decreaseCommandDelay() {
        KEY_DELAY_MS = 15;
        if (instance != null) {
            instance.updateDelay();
        }
        return KEY_DELAY_MS;
    }

    public static int setDefaultCommandDelay(){
        KEY_DELAY_MS = 30;
        if (instance != null) {
            instance.updateDelay();
        }
        return KEY_DELAY_MS;
    }

    @Override public void keyTyped(KeyEvent e) {}
}