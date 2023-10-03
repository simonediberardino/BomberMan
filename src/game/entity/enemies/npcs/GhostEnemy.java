package game.entity.enemies.npcs;

import game.entity.enemies.npcs.IntelligentEnemy;
import game.entity.models.Entity;
import game.entity.models.Direction;
import game.ui.panels.game.PitchPanel;
import game.utils.Paths;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GhostEnemy extends IntelligentEnemy {
    public GhostEnemy() {
        hitboxSizetoWidthRatio = 0.837f;
        hitboxSizeToHeightRatio = 1;
    }
    @Override
    public String[] getCharacterOrientedImages() {
        return new String[]{String.format("%s/mini_ghost/ghost_%s.png", Paths.getEnemiesFolder(), imageDirection.toString().toLowerCase())};
    }

    @Override
    protected List<Direction> getImageDirections() {
        return Arrays.asList(Direction.RIGHT, Direction.LEFT);
    }

    @Override
    public Set<Class<? extends Entity>> getObstacles() {
        return new HashSet<>();
    }

    @Override
    public int getSize() {
        return PitchPanel.COMMON_DIVISOR * 2;
    }
}
