package game.entity.enemies.npcs;

import game.utils.Paths;

public class Eagle extends FlyingEnemy {
    @Override
    protected String getBasePath() {
        return Paths.getEnemiesFolder() + "/eagle";
    }

    public String[] getCharacterOrientedImages() {
        return new String[]{
                String.format("%s/eagle_%s_0.png", getBasePath(), imageDirection.toString().toLowerCase()),
                String.format("%s/eagle_%s_1.png", getBasePath(), imageDirection.toString().toLowerCase()),
                String.format("%s/eagle_%s_2.png", getBasePath(), imageDirection.toString().toLowerCase()),
        };
    }
}
