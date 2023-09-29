package game.ui.panels.game;

import game.Bomberman;
import game.entity.models.Coordinates;
import game.powerups.EmptyPowerup;
import game.powerups.PowerUp;
import game.ui.panels.BombermanFrame;
import game.ui.panels.PagePanel;
import game.values.Dimensions;
import game.utils.Paths;
import game.utils.Utility;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class MatchPanel extends PagePanel implements CustomSoundMode {
    private PitchPanel pitchPanel; // The main panel that contains the game
    private JPanel leftPanel; // Borders of the Game Panel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel rightPanel;
    private JPanel powerUpsPanel;
    private JPanel inventoryPanel;

    public MatchPanel(CardLayout cardLayout, JPanel parent, BombermanFrame frame) {
        super(cardLayout, parent, frame, Paths.getBackgroundImage());
    }

    /**
     * Initializes the game panel which includes the game board with borders and background image.
     * It also sets the layout and adds components to the parent panel.
     */
    public void initGamePanel() {
        createParentPanel(); // Create the parent panel for the game frame
        createGamePanel(); // Create the main game panel that contains the game
        createBorderPanels(); // Create the border panels for the game panel
        addPanelsToGamePanelWithBorders(); // Add the border panels and game panel to the game panel with borders
        addGamePanelWithBordersToParentPanel(); // Add the game panel with borders to the parent panel
        setLayoutAndAddParentPanel(); // Set the layout and add the parent panel to the game frame
        resizeWindowToFitComponents(); // Resize the game frame to fit the components
    }

    private void createParentPanel(){
        setLayout(new BorderLayout()); // Set the layout of the parent panel
        setOpaque(false); // Set the panel to be transparent
    }

    /**
     * Create the main game panel that contains the game.
     */
    private void createGamePanel() {
        pitchPanel = new PitchPanel(); // Create a new GamePanel instance
    }

    /**
     * Create the border panels for the game panel.
     * It creates four panels (left, top, bottom, and right), each with the border image of the game.
     */
    /**

     Creates and sets the border panels to the gamePanelWithBorders.
     Calculates the dimensions and border sizes for the panels.
     Uses createLeftPanel, createTopPanel, createBottomPanel, and createRightPanel methods to create the panels.
     Sets the panels as opaque.
     */
    private void createBorderPanels() {
        // calculate width of left and right border panels
        int widthSides = (int) ((frame.getWidth() - (pitchPanel.getMaximumSize().getWidth())) / 2);

        // calculate height of top and bottom border panels
        int heightNorthSouth = (int) ((frame.getHeight() - (pitchPanel.getMaximumSize().getHeight())) / 2);

        // set the size of the borders
        int borderSize = Utility.px(90);

        // get the images of the border panels from the current level of the game
        Image[] borderImages = Bomberman.getMatch().getCurrentLevel().getBorderImages();

        // create left panel and set the dimensions and the image
        leftPanel = createLeftPanel(
                widthSides,
                (int) pitchPanel.getMaximumSize().getHeight(),
                borderImages[0],
                borderSize
        );

        // create top panel and set the dimensions, the image, and the width of the left panel
        topPanel = createTopPanel(
                (int) pitchPanel.getMaximumSize().getWidth(),
                heightNorthSouth,
                borderImages[3],
                borderSize,
                (int) leftPanel.getPreferredSize().getWidth()
        );

        // create bottom panel and set the dimensions, the image, and the width of the left panel
        bottomPanel = createBottomPanel(
                (int) pitchPanel.getMaximumSize().getWidth(),
                heightNorthSouth,
                borderImages[1],
                borderSize,
                (int) leftPanel.getPreferredSize().getWidth()
        );

        // create right panel and set the dimensions and the image
        rightPanel = createRightPanel(
                widthSides,
                (int) pitchPanel.getMaximumSize().getHeight(),
                borderImages[2],
                borderSize
        );

        // set the border panels as opaque
        leftPanel.setOpaque(false);
        topPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        rightPanel.setOpaque(false);
    }

    /**

     Adds the border panels and the game panel to gamePanelWithBorders using BorderLayout.
     */
    private void addPanelsToGamePanelWithBorders() {
        add(leftPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);
        add(pitchPanel, BorderLayout.CENTER);
    }
    /**

     Adds gamePanelWithBorders to the parent panel using BorderLayout.
     */
    private void addGamePanelWithBordersToParentPanel() {
    }
    /**

     Sets the layout of the main panel to BorderLayout and adds parentPanel to it.
     */
    private void setLayoutAndAddParentPanel() {
        add(pitchPanel, BorderLayout.CENTER);
    }

    /**
     Resizes the window to fit the components.
     */
    private void resizeWindowToFitComponents() {
        frame.pack();
    }

    /**
     * Creates a JPanel for the left side of the game window with an image scaled to the specified border size.
     *
     * @param width      the width of the panel
     * @param height     the height of the panel
     * @param image      the image to be displayed
     * @param borderSize the size of the border for the image
     * @return the JPanel for the left side of the game window
     */
    private JPanel createLeftPanel(int width, int height, Image image, int borderSize) {
        // Create a new JPanel for the left side of the game window
        JPanel leftPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the image scaled to the specified border size on the left side of the panel
                g.drawImage(image.getScaledInstance(borderSize, (int) frame.getPreferredSize().getHeight(), 1), pitchPanel.getX() - borderSize, 0, null);
            }
        };

        inventoryPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Image powerUpsBorder = Utility.loadImage(Paths.getPowerUpsBorderPath()).getScaledInstance(getWidth(), getHeight(), 0);
                g.drawImage(powerUpsBorder, 0, 0, null);
                super.paintComponent(g);
            }
        };

        inventoryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        inventoryPanel.setOpaque(false);
        inventoryPanel.setLayout(new GridLayout(0, 1));

        inventoryPanel.add(Bomberman.getMatch().getInventoryElementControllerPoints().getView());
        inventoryPanel.add(Bomberman.getMatch().getInventoryElementControllerLives().getView());
        inventoryPanel.add(Bomberman.getMatch().getInventoryElementControllerBombs().getView());

        leftPanel.setLayout(new GridBagLayout());
        leftPanel.add(inventoryPanel, new GridBagConstraints());

        // Set the preferred size of the panel to the specified width and height
        leftPanel.setPreferredSize(new Dimension(width, height));
        // Return the JPanel for the left side of the game window
        return leftPanel;
    }

    /**
     * Creates a JPanel for the top of the game window with an image scaled to the specified border size.
     *
     * @param width          the width of the panel
     * @param height         the height of the panel
     * @param image          the image to be displayed
     * @param borderSize     the size of the border for the image
     * @param leftPanelWidth the width of the left panel
     * @return the JPanel for the top of the game window
     */
    private JPanel createTopPanel(int width, int height, Image image, int borderSize, int leftPanelWidth) {
        // Create a new JPanel for the top of the game window
        JPanel topPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the image scaled to the specified border size on the top of the panel
                g.drawImage(image.getScaledInstance(pitchPanel.getWidth() + borderSize * 2, borderSize / 2, 1), leftPanelWidth - borderSize, pitchPanel.getY() - borderSize/2, null);
            }
        };
        // Set the preferred size of the panel to the specified width and height
        topPanel.setPreferredSize(new Dimension(width, height));
        // Return the JPanel for the top of the game window
        return topPanel;
    }

    /**
     * Creates a JPanel for the bottom of the game window with an image scaled to the specified border size.
     *
     * @param width          the width of the panel
     * @param height         the height of the panel
     * @param image          the image to be displayed
     * @param borderSize     the size of the border for the image
     * @param leftPanelWidth the width of the left panel
     * @return the JPanel for the bottom of the game window
     */
    private JPanel createBottomPanel(int width, int height, Image image, int borderSize, int leftPanelWidth) {
        JPanel bottomPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image.getScaledInstance(pitchPanel.getWidth() + borderSize * 2, borderSize / 2, 1), leftPanelWidth - borderSize, 0, null);
            }
        };
        bottomPanel.setPreferredSize(new Dimension(width, height));
        return bottomPanel;
    }

    private JPanel createRightPanel(int width, int height, Image image, int borderSize) {
        // Create a new JPanel for the right side of the game window
        JPanel rightPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(powerUpsPanel != null){
                    int offset = 15;
                    int logoWidth = Utility.px(240);
                    int logoHeight = Utility.px(88);
                    Image powerupsLogo = Utility.loadImage(Paths.getPowerupsLogoPath()).getScaledInstance(logoWidth, logoHeight, 0);
                    g.drawImage(powerupsLogo, powerUpsPanel.getX() - logoWidth / 2 + powerUpsPanel.getWidth()/2, powerUpsPanel.getY() - logoHeight - offset, null);
                }
                // Draw the image scaled to the specified border size on the right side of the panel
                g.drawImage(image.getScaledInstance(borderSize, (int) frame.getPreferredSize().getHeight(), 1), 0, 0, null);
            }
        };

        powerUpsPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Image powerUpsBorder = Utility.loadImage(Paths.getPowerUpsBorderPath()).getScaledInstance(getWidth(), getHeight(), 0);
                g.drawImage(powerUpsBorder, 0, 0, null);
                super.paintComponent(g);
            }
        };

        powerUpsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        powerUpsPanel.setOpaque(false);
        powerUpsPanel.setLayout(new GridLayout(0, 2));

        rightPanel.setLayout(new GridBagLayout());
        rightPanel.add(powerUpsPanel, new GridBagConstraints());
        // Set the preferred size of the panel to the specified width and height
        rightPanel.setPreferredSize(new Dimension(width, height));
        // Return the JPanel for the left side of the game window
        return rightPanel;
    }

    /**
     * Refreshes the power-ups displayed in the power-up panel.
     *
     * @param powerUpList The list of power-up classes to be displayed.
     */
    public void refreshPowerUps(List<Class<? extends PowerUp>> powerUpList) {
        // Create a copy of the power-up list
        final List<PowerUp> powerUpsToShow = powerUpList.stream().map(p -> {
            try {
                return p.getConstructor(Coordinates.class).newInstance(new Coordinates());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                return null;
            }
        }).filter(p -> p != null && p.isDisplayable()).collect(Collectors.toList());

        // Define the dimension of each power-up image
        final int powerUpImageDimension = Dimensions.DEFAULT_INVENTORY_ICON_SIZE;

        // Calculate the number of rows and columns for the power-up panel
        int rows = 0, cols = powerUpList.size() <= 1 ? 1 : 2;

        // Set the layout of the power-up panel
        powerUpsPanel.setLayout(new GridLayout(rows, cols));

        // Remove all existing components from the power-up panel
        powerUpsPanel.removeAll();

        // If there are no power-ups to show, add an EmptyPowerup class as a placeholder
        if (powerUpsToShow.isEmpty())
            powerUpsToShow.add(new EmptyPowerup(new Coordinates()));

        // Iterate over each power-up class
        for (PowerUp p : powerUpsToShow) {
            if(!p.isDisplayable()) continue;

            // Scale the power-up image to the desired dimensions
            Image img = p.getImage().getScaledInstance(powerUpImageDimension, powerUpImageDimension, 0);

            JLabel powerupLabel = new JLabel(new ImageIcon(img));
            powerupLabel.setBorder(BorderFactory.createEmptyBorder(Dimensions.DEFAULT_PADDING, Dimensions.DEFAULT_PADDING, Dimensions.DEFAULT_PADDING, Dimensions.DEFAULT_PADDING));

            // Create a JLabel with the scaled image and add it to the power-up panel
            powerUpsPanel.add(powerupLabel);
        }

        // Update and repaint the right panel to reflect the changes
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public PitchPanel getPitchPanel() {
        return pitchPanel;
    }

    @Override
    public void onShowCallback() {

    }
}
