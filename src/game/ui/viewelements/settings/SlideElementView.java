package game.ui.viewelements.settings;

import game.events.RunnablePar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SlideElementView extends SettingsElementView{
    private static final int MIN_VALUE = -5;
    private static final int MAX_VALUE = 5;
    private final JLabel name;
    private final JSlider slider;

    public SlideElementView(JPanel gridPanel, String title, int currValue, RunnablePar callback) {
        super(gridPanel);

        name = new JLabel(title, SwingConstants.CENTER); // Create a new JLabel named "name" with the specified title and center alignment.
        slider = new JSlider(JSlider.HORIZONTAL, MIN_VALUE, MAX_VALUE, currValue);

        slider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                callback.execute(source.getValue());
            }
        });

        slider.setOpaque(false);
        // Adding mouse listeners to the textfield;

        name.setForeground(Color.WHITE);
        name.setFont(new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE));
        slider.setForeground(Color.ORANGE);
        slider.setFont(new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE));

        add(name); // Add the "name" label to the SettingsElementView object.
        add(slider); // Add the "value" text field to the SettingsElementView object.
    }
}
