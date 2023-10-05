package game.entity.enemies.npcs;

import game.entity.models.Coordinates;
import game.entity.models.Direction;
import game.ui.panels.game.PitchPanel;
import game.utils.Paths;

import java.util.Arrays;
import java.util.List;

/**
 * An enemy with increased speed multiplier;
 */
public class FastEnemy extends IntelligentEnemy {
    public FastEnemy() {
        super();
        hitboxSizeToHeightRatio = 0.527f;
    }

    public FastEnemy(Coordinates coordinates) {
        super(coordinates);
        hitboxSizeToHeightRatio = 0.527f;
    }

    @Override
    protected String getBasePath() {
        return Paths.getEnemiesFolder() + "/fast_enemy/fast_enemy";
    }

    @Override
    public String[] getCharacterOrientedImages() {
        return new String[]{
                String.format("%s_%s_%d.png", getBasePath(), imageDirection.toString().toLowerCase(), 0),
                String.format("%s_%s_%d.png", getBasePath(), imageDirection.toString().toLowerCase(), 1),
                String.format("%s_%s_%d.png", getBasePath(), imageDirection.toString().toLowerCase(), 2),
                String.format("%s_%s_%d.png", getBasePath(), imageDirection.toString().toLowerCase(), 3),
        };
    }

    @Override
    public float getSpeed() {
        return 1f;
    }

    @Override
    protected List<Direction> getImageDirections() {
        return Arrays.asList(Direction.RIGHT, Direction.LEFT);
    }

    @Override
    public int getSize() {
        return PitchPanel.COMMON_DIVISOR * 2;
    }
}
