package game.level;

import game.Bomberman;
import game.data.DataInputOutput;
import game.entity.Player;
import game.entity.blocks.InvisibleBlock;
import game.entity.enemies.boss.Boss;
import game.entity.models.Enemy;
import game.localization.Localization;
import game.entity.models.Coordinates;
import game.powerups.portal.Portal;
import game.powerups.portal.World1Portal;
import game.powerups.portal.World2Portal;
import game.powerups.portal.WorldPortal;
import game.ui.panels.game.PitchPanel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static game.localization.Localization.ISLAND;

public class WorldSelectorLevel extends StoryLevel{
    public final static HashMap<Integer, Class<? extends WorldPortal>> WORLDS_ID_TO_PORTAL = new HashMap<>() {{
        put(1, World1Portal.class);
        put(2, World2Portal.class);
    }};

    @Override
    public int getWorldId() {
        return 0;
    }

    @Override
    public int getLevelId() {
        return 0;
    }

    @Override
    public int startEnemiesCount() {
        return 0;
    }

    @Override
    public int getMaxDestroyableBlocks() {
        return 0;
    }


    @Override
    public Class<? extends Level> getNextLevel() {
        return null;
    }

    @Override
    public Class<? extends Enemy>[] availableEnemies() {
        return new Class[0];
    }


    @Override
    public Boss getBoss() {
        return null;
    }

    @Override
    public void spawnEnemies() {
    }

    public void generateInvisibleBlock(){
        //BORDER INVISIBLE BLOCKS COLUMNS
        for (int i = 0; i< PitchPanel.DIMENSION.getWidth()/PitchPanel.GRID_SIZE-1; i++){
            new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(i,0))).spawn();
            new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(i,(int)(PitchPanel.DIMENSION.getHeight()/PitchPanel.GRID_SIZE-1)))).spawn();
        }
        //ROWS
        for (int i = 0; i< PitchPanel.DIMENSION.getHeight()/PitchPanel.GRID_SIZE-1; i++){
            new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension( 0,i))).spawn();
            new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension((int)(PitchPanel.DIMENSION.getWidth()/PitchPanel.GRID_SIZE)-1,i))).spawn();

        }


        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(1,1),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(2,1),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(3,1),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(4,1),-PitchPanel.GRID_SIZE/6,0)).spawn(true,false);

        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(6,1),-PitchPanel.GRID_SIZE/3,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(11,1),-PitchPanel.GRID_SIZE/3,-PitchPanel.GRID_SIZE/2)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(1,7),+PitchPanel.GRID_SIZE/2,-PitchPanel.GRID_SIZE/2)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(2,9),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(2,8),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(8,10),0,-PitchPanel.GRID_SIZE/2)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(9,9),-PitchPanel.GRID_SIZE/3,-PitchPanel.GRID_SIZE/6)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(9,8),PitchPanel.GRID_SIZE/6,-PitchPanel.GRID_SIZE/6)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(9,7),PitchPanel.GRID_SIZE/6,-PitchPanel.GRID_SIZE/6)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(10,6),PitchPanel.GRID_SIZE*2/3,-PitchPanel.GRID_SIZE*2/3)).spawn(true,false);


        //OUT OF BOUNDS BARRIER BLOCKS
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(10,9),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(10,8),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(11,9),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(11,8),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(10,7),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(11,7),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(11,7),0,-PitchPanel.GRID_SIZE/2)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(10,7),0,-PitchPanel.GRID_SIZE/2)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(9,7),PitchPanel.GRID_SIZE/6,-PitchPanel.GRID_SIZE/2)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(1,8),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(1,9),0,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(1,2),PitchPanel.GRID_SIZE/3,-PitchPanel.GRID_SIZE/3)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(3,6),PitchPanel.GRID_SIZE/6,+PitchPanel.GRID_SIZE/3)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(7,4),PitchPanel.GRID_SIZE/6,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(7,5),PitchPanel.GRID_SIZE/6,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(8,5),-PitchPanel.GRID_SIZE/2,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(8,4),-PitchPanel.GRID_SIZE/2,0)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(11,3),-PitchPanel.GRID_SIZE/2,+PitchPanel.GRID_SIZE/3)).spawn(true,false);
        new InvisibleBlock(Coordinates.fromRowAndColumnsToCoordinates(new Dimension(10,2),-PitchPanel.GRID_SIZE/6,+PitchPanel.GRID_SIZE/2)).spawn(true,false);
    }

    @Override
    public void generateEntities(JPanel jPanel){
        generateInvisibleBlock();
        generatePlayer();
        generatePortals();
    }

    private void generatePortals() {
        int lastWorldId = Math.max(1, DataInputOutput.getPlayerDataObject().getLastWorldId());

        List<Class<? extends WorldPortal>> worldPortals = WORLDS_ID_TO_PORTAL.entrySet()
                .stream().filter(x->x.getKey() <= lastWorldId)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        worldPortals.forEach(e -> {
            try {
                Portal p = e.getConstructor().newInstance();
                p.spawn(true, false);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException instantiationException) {
                instantiationException.printStackTrace();
            }
        });
    }

    @Override
    public Coordinates getPlayerSpawnCoordinates() {
        return Coordinates.fromRowAndColumnsToCoordinates(new Dimension(5, 2),0,0);
    }

    // This method returns the maximum number of bombs that a player can have at one time.
    public int getMaxBombs() {
        return 0;
    }

    @Override
    public String toString() {
        return Localization.get(ISLAND);
    }

}
