package game.ui.panels.menus;

import game.Bomberman;
import game.level.world1.World1Arena;
import game.level.world2.World2Arena;
import game.ui.panels.BombermanFrame;
import game.ui.viewelements.bombermanbutton.RedButton;
import game.ui.viewelements.bombermanbutton.YellowButton;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static game.localization.Localization.*;

public class ArenaMenuPanel extends BaseMenu {
    public ArenaMenuPanel(CardLayout cardLayout, JPanel parent, BombermanFrame frame) {
        super(cardLayout, parent, frame);
    }

    @Override
    protected int getButtonsPadding() {
        return 2;
    }

    @Override
    protected List<JButton> getButtons() {
        return Arrays.asList(createArenaWorld1Button(), createArenaWorld2Button(), createBackButton());
    }

    private JButton createArenaWorld1Button() {
        JButton b = new YellowButton(get(ARENA_WORLD_1));
        b.addActionListener(l -> Bomberman.startLevel(new World1Arena()));
        return b;
    }

    private JButton createArenaWorld2Button() {
        JButton b = new YellowButton(get(ARENA_WORLD_2));
        b.addActionListener(l -> Bomberman.startLevel(new World2Arena()));
        return b;
    }

    private JButton createBackButton() {
        JButton b = new RedButton(get(MAIN_MENU));
        b.addActionListener(l -> Bomberman.showActivity(MainMenuPanel.class));
        return b;
    }

    @Override
    public void onShowCallback() {

    }
}