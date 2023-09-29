package game.ui.panels;

import game.ui.viewelements.misc.ToastHandler;
import game.utils.Utility;

import javax.swing.*;
import java.awt.*;

public abstract class PagePanel extends JPanel {
    protected final ToastHandler toastHandler = ToastHandler.getInstance();
    protected final JPanel parent;
    protected final CardLayout cardLayout;
    protected final BombermanFrame frame;
    private final String imagePath;

    public PagePanel(CardLayout cardLayout, JPanel parent, BombermanFrame frame, String imagePath){
        this.parent = parent;
        this.cardLayout = cardLayout;
        this.frame = frame;
        this.imagePath = imagePath;
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image backgroundImage = Utility.loadImage(imagePath);

        int width = (int) frame.getPreferredSize().getWidth();
        int height = (int) frame.getPreferredSize().getHeight();

        // Scale the background image to fit the size of the panel and draw it
        if (width != 0 && height != 0) {
            try{
                g.drawImage(backgroundImage.getScaledInstance(width, height, 1), 0, 0, null);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ToastHandler getToastHandler(){
        return toastHandler;
    }

    public abstract void onShowCallback();
}
