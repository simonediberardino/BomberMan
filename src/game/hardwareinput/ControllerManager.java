
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
    public Set<Command> commandQueue = new HashSet<>();

    // Key-Command mapping
    private Map<Integer, Command> keyAssignment;

    // Stores the time of the last key event for each command
    private final Map<Command, Long> commandEventsTime = new HashMap<>();
    private PeriodicTask task;

    public ControllerManager(){
        instance = this;
        setupTask();
        // If illegal keys are found, reset the key map;
        try{
            setKeyMap();
        }catch (IllegalArgumentException e){
            DataInputOutput.getInstance().resetKeys();
            DataInputOutput.getInstance().updateStoredPlayerData();
            setKeyMap();
            e.printStackTrace();
        }
    }

    private void setKeyMap(){
        keyAssignment = Map.ofEntries(
                entry(DataInputOutput.getInstance().getForwardKey(), Command.MOVE_UP),
                entry(DataInputOutput.getInstance().getLeftKey(), Command.MOVE_LEFT),
                entry(DataInputOutput.getInstance().getBackKey(), Command.MOVE_DOWN),
                entry(DataInputOutput.getInstance().getRightKey(), Command.MOVE_RIGHT),
                entry(DataInputOutput.getInstance().getBombKey(), Command.PLACE_BOMB),
                entry(KEY_ESC, Command.PAUSE)
        );
    }

    public void onKeyPressed(Command action){
        // Ignore the event if the time elapsed since the last event is less than KEY_DELAY_MS

        if(action != null) {
            commandEventsTime.put(action, System.currentTimeMillis());
            commandQueue.add(action);
        }

        resume();
    }

    /**
     * Handles key pressed events and notifies observers with the corresponding command that should be executed.
     *
     * @param e the KeyEvent object that contains the information of the key that was pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        Command action = keyAssignment.get(e.getKeyCode());
        if(Utility.timePassed( commandEventsTime.getOrDefault(action, 0L)) < KEY_DELAY_MS) return;


        onKeyPressed(action);
        // if a button is pressed, mouse movement gets interrupted
        Bomberman.getMatch().getMouseControllerManager().stopPeriodicTask();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Command action = keyAssignment.get(e.getKeyCode());
        onKeyReleased(action);
    }

    public void onKeyReleased(Command action){
        commandQueue.remove(action);
        if(commandQueue.isEmpty()) stop();
    }

    private void setupTask() {
        task = new PeriodicTask(() -> {
            for (Command command : new HashSet<>(commandQueue)) {
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