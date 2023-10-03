package game.entity.models;

import game.sound.SoundModel;
import game.utils.Paths;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static game.ui.panels.game.PitchPanel.PIXEL_UNIT;

public abstract class MovingEntity extends EntityInteractable {
    public MovingEntity(Coordinates coordinates) {
        super(coordinates);
    }

    public List<Direction> getSupportedDirections() {
        return Arrays.asList(Direction.values());
    }

    /**
     * Gets a list of available directions for the entity to move, based on the current game state.
     * A direction is considered available if moving in that direction would not result in a collision
     * with another entity or an invalid location.
     *
     * @return a list of available directions
     */
    public List<Direction> getAvailableDirections() {
        List<Direction> result = new LinkedList<>();

        // Iterate over each direction
        for (Direction d : Direction.values()) {
            List<Coordinates> newCoordinates = getNewCoordinatesOnDirection(d, PIXEL_UNIT, getSize() / 2);
            // Check if any entities on the next coordinates are blocks or have invalid coordinates
            boolean areCoordinatesValid = Coordinates.getEntitiesOnCoordinates(
                    newCoordinates
            ).stream().noneMatch(this::isObstacle);

            areCoordinatesValid = areCoordinatesValid && newCoordinates.get(0).validate(this); // since the pitch is squared, if a coordinate is valid, then all the other new ones are as well;

            // If all the next coordinates are valid, add this direction to the list of available directions
            if (areCoordinatesValid) {
                result.add(d);
            }
        }

        return result;
    }

    protected SoundModel getStepSound() {
        return null;
    }

}
