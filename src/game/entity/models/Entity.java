package game.entity.models;

import game.Bomberman;
import game.hardwareinput.MouseControllerManager;
import game.tasks.GameTickerObserver;
import game.events.RunnablePar;
import game.ui.panels.game.PitchPanel;
import game.utils.Utility;
import game.values.DrawPriority;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static game.ui.panels.game.PitchPanel.GRID_SIZE;
import static game.utils.Utility.loadImage;


/**
 * Represents an entity in the game world, such as a player, enemy, or obstacle.
 */
public abstract class Entity extends GameTickerObserver implements Comparable<Entity>{
    protected Set<Class<? extends Entity>> passiveInteractionEntities = getBasePassiveInteractionEntities();
    protected BufferedImage image;
    protected int lastImageIndex;
    protected long lastImageUpdate;
    protected float hitboxSizetoWidthRatio = 1;
    protected float hitboxSizeToHeightRatio = 1;
    private Coordinates coords;
    private boolean isSpawned = false;
    private boolean isImmune = false;
    private boolean isInvisible = false;
    private int paddingTop;
    private int paddingWidth;
    private String imagePath = "";
    private float alpha = 1;
    private final long id;

    protected RunnablePar paddingTopFunction = new RunnablePar() {
        @Override
        public <T> Object execute(T par) {
            int temp = (int) ((double) getSize() / (double)par - getSize());
            paddingTop = temp;
            return temp;
        }
    };

    protected RunnablePar paddingWidthFunction = new RunnablePar() {
        @Override
        public <T> Object execute(T par) {
            int temp = (int) (((double) getSize() / (double)par- getSize()) / 2);
            paddingWidth = temp;
            return temp;
        }
    };


    public Entity(){
        this(new Coordinates(-1, -1));
    }

    /**
     * Constructs an entity with the given coordinates.
     *
     * @param coordinates the coordinates of the entity
     */
    public Entity(Coordinates coordinates){
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.coords = coordinates;
    }

    protected String getBasePath(){ return ""; }

    protected void onSpawn(){}
    protected void onDespawn(){
    }

    /**
     * Performs an interaction between this entity and another entity.
     *
     * @param e the other entity to interact with
     */
    protected abstract void doInteract(Entity e);

    public abstract int getSize();

    /**
     * Returns the image of the entity.
     *
     * @return the image of the entity
     */
    public abstract BufferedImage getImage();

    /**
     * Returns the size of the entity in pixels.
     *
     * @return the size of the entity
     */
    public float getHitboxSizeToHeightRatio(){
        return hitboxSizeToHeightRatio;
    }
    //might be override
    public float getHitboxSizeToHeightRatio(String path){
        return getHitboxSizeToHeightRatio();
    }

    public final float getHitboxSizeToWidthRatio(String path){
        return getHitboxSizeToWidthRatio();
    }

    public final float getHitboxSizeToWidthRatio(){
        return hitboxSizetoWidthRatio;
    }

    public int getImageRefreshRate(){
        return 200;
    }

    public long getId() {
        return id;
    }

    public boolean isImmune() {
        return isImmune;
    }

    public void setImmune(boolean isImmune) {
        this.isImmune = isImmune;
    }

    /**
     * Loads the image at the given file path and sets it as the image of this entity.
     *
     * @param imagePath the file path of the image to load
     * @return the loaded image
     */
    public BufferedImage loadAndSetImage(String imagePath) {
        this.lastImageUpdate = System.currentTimeMillis();
        this.image = loadImage(imagePath);
        this.imagePath = imagePath;
        return this.image;
    }

    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the coordinates of the entity to the given coordinates.
     *
     * @param coordinates the new coordinates of the entity
     */
    public void setCoords(Coordinates coordinates) {
        this.coords = coordinates;
    }

    /**
     * Returns the coordinates of the entity.
     *
     * @return the coordinates of the entity
     */
    public Coordinates getCoords() {
        return coords;
    }

    /**
     * Returns true if the entity has been spawned in the game world, false otherwise.
     *
     * @return true if the entity has been spawned, false otherwise
     */
    public boolean isSpawned() {
        return isSpawned;
    }

    /**
     * Sets whether the entity has been spawned in the game world.
     *
     * @param s true if the entity has been spawned, false otherwise
     */
    protected void setSpawned(boolean s) {
        isSpawned = s;
    }

