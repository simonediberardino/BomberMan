package game.ui.panels.menus;

import game.Bomberman;
import game.level.WorldSelectorLevel;
import game.level.world1.World1Level5;
import game.level.world2.World2Level3;
import game.level.world2.World2Level4;
import game.level.world2.World2Level5;
import game.localization.Localization;
import game.ui.panels.settings.ProfilePanel;
import game.ui.panels.settings.SettingsPanel;
import game.ui.viewelements.bombermanbutton.BombermanButton;
import game.ui.viewelements.bombermanbutton.YellowButton;
import game.ui.panels.BombermanFrame;
import game.utils.Paths;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static game.localization.Localization.*;

/**
 * The MenuPanel class represents the main menu screen of the game.
 */
public class MainMenuPanel extends BaseMenu {
    /**
     * Constructs a MenuPanel with the specified CardLayout, parent JPanel, and BombermanFrame.
     *
     * @param cardLayout the CardLayout to use
     * @param parent the parent JPanel
     * @param frame the BombermanFrame
     */
    public MainMenuPanel(CardLayout cardLayout, JPanel parent, BombermanFrame frame) {
        super(cardLayout, parent, frame);
    }

    @Override
    protected int getButtonsPadding() {
        return 2;
    }

    @Override
    protected List<JButton> getButtons() {
        return Arrays.asList(createStartLevelButton(), createProfileButton(), createSettingsButton(), createQuitButton());
    }

    /**
     * Creates the startLevelButton and adds it to the listButtonsPanel.
     */
    private JButton createStartLevelButton() {
        JButton startLevelButton = new YellowButton(get(PLAY));
        startLevelButton.addActionListener((v) -> Bomberman.startLevel(new WorldSelectorLevel()));
        return startLevelButton;
    }

    /**
     * Creates the profileButton and adds it to the listButtonsPanel.
     */
    private JButton createProfileButton() {
        JButton profileButton = new YellowButton(get(PROFILE));
        profileButton.addActionListener(l -> Bomberman.show(ProfilePanel.class));
        return profileButton;
    }

    private JButton createSettingsButton() {
        JButton settingsButton = new YellowButton(get(SETTINGS));
        settingsButton.addActionListener(l -> Bomberman.show(SettingsPanel.class));
        return settingsButton;
    }

    /**
     * Creates the exitButton and adds it to the listButtonsPanel.
     */
    private JButton createQuitButton() {
        JButton exitButton = new YellowButton(get(QUIT));
        exitButton.addActionListener(v -> System.exit(0));
        return exitButton;
    }

    @Override
    public void onShowCallback() {

    }
}