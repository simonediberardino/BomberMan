package game.ui.panels.menus;

import game.Bomberman;
import game.ui.panels.BombermanFrame;
import game.ui.viewelements.bombermanbutton.YellowButton;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static game.localization.Localization.*;

public class PausePanel extends BaseMenu {
    public PausePanel(CardLayout cardLayout, JPanel parent, BombermanFrame frame) {
        super(cardLayout, parent, frame);
    }

    @Override
    protected int getButtonsPadding() {
        return 1;
    }

    @Override
    protected List<JButton> getButtons() {
        return Arrays.asList(createResumeButton(), createQuitButton());
    }

    private JButton createResumeButton() {
        JButton profileButton = new YellowButton(get(RESUME_GAME));
        profileButton.addActionListener(l -> Bomberman.getMatch().toggleGameState());
        return profileButton;
    }

    private JButton createQuitButton() {
        JButton exitButton = new YellowButton(get(QUIT_GAME));
        exitButton.addActionListener(v -> Bomberman.quitMatch());
        return exitButton;
    }

    @Override
    public void onShowCallback() {

    }
}
