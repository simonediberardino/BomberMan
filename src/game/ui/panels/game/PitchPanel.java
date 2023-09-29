package game.ui.panels.game;

import game.BomberManMatch;
import game.Bomberman;
import game.entity.enemies.boss.ghost.GhostBoss;
import game.entity.models.Entity;
import game.events.Observer2;
import game.events.RunnablePar;
import game.sound.AudioManager;
import game.utils.Utility;
import game.values.DrawPriority;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static game.sound.SoundModel.LIGHT_GLITCH;
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
    private final HashMap<String, RunnablePar> graphicsCallbacks = new HashMap<>();
    public volatile Graphics2D g2d;

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
        this.g2d = (Graphics2D) g;

        Image img = loadImage(Bomberman.getMatch().getCurrentLevel().getPitchImagePath());
        g.drawImage(img.getScaledInstance((int) getMaximumSize().getWidth(), (int) getMaximumSize().getHeight(),1), 0, 0, null);

        List<? extends Entity> setEntities = Bomberman.getMatch().getEntitiesCopy();

        for (Entity e : setEntities) {
            try {
                drawEntity(g2d, e);
            }catch (ConcurrentModificationException ex){
                ex.printStackTrace();
            }
        }
        // Runs custom callbacks;
        graphicsCallbacks.forEach((key, value) -> value.execute(g2d));
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
        float widthRatio = e.getHitboxSizeToWidthRatio(path);
        float heightRatio = e.getHitboxSizeToHeightRatio(path);
        int paddingWidth = e.calculateAndGetPaddingWidth(widthRatio);
        int paddingHeight = e.calculateAndGetPaddingTop(heightRatio);

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , e.getAlpha());
        g2d.setComposite(ac);

        g2d.drawImage(
                e.getImage(),
                e.getCoords().getX() - paddingWidth,
                e.getCoords().getY() - paddingHeight,
                (int) Math.ceil (e.getSize() / widthRatio),
                (int) Math.ceil (e.getSize() / heightRatio),
                this
        );
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

    }


    public static void turnOffLights() {
        BomberManMatch match = Bomberman.getMatch();
        if(match == null || !match.getGameState()) return;

        PitchPanel pitchPanel = Bomberman.getBombermanFrame().getPitchPanel();
        AudioManager.getInstance().play(LIGHT_GLITCH);

        pitchPanel.addGraphicsCallback(
                GhostBoss.class.getSimpleName(), new RunnablePar() {
                    @Override
                    public <T> Object execute(T par) {
                        Graphics2D g2d = Bomberman.getBombermanFrame().getPitchPanel().g2d;
                        g2d.setColor(new Color(0,0,0,0.9f));
                        g2d.fillRect(0,0,Bomberman.getBombermanFrame().getHeight(),Bomberman.getBombermanFrame().getWidth());
                        return null;
                    }
                }
        );
    }

    public static void turnOnLights() {
        BomberManMatch match = Bomberman.getMatch();
        if(match == null || !match.getGameState()) return;

        AudioManager.getInstance().play(LIGHT_GLITCH);
        PitchPanel pitchPanel = Bomberman.getBombermanFrame().getPitchPanel();
        pitchPanel.removeGraphicsCallback(GhostBoss.class.getSimpleName());
    }

    public void addGraphicsCallback(String tag, RunnablePar callback){
        graphicsCallbacks.put(tag, callback);
    }

    public void removeGraphicsCallback(String tag){
        graphicsCallbacks.remove(tag);
    }

    public void clearGraphicsCallback() {
        graphicsCallbacks.clear();
    }

    /**
     * Repaints the game panel when it is updated by the game ticker observable
     * @param arg the object argument
     */
    @Override
    public void update(Object arg) {
        repaint();
    }
}