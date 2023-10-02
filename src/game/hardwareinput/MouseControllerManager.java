package game.hardwareinput;

import game.Bomberman;
import game.tasks.PeriodicTask;
import game.entity.Player;
import game.entity.models.Entity;
import game.entity.models.Coordinates;
import game.entity.models.Direction;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The MouseControllerManager class handles mouse interactions and controls player movement based on mouse input.
 */
public class MouseControllerManager extends MouseAdapter implements MouseMotionListener {
    private boolean mouseDraggedInteractionOccured;
    private boolean mouseDraggedInteractionInterrupted;
    private boolean mouseDragInteractionEntered;
    private boolean isMouseDragged;
    private boolean isMouseClicked;
    private LinkedList<MouseEvent> mouseClickQueue = new LinkedList<>();
    // First and last directions from the player are differentiated so the task can be terminated when they have no direction in common,
    // in order to avoid an infinite loop
    private List<Direction> firstDirectionsFromPlayer = new ArrayList<>();
    private List<Direction> latestDirectionsFromPlayer = new ArrayList<>();
    private Coordinates mouseCoords;
    private final int DELAY = 300;
    private Entity entity = null;

    private final Runnable task = () -> {
        Player currPlayer = Bomberman.getMatch().getPlayer();
        if(currPlayer == null) return;
        onCooldown();

        //interact with mouse
        if (Bomberman.getMatch().getPlayer() != null && !currPlayer.getAliveState())
            return;

        if (entity != null && Bomberman.getMatch().getPlayer().getListClassInteractWithMouseClick().contains(entity.getClass())) {
            entity.mouseInteractions();
            onPeriodicTaskEnd();
            nextCommandInQueue();
            return;
        }

        latestDirectionsFromPlayer = mouseCoords.fromCoordinatesToDirection(currPlayer.getCoords());
        for (Direction d : latestDirectionsFromPlayer) {
            Bomberman.getMatch().getControllerManager().onKeyPressed(d.toCommand());
        }

        if (firstDirectionsFromPlayer.isEmpty() || firstDirectionsFromPlayer.stream().anyMatch(d -> latestDirectionsFromPlayer.contains(d))) {
            return;
        }

        onPeriodicTaskEnd();
        nextCommandInQueue();
    };

    /**
     * Retrieves and removes the next command in the mouseClickQueue.
     */
    public void nextCommandInQueue(){
        mouseClickQueue.poll();
        if(!mouseClickQueue.isEmpty())
        onMouseClicked(mouseClickQueue.peekFirst());
    }

    /**
     * Called when the mouse button is released.
     *
     * @param event The MouseEvent representing the release of the mouse button.
     */
    @Override
    public void mouseReleased(MouseEvent event){
        // If a mouse dragged interaction was not successful and the mouse is released, call mouseClicked
        // so that click-dependent interactions can be called.
        // This is necessary because mouseClicked is never called when mouseDragged is called, unlike mousePressed.
        // mouseClicked was still chosen because the two methods shouldn't be called at exactly the same time.
        if (!isMouseDragged) {
            return;
        }
        if (!mouseDraggedInteractionOccured) mouseClicked(event);
        mouseDragInteractionEntered = false;
        isMouseDragged = false;
        mouseDraggedInteractionOccured = false;
        mouseDraggedInteractionInterrupted = false;
    }

    /**
     * Called when the mouse button is pressed.
     *
     * @param event The MouseEvent representing the press of the mouse button.
     */
    @Override
    public void mousePressed(MouseEvent event){
        if(isMouseClicked) return;
        mouseCoords = new Coordinates(event.getX(), event.getY());
        entity = Coordinates.getEntityOnCoordinates(mouseCoords);
    }

    private final PeriodicTask playerMovementTask = new PeriodicTask(task, DELAY);

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!mouseClickQueue.isEmpty()) {
            mouseClickQueue.add(e);
            return;
        }

        mouseClickQueue.add(e);
        onMouseClicked(e);
    }

    public void onMouseClicked(MouseEvent e){
        if(isMouseClicked) return;
        isMouseClicked = true;

        Player player = Bomberman.getMatch().getPlayer();
        if(player == null || !player.getAliveState()) {
            return;
        }

        //isMouseClicked = true;
        mouseCoords = new Coordinates(e.getX(), e.getY());
        firstDirectionsFromPlayer = mouseCoords.fromCoordinatesToDirection(Bomberman.getMatch().getPlayer().getCoords());
        entity = Coordinates.getEntityOnCoordinates(mouseCoords);
        playerMovementTask.resume();
    }

    // Directions are refreshed and will be replaced in task
    public void onCooldown() {
        for (Direction d: firstDirectionsFromPlayer) {
            Bomberman.getMatch().getControllerManager().onKeyReleased(d.toCommand());
        }
    }

    public Coordinates getMouseCoords() {
        return mouseCoords;
    }

    public void setMouseDraggedInteractionOccured(boolean mouseDraggedInteractionOccured) {
        this.mouseDraggedInteractionOccured = mouseDraggedInteractionOccured;
    }

    public boolean isMouseDraggedInteractionInterrupted() {
        return mouseDraggedInteractionInterrupted;
    }

    public void setMouseDraggedInteractionInterrupted(boolean mouseDraggedInteractionInterrupted) {
        this.mouseDraggedInteractionInterrupted = mouseDraggedInteractionInterrupted;
    }

    public boolean isMouseDragInteractionEntered() {
        return mouseDragInteractionEntered;
    }

    public void setMouseDragInteractionEntered(boolean mouseDragInteractionEntered) {
        this.mouseDragInteractionEntered = mouseDragInteractionEntered;
    }

    public boolean isMouseDragged() {
        return isMouseDragged;
    }

    public boolean isMouseClicked() {
        return isMouseClicked;
    }

    public void stopPeriodicTask(){
        mouseClickQueue = new LinkedList<>();
        onPeriodicTaskEnd();
    }

    public void onPeriodicTaskEnd(){
        if(!isMouseClicked)
            return;

        playerMovementTask.stop();
        onCooldown();
        isMouseClicked = false;
    }

    @Override
    public void mouseDragged(MouseEvent event){
        if(isMouseClicked) return;

        isMouseDragged = true;
        mouseCoords = new Coordinates(event.getX(), event.getY());
        if (entity == null) return;
        entity.mouseInteractions();
    }

    public Entity getEntity(){
        return entity;
    }

}
