package game.ui.panels.settings;

import game.data.DataInputOutput;
import game.events.RunnablePar;
import game.localization.Localization;
import game.sound.AudioManager;
import game.ui.panels.BombermanFrame;
import game.ui.viewelements.settings.SettingsElementView;
import game.ui.viewelements.settings.SlideElementView;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import static game.localization.Localization.*;

public class SettingsPanel extends BoxMenuPanel {
    /**
     * Constructs a ProfilePanel object.
     *
     * @param cardLayout the CardLayout of the parent container
     * @param parent     the parent container of this panel
     * @param frame      the BombermanFrame object
     */
    public SettingsPanel(CardLayout cardLayout, JPanel parent, BombermanFrame frame) {
        super(cardLayout, parent, frame, Localization.get(SETTINGS));
    }

    private SettingsElementView createTextFieldElementView(String label, String keyChar, RunnablePar runnablePar) {
        return addTextFieldElementView(label, keyChar, runnablePar, 1);
    }

    private SlideElementView createSlideElementView(String title, int currValue, RunnablePar callback) {
        return addSlideElementView(title, currValue, callback);
    }

    private RunnablePar createKeyRunnable(final Consumer<Integer> keySetter) {
        return new RunnablePar() {
            @Override
            public <T> Object execute(T par) {
                char c = par.toString().isBlank() ? ' ' : par.toString().charAt(0);
                keySetter.accept((int) c);
                return null;
            }
        };
    }

    @Override
    protected void addCustomElements() {
        SettingsElementView forwardKey = createTextFieldElementView(
                Localization.get(KEY_FORWARD),
                DataInputOutput.getInstance().getForwardKeyChar(),
                createKeyRunnable(integer -> DataInputOutput.getInstance().setForwardKey(integer))
        );

        SettingsElementView leftKey = createTextFieldElementView(
                Localization.get(KEY_LEFT),
                DataInputOutput.getInstance().getLeftKeyChar(),
                createKeyRunnable(integer -> DataInputOutput.getInstance().setLeftKey(integer))
        );

        SettingsElementView backKey = createTextFieldElementView(
                Localization.get(KEY_BACK),
                DataInputOutput.getInstance().getBackKeyChar(),
                createKeyRunnable(integer -> DataInputOutput.getInstance().setBackKey(integer))
        );

        SettingsElementView rightKey = createTextFieldElementView(
                Localization.get(KEY_RIGHT),
                DataInputOutput.getInstance().getRightKeyChar(),
                createKeyRunnable(integer -> DataInputOutput.getInstance().setRightKey(integer))
        );

        SettingsElementView bombKey = createTextFieldElementView(
                Localization.get(KEY_BOMB),
                DataInputOutput.getInstance().getBombKeyChar(),
                createKeyRunnable(integer -> DataInputOutput.getInstance().setBombKey(integer))
        );

        SettingsElementView audio = createSlideElementView(Localization.get(AUDIO_VOLUME),
                DataInputOutput.getInstance().getVolume(), new RunnablePar() {
                    @Override
                    public <T> Object execute(T par) {
                        DataInputOutput.getInstance().setVolume((int) par);
                        AudioManager.getInstance().stopBackgroundSong();
                        AudioManager.getInstance().playBackgroundSong();
                        return null;
                    }
                });
    }
}
