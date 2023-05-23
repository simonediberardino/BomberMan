package game.models;

import game.Bomberman;
import game.entity.models.Entity;
import game.ui.panels.game.PitchPanel;

import java.awt.*;
import java.time.temporal.ValueRange;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static game.ui.panels.game.PitchPanel.GRID_SIZE;

public class Coordinates {
    private final int x;
    private final int y;

    public Coordinates() {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     Check whether a coordinate of an Entity is inside the pitch or not;
     @return true if valid, false otherwise;
     */
    public boolean validate(Entity e){
        return validate(e.getSize());
    }
    public boolean validate( int size) {
        Dimension gamePanelDimensions = Bomberman
                .getBombermanFrame()
                .getPitchPanel()
                .getPanelDimensions();


        ValueRange rangeY = ValueRange.of(0, gamePanelDimensions.height - size);
        ValueRange rangeX = ValueRange.of(0, gamePanelDimensions.width - size);

        return (rangeY.isValidValue(getY()) && rangeX.isValidValue(getX()));
    }

    public static Coordinates roundedRandomCoords(Coordinates offset){
        Dimension dimensions = Bomberman.getBombermanFrame().getPitchPanel()
                .getPanelDimensions();

        return roundCoordinates(new Coordinates( (    (int) (Math.random() * dimensions.getWidth())  ),
                (  (int) (Math.random() *  dimensions.getHeight() ) )), offset);
    }

    public static Coordinates roundCoordinates(Coordinates coords){
        return roundCoordinates(coords,new Coordinates(0,0));
    }
    public static Coordinates roundCoordinates(Coordinates coords, Coordinates offset){
        return new Coordinates(((coords.getX() / GRID_SIZE) * GRID_SIZE + offset.getX()), ((coords.getY()/ GRID_SIZE) * GRID_SIZE + offset.getY()));
    }

    public static Coordinates generateRandomCoordinates(){
        return generateRandomCoordinates(new Coordinates(0,0));
    }

    public static Coordinates generateCoordinatesAwayFromPlayer() {
        return generateCoordinatesAwayFrom(Bomberman.getMatch().getPlayer().getCoords(), GRID_SIZE * 3);
    }

    public static Coordinates generateCoordinatesAwayFrom(Coordinates other, int offset) {
        Coordinates coord;
        while ((coord = Coordinates.generateRandomCoordinates()).distanceTo(other) < offset);
        return coord;
    }

    public static Coordinates randomCoordinatesFromPlayer() {
        return Coordinates.generateCoordinatesAwayFrom(Bomberman.getMatch().getPlayer().getCoords(), GRID_SIZE * 3);
    }

    public static Coordinates generateRandomCoordinates(Coordinates spawnOffset){
        while (true) {
            Coordinates coords = roundedRandomCoords(spawnOffset);

            if (!Coordinates.isBlockOccupied(coords)){
                return coords;
            }
        }
    }

    public double distanceTo(Coordinates other) {
        return Math.sqrt(Math.pow(Math.abs(this.x - other.x), 2) + Math.pow(Math.abs(this.y - other.y), 2));
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates that = (Coordinates) o;
        return getX() == that.getX() && getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    public static Coordinates fromDirectionToCoordinateOnEntity(Entity entity, EnhancedDirection direction, int symmetricOffset){
        switch (direction){
            case LEFTUP: return new Coordinates(entity.getCoords().getX(),entity.getCoords().getY());
            case LEFTDOWN: return new Coordinates(entity.getCoords().getX(),entity.getCoords().getY()+ entity.getSize()-symmetricOffset);
            case RIGHTUP: return new Coordinates(entity.getCoords().getX()+ entity.getSize()-symmetricOffset, entity.getCoords().getY());
            case RIGHTDOWN: return new Coordinates(entity.getSize() + entity.getCoords().getX()-symmetricOffset, entity.getCoords().getY()+ entity.getSize()-symmetricOffset);

        }
        return null;
    }
    public static Coordinates fromDirectionToCoordinateOnEntity(Entity entity, Direction direction,int classSize,int symmetricOffset){



        return fromDirectionToCoordinateOnEntity(entity,direction,0,-classSize/2,symmetricOffset);
    }

    public static Coordinates fromDirectionToCoordinateOnEntity(Entity entity, Direction direction, int inwardOffset, int parallelOffset, int symmetricOffset){
        switch (direction){
            case LEFT:return new Coordinates(entity.getCoords().getX()+inwardOffset,entity.getCoords().getY()+ entity.getSize()/2+parallelOffset);

            case RIGHT:return new Coordinates(entity.getCoords().getX() + entity.getSize()-inwardOffset-symmetricOffset,entity.getCoords().getY()+ entity.getSize()/2+parallelOffset);

            case UP: return new Coordinates(entity.getCoords().getX()+ entity.getSize()/2+parallelOffset,entity.getCoords().getY()+inwardOffset);

            case DOWN: return new Coordinates(entity.getCoords().getX()+ entity.getSize()/2+parallelOffset,entity.getCoords().getY() + entity.getSize()-inwardOffset-symmetricOffset);
        }
        return null;
    }

    public ArrayList<Direction> fromCoordinatesToDirection(Coordinates entityCoords){
        ArrayList<Direction> output = new ArrayList<>();
        entityCoords = roundCoordinates(entityCoords);
        Coordinates mouseCoords = roundCoordinates(this);
        if(mouseCoords.getX()> entityCoords.getX()) output.add(Direction.RIGHT);
        if(mouseCoords.getX()< entityCoords.getX()) output.add(Direction.LEFT);
        if(mouseCoords.getY()>entityCoords.getY()) output.add(Direction.DOWN);
        if(mouseCoords.getY()<entityCoords.getY()) output.add(Direction.UP);
        return output;
    }


    public static Coordinates fromRowAndColumnsToCoordinates(Dimension d){
        return fromRowAndColumnsToCoordinates(d,0,0);
    }

    public static Coordinates fromRowAndColumnsToCoordinates(Dimension d, int offsetX, int offsetY){
        if(offsetX>= GRID_SIZE||offsetY>=GRID_SIZE){
            return null;
        }

        if ((d.getWidth()>= PitchPanel.DIMENSION.getWidth()/ GRID_SIZE)){
            return null;
        }

        if(d.getHeight()>= PitchPanel.DIMENSION.getHeight()/GRID_SIZE){
            return null;
        }

        return new Coordinates((int)d.getWidth()*GRID_SIZE+offsetX, (int)d.getHeight()*GRID_SIZE+offsetY);
    }

    public static Coordinates getCenterCoordinatesOfEntity(Entity e){
        return new Coordinates(e.getCoords().getX()+e.getSize()/2,e.getCoords().getY()+e.getSize()/2);
    }
    public static Entity getEntityOnCoordinates(Coordinates desiredCoords) {
        List<Entity> entities = Coordinates.getEntitiesOnBlock(desiredCoords);
        if (!entities.isEmpty()) return entities.get(0);
        return null;
    }


    public synchronized static List<Entity> getEntitiesOnCoordinates(List<Coordinates> desiredCoords){
        List<Entity> entityLinkedList = new LinkedList<>();
        // Check for each entity if it occupies the specified coordinates
        Set<? extends Entity> entities = new HashSet<>(Bomberman.getMatch().getEntities()) ;
        for (Entity e : entities) {
            for (Coordinates coord : desiredCoords) {
                int entityBottomRightX = e.getCoords().getX() + e.getSize() - 1;
                int entityBottomRightY = e.getCoords().getY() + e.getSize() - 1;

                if (coord.getX() >= e.getCoords().getX()
                        && coord.getX() <= entityBottomRightX
                        && coord.getY() >= e.getCoords().getY()
                        && coord.getY() <= entityBottomRightY
                ) {
                    entityLinkedList.add(e);
                }
            }
        }

        return entityLinkedList;
    }

    /**
     * Gets a list of entities that occupy the specified coordinate.
     * @param nextOccupiedCoords the coordinate to check for occupied entities
     * @return a list of entities that occupy the specified coordinate
     */
    public static List<Entity> getEntitiesOnBlock(Coordinates nextOccupiedCoords) {
        ArrayList<Coordinates> arrayCoordinates = getAllCoordinates(Coordinates.roundCoordinates(nextOccupiedCoords),GRID_SIZE);
        // Get all the blocks and entities in the game
        var entities = Bomberman.getMatch().getEntities();
        return entities.stream().filter(e->arrayCoordinates.stream().anyMatch(coords->doesCollideWith(coords,e))).collect(Collectors.toList());
    }

    /**
     * Checks if the given coordinates collide with the given entity.
     *
     * @param nextOccupiedCoords the coordinates to check for collision
     * @param e     the entity to check for collision with
     * @return true if the given coordinates collide with the given entity, false otherwise
     */
    public static boolean doesCollideWith(Coordinates nextOccupiedCoords, Entity e) {
        return doesCollideWith(nextOccupiedCoords,e.getCoords(), e.getSize());
    }

    private static boolean doesCollideWith(Coordinates nextOccupiedCoords, Coordinates entityCoords,int size) {


        // Get the coordinates of the bottom-right corner of the entity
        int entityBottomRightX = entityCoords.getX() + size - 1;
        int entityBottomRightY = entityCoords.getY() + size - 1;

        // Check if the given coordinates collide with the entity
        return nextOccupiedCoords.getX() >= entityCoords.getX()
                && nextOccupiedCoords.getX() <= entityBottomRightX
                && nextOccupiedCoords.getY() >= entityCoords.getY()
                && nextOccupiedCoords.getY() <= entityBottomRightY;
    }
    public static ArrayList<Coordinates> getAllCoordinates(Coordinates coords, int size){
        int lastX;
        int lastY;
        ArrayList<Coordinates> arrayCoordinates = new ArrayList<>();
        for(int x = 0; x<= size/PitchPanel.COMMON_DIVISOR; x++){
            for(int y = 0; y<= size/PitchPanel.COMMON_DIVISOR; y++){
                lastX = lastY = 0;
                if (x== size/PitchPanel.COMMON_DIVISOR) lastX = PitchPanel.PIXEL_UNIT;
                if (y== size/PitchPanel.COMMON_DIVISOR) lastY = PitchPanel.PIXEL_UNIT;
                arrayCoordinates.add(new Coordinates(coords.getX()+ x*PitchPanel.COMMON_DIVISOR - lastX, coords.getY()+y*PitchPanel.COMMON_DIVISOR-lastY));



            }
        }
        return arrayCoordinates;
    }

    public static boolean isBlockOccupied(Coordinates nextOccupiedCoords){
        return !getEntitiesOnBlock(nextOccupiedCoords).isEmpty();
    }



}
