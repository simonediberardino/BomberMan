package game.domain.world.domain.entity.geo;

import game.Bomberman;
import game.domain.world.domain.entity.actors.abstracts.base.Entity;
import game.presentation.ui.panels.game.PitchPanel;

import java.awt.*;
import java.time.temporal.ValueRange;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static game.presentation.ui.panels.game.PitchPanel.GRID_SIZE;

public class Coordinates implements Comparable<Coordinates> {
    private final int x;
    private final int y;

    public Coordinates() {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates plus(Coordinates addend) {
        return new Coordinates(this.getX() + addend.getX(), this.getY() + addend.getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Check whether a coordinate of an Entity is inside the pitch or not;
     *
     * @return true if valid, false otherwise;
     */
    public boolean validate(Entity e) {
        return validate(e.getState().getSize());
    }

    public boolean validate(int size) {
        Dimension gamePanelDimensions = Bomberman.getBombermanFrame().getPitchPanel().getPanelDimensions();


        ValueRange rangeY = ValueRange.of(0, gamePanelDimensions.height - size);
        ValueRange rangeX = ValueRange.of(0, gamePanelDimensions.width - size);

        return (rangeY.isValidValue(getY()) && rangeX.isValidValue(getX()));
    }

    public static Coordinates roundedRandomCoords(Coordinates offset) {
        Dimension dimensions = Bomberman.getBombermanFrame().getPitchPanel().getPanelDimensions();

        return roundCoordinates(new Coordinates(((int) (Math.random() * dimensions.getWidth())), ((int) (Math.random() * dimensions.getHeight()))), offset);
    }

    public static Coordinates roundCoordinates(Coordinates coords) {
        return roundCoordinates(coords, new Coordinates(0, 0));
    }

    public static Coordinates roundCoordinates(Coordinates coords, Coordinates offset) {
        return new Coordinates(((coords.getX() / GRID_SIZE) * GRID_SIZE + offset.getX()), ((coords.getY() / GRID_SIZE) * GRID_SIZE + offset.getY()));
    }

    public static Coordinates generateRandomCoordinates() {
        return generateRandomCoordinates(new Coordinates(0, 0));
    }

    public static Coordinates generateCoordinatesAwayFromPlayer() {
        return generateCoordinatesAwayFrom(Bomberman.getMatch().getPlayer().getInfo().getPosition(), GRID_SIZE * 3);
    }

    public static Coordinates generateCoordinatesAwayFrom(Coordinates other, int offset) {
        Coordinates coord;
        while ((coord = Coordinates.generateRandomCoordinates()).distanceTo(other) < offset) ;
        return coord;
    }


    public static Coordinates randomCoordinatesFromPlayer(int entitySize) {
        return randomCoordinatesFromPlayer(entitySize, GRID_SIZE * 3);
    }

    public static Coordinates randomCoordinatesFromPlayer(int entitySize, int distance) {
        Coordinates c;
        while (true) {
            c = Coordinates.generateCoordinatesAwayFrom(Bomberman.getMatch().getPlayer().getInfo().getPosition(), distance);
            if (c.validate(entitySize)) return c;
        }
    }

    public static Coordinates generateRandomCoordinates(Coordinates spawnOffset, int size) {
        while (true) {
            Coordinates coords = roundedRandomCoords(spawnOffset);

            if (!Coordinates.isBlockOccupied(coords) && coords.validate(size)) {
                return coords;
            }
        }
    }

    public static Coordinates generateRandomCoordinates(Coordinates spawnOffset) {
        return generateRandomCoordinates(spawnOffset, GRID_SIZE);
    }

    public double distanceTo(Coordinates other) {
        return Math.sqrt(Math.pow(Math.abs(this.x - other.x), 2) + Math.pow(Math.abs(this.y - other.y), 2));
    }

    /**
     * Gets the next coordinates in the given direction and with the given step size.
     *
     * @param d        the direction to get the next coordinates in
     * @param stepSize the step size to use
     * @return the next coordinates in the given direction and with the given step size
     */
    public static Coordinates nextCoords(Coordinates coordinates, Direction d, int stepSize) {
        int x = 0;
        int y = 0;

        // Determine the direction to move in
        switch (d) {
            case RIGHT:
                x = stepSize;
                break;
            case LEFT:
                x = -stepSize;
                break;
            case UP:
                y = -stepSize;
                break;
            case DOWN:
                y = stepSize;
                break;
        }

        // Calculate the next coordinates based on the current direction and step size
        return new Coordinates(x + coordinates.getX(), y + coordinates.getY());
    }

    @Override
    public String toString() {
        return "Coordinates{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates that)) return false;
        return getX() == that.getX() && getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    public static Coordinates fromDirectionToCoordinateOnEntity(Entity entity, EnhancedDirection direction, int symmetricOffset) {
        return switch (direction) {
            case LEFTUP ->
                    new Coordinates(entity.getInfo().getPosition().getX(), entity.getInfo().getPosition().getY());
            case LEFTDOWN ->
                    new Coordinates(entity.getInfo().getPosition().getX(), entity.getInfo().getPosition().getY() + entity.getState().getSize() - symmetricOffset);
            case RIGHTUP ->
                    new Coordinates(entity.getInfo().getPosition().getX() + entity.getState().getSize() - symmetricOffset, entity.getInfo().getPosition().getY());
            case RIGHTDOWN ->
                    new Coordinates(entity.getState().getSize() + entity.getInfo().getPosition().getX() - symmetricOffset, entity.getInfo().getPosition().getY() + entity.getState().getSize() - symmetricOffset);
        };
    }

    public static Coordinates fromDirectionToCoordinateOnEntity(Entity entity, Direction direction, int classSize, int symmetricOffset) {


        return fromDirectionToCoordinateOnEntity(entity, direction, 0, -classSize / 2, symmetricOffset);
    }

    public static Coordinates fromDirectionToCoordinateOnEntity(Entity entity, Direction direction, int inwardOffset, int parallelOffset, int symmetricOffset) {
        return switch (direction) {
            case LEFT ->
                    new Coordinates(entity.getInfo().getPosition().getX() + inwardOffset, entity.getInfo().getPosition().getY() + entity.getState().getSize() / 2 + parallelOffset);
            case RIGHT ->
                    new Coordinates(entity.getInfo().getPosition().getX() + entity.getState().getSize() - inwardOffset - symmetricOffset, entity.getInfo().getPosition().getY() + entity.getState().getSize() / 2 + parallelOffset);
            case UP ->
                    new Coordinates(entity.getInfo().getPosition().getX() + entity.getState().getSize() / 2 + parallelOffset, entity.getInfo().getPosition().getY() + inwardOffset);
            case DOWN ->
                    new Coordinates(entity.getInfo().getPosition().getX() + entity.getState().getSize() / 2 + parallelOffset, entity.getInfo().getPosition().getY() + entity.getState().getSize() - inwardOffset - symmetricOffset);
        };
    }

    public ArrayList<Direction> fromCoordinatesToDirection(Coordinates entityCoords) {
        ArrayList<Direction> output = new ArrayList<>();
        entityCoords = roundCoordinates(entityCoords);
        Coordinates mouseCoords = roundCoordinates(this);
        if (mouseCoords.getX() > entityCoords.getX()) output.add(Direction.RIGHT);
        if (mouseCoords.getX() < entityCoords.getX()) output.add(Direction.LEFT);
        if (mouseCoords.getY() > entityCoords.getY()) output.add(Direction.DOWN);
        if (mouseCoords.getY() < entityCoords.getY()) output.add(Direction.UP);
        return output;
    }


    public static Coordinates fromRowAndColumnsToCoordinates(Dimension d) {
        return fromRowAndColumnsToCoordinates(d, 0, 0);
    }

    public static Coordinates fromRowAndColumnsToCoordinates(Dimension d, int offsetX, int offsetY) {
        if (offsetX >= GRID_SIZE || offsetY >= GRID_SIZE) {
            return null;
        }

        if ((d.getWidth() >= PitchPanel.DIMENSION.getWidth() / GRID_SIZE)) {
            return null;
        }

        if (d.getHeight() >= PitchPanel.DIMENSION.getHeight() / GRID_SIZE) {
            return null;
        }

        return new Coordinates((int) d.getWidth() * GRID_SIZE + offsetX, (int) d.getHeight() * GRID_SIZE + offsetY);
    }

    public static Coordinates getCenterCoordinatesOfEntity(Entity e) {
        return new Coordinates(e.getInfo().getPosition().getX() + e.getState().getSize() / 2, e.getInfo().getPosition().getY() + e.getState().getSize() / 2);
    }

    public static Entity getEntityOnCoordinates(Coordinates desiredCoords) {
        List<Entity> entities = Coordinates.getEntitiesOnBlock(desiredCoords);
        if (!entities.isEmpty()) return entities.get(0);
        return null;
    }


    public static List<Entity> getEntitiesOnCoordinates(List<Coordinates> desiredCoords) {
        List<Entity> entityLinkedList = new LinkedList<>();
        // Check for each entity if it occupies the specified coordinates
        Collection<Entity> entities = Bomberman.getMatch().getEntities();

        entities.parallelStream().forEach(e -> {
            for (Coordinates coord : desiredCoords) {
                int entityBottomRightX = e.getInfo().getPosition().getX() + e.getState().getSize() - 1;
                int entityBottomRightY = e.getInfo().getPosition().getY() + e.getState().getSize() - 1;

                if (coord.getX() >= e.getInfo().getPosition().getX() && coord.getX() <= entityBottomRightX && coord.getY() >= e.getInfo().getPosition().getY() && coord.getY() <= entityBottomRightY) {
                    entityLinkedList.add(e);
                }
            }
        });

        return entityLinkedList;
    }


    // returns a list of coordinates a certain number of steps away from the entity in a given direction, taking into account entity size
    public static List<Coordinates> getNewCoordinatesOnDirection(Coordinates position, Direction d, int steps, int offset, int size) {
        return switch (d) {
            case RIGHT ->
                    getNewCoordinatesOnRight(position, steps, offset, size); // get coordinates to the right of entity
            case LEFT ->
                    getNewCoordinatesOnLeft(position, steps, offset, size); // get coordinates to the left of entity
            case UP -> getNewCoordinatesOnUp(position, steps, offset, size); // get coordinates above entity
            case DOWN -> getNewCoordinatesOnDown(position, steps, offset, size); // get coordinates below entity
        };
    }

    protected static List<Coordinates> getNewCoordinatesOnRight(Coordinates position, int steps, int offset, int size) {
        List<Coordinates> coordinates = new ArrayList<>();
        int last = 0;
        for (int step = 0; step <= steps / offset; step++) {
            for (int i = 0; i <= size / offset; i++) {
                if (i == size / offset) last = PitchPanel.PIXEL_UNIT;

                coordinates.add(new Coordinates(position.getX() + size + step * offset, position.getY() + i * offset - last));
            }
        }
        return coordinates;
    }

    protected static List<Coordinates> getNewCoordinatesOnLeft(Coordinates position, int steps, int offset, int size) {
        List<Coordinates> coordinates = new ArrayList<>();
        int first = steps;
        int last = 0;
        for (int step = 0; step <= steps / offset; step++) {
            for (int i = 0; i <= size / offset; i++) {
                if (i == size / offset) last = PitchPanel.PIXEL_UNIT;
                coordinates.add(new Coordinates(position.getX() - first - step * offset, position.getY() + i * offset - last));
            }
            first = 0;
        }
        return coordinates;
    }

    protected static List<Coordinates> getNewCoordinatesOnUp(Coordinates position, int steps, int offset, int size) {
        List<Coordinates> coordinates = new ArrayList<>();
        int first = steps, last = 0;

        for (int step = 0; step <= steps / offset; step++) {
            for (int i = 0; i <= size / offset; i++) {
                if (i == size / offset) last = PitchPanel.PIXEL_UNIT;
                coordinates.add(new Coordinates(position.getX() + i * offset - last, position.getY() - first - step * offset));
            }
            first = 0;
        }

        return coordinates;
    }

    protected static List<Coordinates> getNewCoordinatesOnDown(Coordinates position, int steps, int offset, int size) {
        List<Coordinates> coordinates = new ArrayList<>();
        int first = steps, last = 0;

        for (int step = 0; step <= steps / offset; step++) {
            for (int i = 0; i <= size / offset; i++) {
                if (i == size / offset) last = PitchPanel.PIXEL_UNIT;

                coordinates.add(new Coordinates(position.getX() + i * offset - last, position.getY() + size - 1 + first + step * offset));
            }
            first = 0;
        }

        return coordinates;
    }

    /**
     * Gets a list of entities that occupy the specified coordinate.
     *
     * @param nextOccupiedCoords the coordinate to check for occupied entities
     * @return a list of entities that occupy the specified coordinate
     */
    public static List<Entity> getEntitiesOnBlock(Coordinates nextOccupiedCoords) {
        if (nextOccupiedCoords == null)
            return Collections.emptyList();

        ArrayList<Coordinates> arrayCoordinates = getAllCoordinates(Coordinates.roundCoordinates(nextOccupiedCoords), GRID_SIZE);
        // Get all the blocks and entities in the game
        var entities = Bomberman.getMatch().getEntities();
        return entities.parallelStream().filter(e -> arrayCoordinates.stream().anyMatch(coords -> doesCollideWith(coords, e))).collect(Collectors.toList());
    }

    /**
     * Checks if the given coordinates collide with the given entity.
     *
     * @param nextOccupiedCoords the coordinates to check for collision
     * @param e                  the entity to check for collision with
     * @return true if the given coordinates collide with the given entity, false otherwise
     */
    public static boolean doesCollideWith(Coordinates nextOccupiedCoords, Entity e) {
        return doesCollideWith(nextOccupiedCoords, e.getInfo().getPosition(), e.getState().getSize());
    }

    private static boolean doesCollideWith(Coordinates nextOccupiedCoords, Coordinates entityCoords, int size) {


        // Get the coordinates of the bottom-right corner of the entity
        int entityBottomRightX = entityCoords.getX() + size - 1;
        int entityBottomRightY = entityCoords.getY() + size - 1;

        // Check if the given coordinates collide with the entity
        return nextOccupiedCoords.getX() >= entityCoords.getX() && nextOccupiedCoords.getX() <= entityBottomRightX && nextOccupiedCoords.getY() >= entityCoords.getY() && nextOccupiedCoords.getY() <= entityBottomRightY;
    }

    // calculates the coordinates of a point a certain distance away from the entity's top-left corner in a given direction
    public static Coordinates getNewTopLeftCoordinatesOnDirection(Coordinates coordinates, Direction d, int distance) {
        int sign = switch (d) {
            case UP, LEFT -> -1; // if direction is up or left, sign is negative
            case DOWN, RIGHT -> 1; // if direction is down or right, sign is positive
        };

        return switch (d) {
            case LEFT, RIGHT ->
                    new Coordinates(coordinates.getX() + distance * sign, coordinates.getY()); // calculate new x-coordinate based on direction and distance

            case DOWN, UP ->
                    new Coordinates(coordinates.getX(), coordinates.getY() + distance * sign); // calculate new y-coordinate based on direction and distance
        };

    }

    public static ArrayList<Coordinates> getAllCoordinates(Coordinates coords, int size) {
        int lastX;
        int lastY;
        ArrayList<Coordinates> arrayCoordinates = new ArrayList<>();
        for (int x = 0; x <= size / PitchPanel.COMMON_DIVISOR; x++) {
            for (int y = 0; y <= size / PitchPanel.COMMON_DIVISOR; y++) {
                lastX = lastY = 0;
                if (x == size / PitchPanel.COMMON_DIVISOR)
                    lastX = PitchPanel.PIXEL_UNIT;

                if (y == size / PitchPanel.COMMON_DIVISOR)
                    lastY = PitchPanel.PIXEL_UNIT;
                arrayCoordinates.add(new Coordinates(coords.getX() + x * PitchPanel.COMMON_DIVISOR - lastX, coords.getY() + y * PitchPanel.COMMON_DIVISOR - lastY));
            }
        }
        return arrayCoordinates;
    }

    public static ArrayList<Coordinates> getAllBlocksInArea(Coordinates topLeft, Coordinates bottomRight) {
        if (topLeft.compareTo(bottomRight) > 0) {
            return null;
        }

        ArrayList<Coordinates> output = new ArrayList<>();
        topLeft = Coordinates.roundCoordinates(topLeft);
        bottomRight = Coordinates.roundCoordinates(bottomRight);

        for (int x = topLeft.getX(); x <= bottomRight.getX(); x += PitchPanel.GRID_SIZE) {
            for (int y = topLeft.getY(); y <= bottomRight.getY(); y += PitchPanel.GRID_SIZE) {
                output.add(Coordinates.roundCoordinates(new Coordinates(x, y)));
            }
        }

        return output;
    }

    public static boolean isBlockOccupied(Coordinates nextOccupiedCoords) {
        return !getEntitiesOnBlock(nextOccupiedCoords).isEmpty();
    }

    public static List<Coordinates> getAllBlocksInAreaFromDirection(Entity e, Direction d, int depth) {
        return switch (d) {
            case LEFT ->
                    getAllBlocksInArea(e.getInfo().getPosition().plus(new Coordinates(-GRID_SIZE * depth + 1, 0)), e.getInfo().getPosition().plus(new Coordinates(1, e.getState().getSize() - 1)));
            case DOWN ->
                    Coordinates.getAllBlocksInArea(e.getInfo().getPosition().plus(new Coordinates(0, e.getState().getSize())), e.getInfo().getPosition().plus(new Coordinates(e.getState().getSize() - 1, e.getState().getSize() + PitchPanel.GRID_SIZE * depth - 1)));
            case UP ->
                    getAllBlocksInArea(e.getInfo().getPosition().plus(new Coordinates(-1, -(PitchPanel.GRID_SIZE * depth - 1))), e.getInfo().getPosition().plus(new Coordinates(e.getState().getSize() - 1, -1)));
            case RIGHT ->
                    getAllBlocksInArea(e.getInfo().getPosition().plus(new Coordinates(e.getState().getSize(), 0)), e.getInfo().getPosition().plus(new Coordinates(e.getState().getSize() + GRID_SIZE * depth - 1, e.getState().getSize() - 1)));
        };
    }

    public static int roundIntToGridSize(int p) {
        return p / GRID_SIZE * GRID_SIZE;
    }

    @Override
    public int compareTo(Coordinates o) {
        return Comparator.comparing(Coordinates::getY).thenComparing(Coordinates::getX).compare(this, o);

    }

    public static Coordinates roundCoordinatesToBottom(Coordinates coords, int entitySize) {
        return new Coordinates(coords.getX(), roundIntToGridSize(coords.getY()) - entitySize + GRID_SIZE);
    }

}