    protected Coordinates getSpawnOffset(){
        return new Coordinates((PitchPanel.GRID_SIZE-getSize())/2,(PitchPanel.GRID_SIZE-getSize())/2);
    }

    /**
     * Despawns the entity from the game world.
     */
    public final void despawn() {
        setSpawned(false);
        Bomberman.getMatch().removeEntity(this);
        this.onDespawn();
    }

    public final void spawnAtRandomCoordinates() {
        setCoords(Coordinates.generateCoordinatesAwayFrom(Bomberman.getMatch().getPlayer().getCoords(), GRID_SIZE * 3));
        spawn();
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    protected void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }

    /**
     * Spawns the entity if it is not already spawned and if there is no other entity at the desired coordinates.
     */
    public final void spawn(){
        spawn(false,true);
    }


    public final void spawn(boolean forceSpawn){
        spawn(forceSpawn,true);
    }

    // checks if entity has already been spawned
    // if not, force spawn it and add it to the game state
    public final void spawn(boolean forceSpawn, boolean forceCentering) {
        if (isSpawned()) { // if entity is already spawned, return
            return;
        }

        // centers entity on tile
        if (forceCentering)
            setCoords(Coordinates.roundCoordinates(getCoords(), getSpawnOffset()));

        // spawns entity if the spawn point is free, otherwise do nothing
        if (forceSpawn || !Coordinates.isBlockOccupied(coords)) {
            setSpawned(true); // mark entity as spawned
            Bomberman.getMatch().addEntity(this); // add entity to the game state
            onSpawn(); // run entity-specific spawn logic
        }
    }

    // calculates the coordinates of a point a certain distance away from the entity's top-left corner in a given direction
    protected Coordinates getNewTopLeftCoordinatesOnDirection(Direction d, int distance){
        int sign = 0;

        switch (d){
            case UP:case LEFT: sign = -1; break; // if direction is up or left, sign is negative
            case DOWN:case RIGHT: sign = 1; break; // if direction is down or right, sign is positive
        }

        switch (d){
            case LEFT:
            case RIGHT:
                return new Coordinates(getCoords().getX() + distance*sign, getCoords().getY()); // calculate new x-coordinate based on direction and distance

            case DOWN:
            case UP:
                return new Coordinates(getCoords().getX() , getCoords().getY() + distance * sign); // calculate new y-coordinate based on direction and distance
        }

        return null; // shouldn't happen
    }

    // returns a list of all the coordinates that make up the entity, including all tiles it occupies
    protected List<Coordinates> getAllCoordinates(){
        List<Coordinates> coordinates = new ArrayList<>();
        int last = 0;
        for (int step = 0; step <= getSize() / PitchPanel.COMMON_DIVISOR; step++) { // iterate over each step in entity's size
            for (int i = 0; i <= getSize() / PitchPanel.COMMON_DIVISOR; i++) { // iterate over each tile in entity
                if (i== getSize()/PitchPanel.COMMON_DIVISOR) last = PitchPanel.PIXEL_UNIT; // if last tile, use pixel unit instead of common divisor

                // add the new coordinate to the list of coordinates
                coordinates.add(new Coordinates(getCoords().getX() + step * PitchPanel.COMMON_DIVISOR, getCoords().getY() + i * PitchPanel.COMMON_DIVISOR - last));
            }
        }
        return coordinates; // return list of all coordinates that make up the entity
    }

    // returns a list of coordinates a certain number of steps away from the entity in a given direction, taking into account entity size
    protected List<Coordinates> getNewCoordinatesOnDirection(Direction d, int steps, int offset){
        List<Coordinates> desiredCoords = new ArrayList<>();

        switch (d) {
            case RIGHT: return getNewCoordinatesOnRight(steps, offset); // get coordinates to the right of entity
            case LEFT: return getNewCoordinatesOnLeft(steps, offset); // get coordinates to the left of entity
            case UP: return getNewCoordinatesOnUp(steps, offset); // get coordinates above entity
            case DOWN: return getNewCoordinatesOnDown(steps, offset, getSize()); // get coordinates below entity
        }

        return desiredCoords; // shouldn't happen
    }

