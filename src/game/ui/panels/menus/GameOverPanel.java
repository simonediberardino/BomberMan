package game.ui.panels.menus;

import game.Bomberman;
import game.data.DataInputOutput;
import game.level.Level;
import game.level.WorldSelectorLevel;
import game.localization.Localization;
import game.ui.viewelements.bombermanbutton.RedButton;
import game.ui.viewelements.misc.ToastHandler;
import game.ui.panels.BombermanFrame;
import game.utils.Paths;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static game.localization.Localization.*;

public class GameOverPanel extends BaseMenu {
    private JButton retryButton;

    /**
     * Constructs a MenuPanel with the specified CardLayout, parent JPanel, and BombermanFrame.
     *
     * @param cardLayout the CardLayout to use
     * @param parent the parent JPanel
     * @param frame the BombermanFrame
     */
    public GameOverPanel(CardLayout cardLayout, JPanel parent, BombermanFrame frame) {
        super(cardLayout, parent, frame, Paths.getDeathWallpaper());
    }

    @Override
    protected List<JButton> getButtons() {
        return Arrays.asList(createStartLevelButton(), createMainMenuButton());
    }

    @Override
    protected int getButtonsPadding() {
        return 1;
    }

    private JButton createStartLevelButton() {
        retryButton = new RedButton("");
        retryButton.addActionListener((v) -> {
            boolean hasLives = DataInputOutput.getLives() > 0;

            ToastHandler.getInstance().cancel();

            Level level = hasLives ? Level.getCurrLevel() : new WorldSelectorLevel();
            Bomberman.startLevel(level);
        });

        updatePlayAgainButtonText();
        return retryButton;
    }

    private JButton createMainMenuButton() {
        JButton mainMenuButton = new RedButton(Localization.get(MAIN_MENU));
        mainMenuButton.addActionListener((v) -> {
            ToastHandler.getInstance().cancel();
            Bomberman.showActivity(MainMenuPanel.class);
        });

        return mainMenuButton;
    }

    private void updatePlayAgainButtonText(){
        boolean hasLives = DataInputOutput.getLives() > 0;
        String text = hasLives ? Localization.get(PLAY_AGAIN) : Localization.get(RESET_WORLD);
        retryButton.setText(text);
    }

    private void showToastMessage() {
        Level lastLevel = Level.getCurrLevel();
        String diedMessage = lastLevel.getDiedMessage();
        ToastHandler.getInstance().show(diedMessage, true);
    }

    @Override
    public void onShowCallback() {
        showToastMessage();
        updatePlayAgainButtonText();
    }
}