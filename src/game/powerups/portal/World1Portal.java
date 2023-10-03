package game.powerups.portal;

import game.entity.models.Coordinates;
import game.ui.panels.game.PitchPanel;

import java.awt.*;

public class World1Portal extends WorldPortal {
    public World1Portal() {
        super(null, 1);
    }

    @Override
    Coordinates getDefaultCoords() {
        return (Coordinates.fromRowAndColumnsToCoordinates(new Dimension(3, 4), 0, -PitchPanel.GRID_SIZE / 2));
    }
}
