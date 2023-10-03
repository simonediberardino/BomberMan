package game.powerups.portal;

import game.entity.models.Coordinates;
import game.ui.panels.game.PitchPanel;

import java.awt.*;

public class World2Portal extends WorldPortal {
    public World2Portal() {
        super(2);
    }

    @Override
    Coordinates getDefaultCoords() {
        return (Coordinates.fromRowAndColumnsToCoordinates(new Dimension(6, 7), 0, -PitchPanel.GRID_SIZE / 2));
    }
}
