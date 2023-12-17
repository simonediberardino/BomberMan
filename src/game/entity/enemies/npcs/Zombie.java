package game.entity.enemies.npcs;

import game.entity.models.Coordinates;
import game.utils.Paths;

public class Zombie extends IntelligentEnemy {
    public Zombie() {
        super();
        setMaxHp(300);
    }

    public Zombie(Coordinates coordinates) {
        super(coordinates);
        setMaxHp(300);
    }

    @Override
    public float getSpeed() {
        return 0.5f;
    }

    @Override
    protected String getBasePath() {
        return Paths.INSTANCE.getEntitiesFolder() + "/zombie";
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