    protected List<Coordinates> getNewCoordinatesOnRight(int steps, int offset) {
        List<Coordinates> coordinates = new ArrayList<>();
        int last = 0;
        for (int step = 0; step <= steps / offset; step++) {
            for (int i = 0; i <= getSize() / offset; i++) {
                if (i== getSize()/offset) last = PitchPanel.PIXEL_UNIT;

                coordinates.add(new Coordinates(getCoords().getX() + getSize()+step * offset, getCoords().getY() + i * offset - last));
            }
        }
        return coordinates;
    }

    protected List<Coordinates> getNewCoordinatesOnLeft(int steps, int offset) {
        List<Coordinates> coordinates = new ArrayList<>();
        int first = steps;
        int last = 0;
        for (int step = 0; step <= steps/offset; step++) {
            for (int i = 0; i <= getSize() / offset; i++) {
                if (i== getSize()/offset) last = PitchPanel.PIXEL_UNIT;
                coordinates.add(new Coordinates(getCoords().getX() - first - step * offset, getCoords().getY() + i * offset - last));
            }first =0;
        }
        return coordinates;
    }

    protected List<Coordinates> getNewCoordinatesOnUp(int steps, int offset) {
        List<Coordinates> coordinates = new ArrayList<>();
        int first = steps, last = 0;

        for (int step = 0; step <= steps/offset; step++) {
            for (int i = 0; i <= getSize() / offset; i++) {
                if (i== getSize()/offset) last = PitchPanel.PIXEL_UNIT;
                coordinates.add(new Coordinates(getCoords().getX() + i * offset - last, getCoords().getY() - first - step * offset));
            }
            first = 0;
        }

        return coordinates;
    }

    protected List<Coordinates> getNewCoordinatesOnDown(int steps, int offset, int size) {
        List<Coordinates> coordinates = new ArrayList<>();
        int first = steps, last = 0;

        for (int step = 0; step <= steps / offset; step++) {
            for (int i = 0; i <= getSize() / offset; i++) {
                if (i== getSize()/offset)
                    last = PitchPanel.PIXEL_UNIT;

                coordinates.add(new Coordinates(getCoords().getX() + i * offset - last, getCoords().getY() + size - 1 + first + step * offset));
            }
            first = 0;
        }
        
        return coordinates;
    }
    
    public void setPaddingTop(int p){
        paddingTop=p;
    }

    public void setPaddingWidth(int p){
        paddingWidth = p;
    }

    public int calculateAndGetPaddingTop(){
        return calculateAndGetPaddingTop(getHitboxSizeToHeightRatio());
    }

    public int calculateAndGetPaddingTop(double ratio){
        return (int) paddingTopFunction.execute(ratio);
    }

    public int getPaddingTop(){
        return paddingTop;
    }

    public int getPaddingWidth(){
        return paddingWidth;
    }

    public int calculateAndGetPaddingWidth(){
        return calculateAndGetPaddingWidth(getHitboxSizeToWidthRatio());
    }

    public int calculateAndGetPaddingWidth(double ratio){
        return (int) paddingWidthFunction.execute(ratio);
    }

    public DrawPriority getDrawPriority() {
        return DrawPriority.DRAW_PRIORITY_1;
    }

