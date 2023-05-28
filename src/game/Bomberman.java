package game;

import game.data.DataInputOutput;
import game.tasks.GarbageCollectorTask;
import game.level.Level;
import game.level.WorldSelectorLevel;
import game.localization.Localization;
import game.sound.AudioManager;
import game.sound.SoundModel;
import game.ui.viewelements.misc.ToastHandler;
import game.ui.panels.BombermanFrame;
import game.ui.panels.PagePanel;
import game.ui.panels.game.MatchPanel;
import game.ui.panels.menus.LoadingPanel;
import game.ui.panels.menus.MainMenuPanel;

import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

import static game.localization.Localization.WELCOME_TEXT;

public class Bomberman {
    private static BomberManMatch bomberManMatch;
    private static BombermanFrame bombermanFrame;

    public static void main(String[] args){
        retrievePlayerData();
        startGarbageCollectorTask();
        start();
    }

    private static void start() {
        bombermanFrame = new BombermanFrame();
        bombermanFrame.create();
        show(MainMenuPanel.class);
        ToastHandler.getInstance().show(Localization.get(WELCOME_TEXT));
        AudioManager.getInstance().play(SoundModel.SOUNDTRACK, true);
    }

    public static void startGarbageCollectorTask() {
        new GarbageCollectorTask().start();
    }

    public static void retrievePlayerData() {
        DataInputOutput.retrieveData();
    }

    public static BombermanFrame getBombermanFrame() {
        return bombermanFrame;
    }

    public static BomberManMatch getMatch() {
        return bomberManMatch;
    }

    public static void quitMatch() {
        destroyLevel();
        show(MainMenuPanel.class);
    }

    public static void destroyLevel() {
        if(bomberManMatch != null) bomberManMatch.destroy();
        bomberManMatch = new BomberManMatch(new WorldSelectorLevel()); // Temporary sets the current level to WorldSelectorLevel to avoid null pointer exception if some threads aren't killed yet
        System.gc();
    }

    private static void doStartLevel(Level level) {
        destroyLevel();
        bomberManMatch = new BomberManMatch(level);
        bombermanFrame.initGamePanel();
        bomberManMatch.getCurrentLevel().start(bombermanFrame.getPitchPanel());
        Bomberman.getBombermanFrame().addKeyListener(Bomberman.getMatch().getControllerManager());
        Bomberman.getBombermanFrame().getPitchPanel().addMouseListener(Bomberman.getMatch().getMouseControllerManager());
        Bomberman.getBombermanFrame().getPitchPanel().addMouseMotionListener(Bomberman.getMatch().getMouseControllerManager());
        show(MatchPanel.class);
    }

    public static void startLevel(Level level) {
        bombermanFrame.getLoadingPanel().initialize();
        bombermanFrame.getLoadingPanel().updateText(level);
        bombermanFrame.getLoadingPanel().setCallback(() -> doStartLevel(level));
        show(LoadingPanel.class);

        Bomberman.show(LoadingPanel.class);
    }

    public static void show(Class<? extends PagePanel> page) {
        bombermanFrame.getCardLayout().show(bombermanFrame.getParentPanel(), page.getSimpleName());

        // Gets the component with the passed class and fires its onShowCallback;
        Optional<Component> shownComponentOpt = Arrays.stream(
                bombermanFrame.getParentPanel().getComponents()
        ).filter(c -> c.getClass() == page).findFirst();

        Component shownComponent = shownComponentOpt.orElse(null);
        if(shownComponent instanceof PagePanel)
            ((PagePanel)(shownComponent)).onShowCallback();
    }
}
