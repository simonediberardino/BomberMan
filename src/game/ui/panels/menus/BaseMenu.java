package game.ui.panels.menus;

import game.ui.panels.BombermanFrame;
import game.ui.panels.PagePanel;
import game.ui.viewelements.misc.Space;
import game.utils.Paths;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class BaseMenu extends PagePanel {
    protected JPanel listButtonsPanel;

    public BaseMenu(CardLayout cardLayout, JPanel parent, BombermanFrame frame, String imagePath) {
        super(cardLayout, parent, frame, imagePath);
        setupLayout();
    }

    public BaseMenu(CardLayout cardLayout, JPanel parent, BombermanFrame frame) {
        super(cardLayout, parent, frame, Paths.getMainMenuWallpaper());
        setupLayout();
    }

    /**
     * Sets up the layout of the MenuPanel.
     */
    private void setupLayout() {
        setLayout(new GridBagLayout());

        createListButtonsPanel();
        addButtons();
    }

    private void addButtons() {
        List<JButton> buttons = getButtons();

        for (JButton b : buttons) listButtonsPanel.add(b);
    }

    /**
     * Creates and adds the listButtonsPanel to the MenuPanel.
     */
    private void createListButtonsPanel() {
        listButtonsPanel = new JPanel();
        listButtonsPanel.setLayout(new GridLayout(0, 1));
        listButtonsPanel.setOpaque(false);

        for (int i = 0; i < getButtonsPadding(); i++) {
            listButtonsPanel.add(new Space());
        }

        add(listButtonsPanel);
    }

    protected abstract int getButtonsPadding();

    protected abstract List<JButton> getButtons();

    @Override
    public void onShowCallback() {

    }
}
