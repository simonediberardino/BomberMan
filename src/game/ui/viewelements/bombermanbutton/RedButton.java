package game.ui.viewelements.bombermanbutton;

import game.values.BomberColors;

import java.awt.*;

public class RedButton extends BombermanButton {
    public RedButton(String text) {
        super(text);
    }

    @Override
    public Color getBorderColor() {
        return BomberColors.RED;
    }

    @Override
    public Color getMouseHoverBackgroundColor() {
        return Color.RED;
    }
}
