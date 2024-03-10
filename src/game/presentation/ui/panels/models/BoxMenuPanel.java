package game.presentation.ui.panels.models;

import game.presentation.ui.frames.BombermanFrame;
import game.presentation.ui.panels.game.PagePanel;
import game.utils.file_system.Paths;

import javax.swing.*;
import java.awt.*;

public abstract class BoxMenuPanel extends PagePanel {
    protected JBombermanBoxContainerPanel boxComponentsPanel;
    protected final String title;

    public BoxMenuPanel(CardLayout cardLayout, JPanel parent, BombermanFrame frame, String title) {
        super(cardLayout, parent, frame, Paths.getBackgroundImage());
        this.title = title;
        initializeLayout();
    }

    protected abstract int getBoxPanelWidth();

    private void initializeLayout() {
        setLayout(new GridBagLayout());

        this.boxComponentsPanel = new JBombermanBoxContainerPanel(title, true) {
            @Override
            protected int getDefaultBoxPanelWidth() {
                return BoxMenuPanel.this.getBoxPanelWidth();
            }

            @Override
            protected void addCustomElements() {
                BoxMenuPanel.this.addCustomElements();
            }
        };
        boxComponentsPanel.initializeLayout();
        add(boxComponentsPanel);
    }


    protected abstract void addCustomElements();

    @Override
    public void onShowCallback() {
        boxComponentsPanel.refresh();
    }
}