    @Override
    public int compareTo(Entity other) {
        return Comparator.comparing(Entity::getDrawPriority)
                .thenComparing(e -> e.getCoords().getY())
                .thenComparingInt(e -> (int) e.getId())
                .compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    /**
     * Eliminates the entity by despawning it.
     */
    public void eliminated() {
        despawn();
    }

    /**
     * Handles mouse click interactions.
     * If the entity is within one grid size distance from the player's center coordinates, it gets eliminated.
     */
    protected void mouseClickInteraction() {
        Coordinates centerCoordinatesOfEntity = Coordinates.roundCoordinates(Coordinates.getCenterCoordinatesOfEntity(Bomberman.getMatch().getPlayer()));
        // Check if the entity is within one grid size distance from the player's center coordinates
        if (getCoords().distanceTo(Coordinates.roundCoordinates(centerCoordinatesOfEntity)) <= PitchPanel.GRID_SIZE) {
            eliminated();
        }
    }

    /**
     * Handles mouse drag interactions.
     * This method allows dragging the entity to move it around.
     */
    protected void mouseDragInteraction() {
        Entity player = Bomberman.getMatch().getPlayer();
        MouseControllerManager mouseControllerManager = Bomberman.getMatch().getMouseControllerManager();
        Coordinates centerCoordinatesOfEntity = Coordinates.roundCoordinates(Coordinates.getCenterCoordinatesOfEntity(player));
        Coordinates mouseCoordinates = Coordinates.roundCoordinates(mouseControllerManager.getMouseCoords());

        if (mouseControllerManager.isMouseDraggedInteractionInterrupted()) {
            return;
        }

        // Check if the interacted entity is not within one grid size distance from the player's center coordinates
        // and the mouse drag interaction has not been entered yet
        if (!(Coordinates.roundCoordinates(getCoords()).distanceTo(Coordinates.roundCoordinates(centerCoordinatesOfEntity)) <= PitchPanel.GRID_SIZE)//todo
                && !mouseControllerManager.isMouseDragInteractionEntered()) {
            return;
        }

        List<Entity> entitiesOnOccupiedBlock = Coordinates.getEntitiesOnBlock(mouseCoordinates);

        //Check if there are other entities on the occupied block, and they are not the current entity

        if (!entitiesOnOccupiedBlock.isEmpty() && entitiesOnOccupiedBlock.stream().anyMatch(e -> e != this&&e!=Bomberman.getMatch().getPlayer())) {
            mouseControllerManager.setMouseDraggedInteractionInterrupted(true);
            return;
        }

        if (!Coordinates.isBlockOccupied(mouseCoordinates) && mouseCoordinates.validate(getSize())) {
            // Move the entity to the dragged mouse coordinates
            Bomberman.getMatch().getMouseControllerManager().setMouseDragInteractionEntered(true);
            Bomberman.getMatch().getMouseControllerManager().setMouseDraggedInteractionOccured(true);
            setCoords(Coordinates.roundCoordinates(mouseCoordinates, getSpawnOffset()));
        }
    }

    /**
     * Retrieves the set of base passive interaction entities.
     *
     * @return The set of base passive interaction entities.
     */
    protected abstract Set<Class<? extends Entity>> getBasePassiveInteractionEntities();

    /**
     * Removes a passive interaction entity.
     *
     * @param e The class of the entity to remove.
     */
    protected final void removePassiveInteractionEntity(Class<? extends Entity> e) {
        passiveInteractionEntities.remove(e);
    }

    /**
     * Adds a passive interaction entity.
     *
     * @param e The class of the entity to add.
     */
    protected final void addPassiveInteractionEntity(Class<? extends Entity> e) {
        passiveInteractionEntities.add(e);
    }

    /**
     * Checks if this entity can be interacted with by another entity.
     *
     * @param e The entity attempting to interact.
     * @return {@code true} if the entity can be interacted with, {@code false} otherwise.
     */
    protected final boolean canBeInteractedBy(Entity e) {
        return e == null || passiveInteractionEntities.stream().anyMatch(c -> c.isInstance(e));
    }

    protected boolean canEntityInteractWithMouseDrag(){
        return Bomberman.getMatch().getPlayer().getListClassInteractWithMouseDrag().stream().anyMatch(cls -> cls.isInstance(this)
            && Bomberman.getMatch().getMouseControllerManager().isMouseDragged());
    }

    protected boolean canEntityInteractWithMouseClick(){
        return Bomberman.getMatch()
                .getPlayer()
                .getListClassInteractWithMouseClick()
                .stream()
                .anyMatch(
                        cls -> cls.isInstance(this)
                )
                && Bomberman.getMatch().getMouseControllerManager().isMouseClicked();
    }

    /**
     * Handles mouse interactions.
     * This method is responsible for mouse click and mouse drag interactions.
     */
    public void mouseInteractions() {
        MouseControllerManager mouseControllerManager = Bomberman.getMatch().getMouseControllerManager();
        Entity entity = mouseControllerManager.getEntity();

        if (entity == null) {
            return;
        }

        if(canEntityInteractWithMouseClick()){
            mouseClickInteraction();
            return;
        }

        if(canEntityInteractWithMouseDrag()) {
            mouseDragInteraction();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setAlpha(float alpha){
        this.alpha = Utility.ensureRange(alpha, 0, 1);
    }

    public float getAlpha() {
        return alpha;
    }
}
