package game.ui.viewelements.bombermanpanel;

import game.utils.Utility;

import javax.swing.*;
import java.awt.*;

public abstract class BombermanPanel extends JPanel {
    private final int borderWidth = Utility.px(10);
    private final int cornerRadius = 15;
    private final Color backgroundColor = new Color(0, 0, 0, 0);
    private final Color borderColor = getBorderColor();
    private final Color shadowColor = new Color(0, 0, 0, 150);

    public BombermanPanel() {
        setOpaque(false);
    }

    public abstract Color getBorderColor();

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth() - 1;
        int height = getHeight() - 1;

        // Draw background
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, width, height);

        // Draw border
        g2d.setColor(borderColor);
        g2d.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        g2d.setColor(shadowColor);
        g2d.fillRoundRect(borderWidth, borderWidth, width - borderWidth * 2, height - borderWidth * 2, cornerRadius, cornerRadius);

        // Draw transparent border/margin
        g2d.setColor(new Color(0, 0, 0, 0)); // transparent color
        g2d.fillRect(0, 0, borderWidth, height); // left
        g2d.fillRect(width - borderWidth, 0, borderWidth, height); // right
        g2d.fillRect(borderWidth, 0, width - borderWidth * 2, borderWidth); // top
        g2d.fillRect(borderWidth, height - borderWidth, width - borderWidth * 2, borderWidth); // bottom

        g2d.dispose();
    }

    @Override
    public void setBackground(Color bg) {
        // Ignore setBackground to keep the custom background color
    }
}