package game.entity.enemies.npcs;

import game.entity.models.Coordinates;
import game.utils.Paths;

public class Zombie extends IntelligentEnemy {
    public Zombie() {
        super();
        setMaxHp(300);
        setHp(getMaxHp());
    }

    public Zombie(Coordinates coordinates) {
        super(coordinates);
        setMaxHp(300);
        setHp(getMaxHp());
    }

    @Override
    public float getSpeed() {
        return 0.5f;
    }

    @Override
    protected String getBasePath() {
        return Paths.getEnemiesFolder() + "/zombie";
    }

    @Override
    public String[] getCharacterOrientedImages() {
        return new String[]{
                String.format("%s/zombie_%s_0.png", getBasePath(), imageDirection.toString().toLowerCase()),
                String.format("%s/zombie_%s_1.png", getBasePath(), imageDirection.toString().toLowerCase()),
                String.format("%s/zombie_%s_2.png", getBasePath(), imageDirection.toString().toLowerCase()),
                String.format("%s/zombie_%s_3.png", getBasePath(), imageDirection.toString().toLowerCase())
        };
    }
}
