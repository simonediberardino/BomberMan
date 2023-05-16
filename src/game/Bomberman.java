package game;

import game.data.DataInputOutput;
import game.engine.GarbageCollectorTask;
import game.level.Level;
import game.level.world1.World1Level5;
import game.ui.panels.BombermanFrame;
import game.ui.panels.PagePanel;
import game.ui.panels.game.MatchPanel;
import game.ui.panels.menus.LoadingPanel;
import game.ui.panels.menus.MainMenuPanel;

import java.util.Timer;
import java.util.TimerTask;

import static game.ui.panels.menus.LoadingPanel.LOADING_TIMER;

public class Bomberman {
    private static BomberManMatch bomberManMatch;
    private static BombermanFrame bombermanFrame;

    public static void main(String[] args){
        retrievePlayerData();
        startGarbageCollectorTask();
        start();
    }

    public static void start() {
        bombermanFrame = new BombermanFrame();
        bombermanFrame.create();
        show(MainMenuPanel.class);
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

    public static void destroyLevel() {
        if(bomberManMatch != null) bomberManMatch.destroy();
        bomberManMatch = new BomberManMatch(new World1Level5()); // Temporary sets the current level to WorldSelectorLevel to avoid null pointer exception if some threads aren't killed yet
        System.gc();
    }

    private static void doStartLevel(Level level) {
        destroyLevel();
        bomberManMatch = new BomberManMatch(level);
        bombermanFrame.initGamePanel();
        bomberManMatch.getCurrentLevel().start(bombermanFrame.getPitchPanel());
        Bomberman.getBombermanFrame().addKeyListener(Bomberman.getMatch().getControllerManager());

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
    }
}
