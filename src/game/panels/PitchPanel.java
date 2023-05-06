package game.panels;

import game.BomberMan;
import game.entity.Particle;
import game.entity.models.Character;
import game.entity.models.Entity;
import game.entity.models.EntityInteractable;
import game.utils.Utility;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static game.utils.Utility.loadImage;
import static game.utils.Utility.px;

/**
 * The GamePanel class represents the main game panel that displays the game world and entities
 */
public class PitchPanel extends JPanel implements Observer {
    public static final Dimension DEFAULT_DIMENSION = new Dimension(945, 800);
    public static final int DEFAULT_PIXEL_UNIT = 6;
    public static final int PIXEL_UNIT = Utility.px(DEFAULT_PIXEL_UNIT);
    public static final int COMMON_DIVISOR = PIXEL_UNIT*2;
    public static final int GRID_SIZE = PitchPanel.COMMON_DIVISOR * 6;
    public static final int OFFSET_ON_CHECK = GRID_SIZE/2;
    private Dimension panelDimensions;

    /**
     * Constructs a new GamePanel with the default dimensions and sets it as the observer for the game ticker observable
     */
    public PitchPanel() {
        // Convert default dimensions to screen size
        int convertedHeight = Utility.px((int) DEFAULT_DIMENSION.getHeight());
        convertedHeight = convertedHeight/(GRID_SIZE *2)*(GRID_SIZE*2)+GRID_SIZE;
        int convertedWidth = Utility.px((int) DEFAULT_DIMENSION.getWidth());
        convertedWidth = convertedWidth/(GRID_SIZE *2)*(GRID_SIZE*2)+GRID_SIZE;

        panelDimensions = new Dimension(convertedWidth, convertedHeight);
        // Set preferred, maximum, and minimum size to converted dimensions
        setPreferredSize(panelDimensions);
        setMaximumSize(panelDimensions);
        setMinimumSize(panelDimensions);

        // Set this GamePanel as observer for the game ticker observable
        BomberMan.getInstance().getGameTickerObservable().deleteObservers();
        BomberMan.getInstance().getGameTickerObservable().addObserver(this);

        repaint();
    }

    public Dimension getPanelDimensions() {
        return panelDimensions;
    }

    /**
     * Paints the graphics of the entities in the game world
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        paintComponent(g2d);
        Set<? extends Entity> setParticles = BomberMan.getInstance().getParticles();
        Set<? extends EntityInteractable> setEntities = BomberMan.getInstance().getEntities();
        Set<? extends Entity> setBlocks = BomberMan.getInstance().getStaticEntities();
        // Draw each entity in the new set

        setBlocks.parallelStream().forEach((it) -> drawEntity(g2d, it));
        setEntities.parallelStream().forEach((it) -> drawEntity(g2d, it));
        setParticles.parallelStream().forEach((it) -> drawEntity(g2d, it));
    }

    /**
     * Draws an entity on the game panel
     * @param g2d the Graphics2D object to draw with
     * @param e the entity to draw
     */
    private void drawEntity(Graphics2D g2d, Entity e){
        // Draw entity's image at entity's coordinates and size
        int y = 0;
        if (e instanceof Character)
            y = Character.PADDING_HEAD;

        g2d.drawImage(e.getImage(), e.getCoords().getX(), e.getCoords().getY() - y, e.getSize(), (int) Math.ceil (e.getSize()/e.getImageRatio()), this);
    }

    /**
     * Repaints the game panel when it is updated by the game ticker observable
     * @param o the observable object
     * @param arg the object argument
     */
    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = loadImage(BomberMan.getInstance().getCurrentLevel().getGrassBlock());
        g.drawImage(img.getScaledInstance((int) getMaximumSize().getWidth(), (int) getMaximumSize().getHeight(),1), 0, 0, null);
    }
}