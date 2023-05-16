package game.ui.panels.game;

import game.Bomberman;
import game.entity.models.Entity;
import game.events.Observer2;
import game.utils.Utility;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

import static game.utils.Utility.loadImage;
import static game.utils.Utility.px;

/**
 * The GamePanel class represents the main game panel that displays the game world and entities
 */
public class PitchPanel extends JPanel implements Observer2 {

    public static final int DEFAULT_PIXEL_UNIT = 6;
    public static final int PIXEL_UNIT = Utility.px(DEFAULT_PIXEL_UNIT);
    //to simplify calculations
    public static final int COMMON_DIVISOR = PIXEL_UNIT * 4;
    public static final int GRID_SIZE = PitchPanel.COMMON_DIVISOR * 3;
    //GRID_SIZE must be multiplied by an odd number in order to guarantee free space around borders on the game pitch
    public static final Dimension DIMENSION = new Dimension(GRID_SIZE*13, 11*GRID_SIZE);

    /**
     * Constructs a new GamePanel with the default dimensions and sets it as the observer for the game ticker observable
     */
    public PitchPanel() {
        // Set preferred, maximum, and minimum size to converted dimensions
        setPreferredSize(DIMENSION);
        setMaximumSize(DIMENSION);
        setMinimumSize(DIMENSION);

        // Set this GamePanel as observer for the game ticker observable
        Bomberman.getMatch().getGameTickerObservable().unregisterAll();
        Bomberman.getMatch().getGameTickerObservable().register(this);

        repaint();
    }

    public Dimension getPanelDimensions() {
        return DIMENSION;
    }

    /**
     * Paints the graphics of the entities in the game world
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        paintComponent(g2d);
        Set<? extends Entity> setEntities = Bomberman.getMatch().getEntities();
        // Draw each entity in the new set

        for (Entity e: setEntities
             ) {
            drawEntity(g2d, e);
        }
    }

    /**
     * Draws an entity on the game panel
     * @param g2d the Graphics2D object to draw with
     * @param e the entity to draw
     */
    private void drawEntity(Graphics2D g2d, Entity e){
        // Draw entity's image at entity's coordinates and size
        if(e.isInvisible()) return;

        String path = e.getImagePath();
        float widthRatio = e.getWidthToHitboxSizeRatio(path);
        float heightRatio = e.getHeightToHitboxSizeRatio(path);
        int paddingWidth = e.getPaddingWidth(widthRatio);
        int paddingHeight = e.getPaddingTop(heightRatio);

        g2d.drawImage(
                e.getImage(),
                e.getCoords().getX() - paddingWidth,
                e.getCoords().getY() - paddingHeight,
                (int) Math.ceil (e.getSize() / widthRatio),
                (int) Math.ceil (e.getSize() / heightRatio),
                this
        );
    }

    /**
     * Repaints the game panel when it is updated by the game ticker observable
     * @param arg the object argument
     */
    @Override
    public void update(Object arg) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = loadImage(Bomberman.getMatch().getCurrentLevel().getGrassBlock());
        g.drawImage(img.getScaledInstance((int) getMaximumSize().getWidth(), (int) getMaximumSize().getHeight(),1), 0, 0, null);
    }
}