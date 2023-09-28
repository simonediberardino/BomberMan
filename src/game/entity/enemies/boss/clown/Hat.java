package game.entity.enemies.boss.clown;

import game.entity.Player;
import game.entity.enemies.npcs.Orb;
import game.entity.models.Entity;
import game.entity.models.Coordinates;
import game.entity.models.Direction;
import game.entity.models.EnhancedDirection;
import game.utils.Paths;
import game.values.DrawPriority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Hat extends Orb {
    @Override
    protected String getBasePath() {
        return Paths.getEnemiesFolder() + "/clown/hat";
    }

    @Override
    public String[] getCharacterOrientedImages() {
        return new String[]{
                getBasePath() + "1.png",
                getBasePath() + "2.png",
                getBasePath() + "3.png",
                getBasePath() + "4.png",
                getBasePath() + "5.png",
                getBasePath() + "6.png",
                getBasePath() + "7.png",
                getBasePath() + "8.png",
                getBasePath() + "9.png",
                getBasePath() + "10.png",
        };
    }

    public Hat(Coordinates coordinates, EnhancedDirection enhancedDirection) {
        super(coordinates, enhancedDirection);
        setMaxHp(300);
        setHp(getMaxHp());
    }

    @Override
    public int getSize() {
        return SIZE * 3;
    }

    @Override
    protected void doInteract(Entity e) {
        if(e == null) return;

        if(e instanceof Clown) {
            ((Clown) e).setHasHat(true);
            despawn();
        } else {
            attack(e);
        }
    }

    @Override
    protected boolean useOnlyBaseIcons() {
        return true;
    }

    protected void updateDirection() {
        if(!canMove || !isAlive) return;

        if (enhancedDirection == null) {
            // When hitting a wall, bounce and change direction;
            if(!moveOrInteract(direction))
                direction = direction.opposite();

            updateLastDirection(direction);

            return;
        }

        for (Direction d : enhancedDirection.toDirection()) {
            currDirection = d;
            if (!moveOrInteract(d)) {
                enhancedDirection = enhancedDirection.opposite(d);
            }

            updateLastDirection(currDirection);
        }
    }

    @Override
    public Set<Class<? extends Entity>> getInteractionsEntities(){
        return new HashSet<>(Arrays.asList(Player.class, Clown.class));
    }

    @Override
    public Set<Class<? extends Entity>> getObstacles(){
        return new HashSet<>();
    }

    @Override
    public void doUpdate(boolean gameState) {
        if(gameState) updateDirection();
    }

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.DRAW_PRIORITY_3;
    }
}
