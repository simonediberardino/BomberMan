package game.ui.viewelements.misc;

import game.events.NewToastGameEvent;
import game.utils.Utility;

import java.awt.*;

public class ToastHandler {
    private static final int TOAST_DURATION = 3500;
    private static final int TOAST_START_Y = (int) Utility.getScreenSize().getHeight();
    private static final int TOAST_ANIM_STEP_SIZE = Utility.px(50);
    private static final int CORNER_RADIUS = 15;
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 0);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(255, 87, 51);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 150);
    private static final Font TOAST_FONT = new Font(Font.MONOSPACED, Font.BOLD, Utility.px(35));
    private static final int BORDER_WIDTH = Utility.px(5);
    private static ToastHandler instance = null;
    private long animStoppedTime = 0;
    private int toastY = TOAST_START_Y;
    private String text;
    private boolean permanent = false;

    public static ToastHandler getInstance() {
        return instance = instance == null ? new ToastHandler() : instance;
    }

    public void showToast(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Calculate the dimensions based on text length
        Font toastFont = new Font(Font.MONOSPACED, Font.BOLD, Utility.px(35));
        FontMetrics metrics = g2d.getFontMetrics(toastFont);
        int toastWidth = calculateToastWidth(metrics);
        int toastHeight = calculateToastHeight(metrics);
        int toastX = calculateToastX(toastWidth);
        int toastEndY = calculateToastEndY();

        animateToast(toastEndY);

        if (text == null || text.isBlank()) {
            g2d.dispose();
            return;
        }

        drawToastBackground(g2d, toastX, toastY, toastWidth, toastHeight);
        drawToastBorder(g2d, toastX, toastY, toastWidth, toastHeight, BORDER_WIDTH);
        drawTransparentBorder(g2d, toastX, toastY, toastWidth, toastHeight);
        drawToastText(g2d, toastX, toastY, metrics);

        g2d.dispose();
    }

    private int calculateToastWidth(FontMetrics metrics) {
        int textWidth = metrics.stringWidth(text);
        int paddingX = BORDER_WIDTH * 2;
        return textWidth + 2 * paddingX;
    }

    private int calculateToastHeight(FontMetrics metrics) {
        int textHeight = metrics.getHeight();
        return textHeight + 2 * BORDER_WIDTH;
    }

    private int calculateToastX(int toastWidth) {
        return (int) (Utility.getScreenSize().getWidth() / 2 - toastWidth / 2);
    }

    private int calculateToastEndY() {
        return (int) (Utility.getScreenSize().getHeight() - Utility.px(200));
    }

    private void animateToast(int toastEndY) {
        if (toastEndY < toastY && animStoppedTime == 0) {
            toastY -= TOAST_ANIM_STEP_SIZE;  // Move the toast up
        } else if (animStoppedTime == 0) {
            animStoppedTime = System.currentTimeMillis();  // Record the stop time
        }

        if (animStoppedTime == 0 || permanent) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (currentTime - animStoppedTime > TOAST_DURATION) {
            toastY += TOAST_ANIM_STEP_SIZE;  // Move the toast down

            if (toastY >= toastEndY * 2) {
                cancel();  // Reset the toast animation
            }
        }
    }

    private void drawToastBackground(Graphics2D g2d, int toastX, int toastY, int toastWidth, int toastHeight) {
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(toastX, toastY, toastWidth, toastHeight);
    }

    private void drawToastBorder(Graphics2D g2d, int toastX, int toastY, int toastWidth, int toastHeight, int borderWidth) {
        g2d.setColor(BORDER_COLOR);
        g2d.fillRoundRect(toastX, toastY, toastWidth, toastHeight, CORNER_RADIUS, CORNER_RADIUS);
        g2d.setColor(SHADOW_COLOR);
        g2d.fillRoundRect(toastX + borderWidth, toastY + borderWidth, toastWidth - borderWidth * 2, toastHeight - borderWidth * 2, CORNER_RADIUS, CORNER_RADIUS);
    }

    private void drawTransparentBorder(Graphics2D g2d, int toastX, int toastY, int toastWidth, int toastHeight) {
        FontMetrics metrics = g2d.getFontMetrics(TOAST_FONT);

        // Draw transparent border/margin
        g2d.setColor(new Color(0, 0, 0, 0)); // transparent color
        g2d.fillRect(BORDER_WIDTH, toastY, BORDER_WIDTH, toastHeight); // left
        g2d.fillRect(toastWidth - BORDER_WIDTH, toastY, BORDER_WIDTH, toastHeight); // right
        g2d.fillRect(BORDER_WIDTH, toastY, toastWidth - BORDER_WIDTH * 2, BORDER_WIDTH); // top
        g2d.fillRect(BORDER_WIDTH, toastHeight - BORDER_WIDTH, toastWidth - BORDER_WIDTH * 2, BORDER_WIDTH); // bottom

        // Draw text
        g2d.setFont(TOAST_FONT);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(TEXT_COLOR);
        int textX = toastX + BORDER_WIDTH * 2;
        int textY = toastY + BORDER_WIDTH + metrics.getAscent();
        g2d.drawString(text, textX, textY);
    }

    private void drawToastText(Graphics2D g2d, int toastX, int toastY, FontMetrics metrics) {
        // Draw text
        g2d.setFont(TOAST_FONT);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(TEXT_COLOR);
        int textX = toastX + BORDER_WIDTH * 2;
        int textY = toastY + BORDER_WIDTH + metrics.getAscent();
        g2d.drawString(text, textX, textY);
    }

    public void cancel() {
        animStoppedTime = 0;
        toastY = TOAST_START_Y;
        permanent = false;
        text = null;
    }

    public void show(String text, boolean playSound) {
        show(text, false, playSound);
    }

    public void show(String text) {
        show(text, false, true);
    }

    public void show(String text, boolean permanent, boolean playSound) {
        cancel();
        this.text = text;
        this.permanent = permanent;
        new NewToastGameEvent().invoke(playSound);
    }

    public String getText() {
        return text;
    }
}