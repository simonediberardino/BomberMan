package game.level;

import game.data.DataInputOutput;
import game.events.RoundPassedGameEvent;
import game.localization.Localization;

import java.lang.reflect.InvocationTargetException;

import static game.localization.Localization.YOU_DIED;

public abstract class StoryLevel extends Level {
    @Override
    public String getDiedMessage() {
        int lives = DataInputOutput.getInstance().getLives();
        return Localization.get(YOU_DIED).replace("%lives%", Integer.toString(lives));
    }

    @Override
    public void endLevel() {
        try {
            DataInputOutput.getInstance().setLastLevel(getNextLevel().getConstructor().newInstance());
            DataInputOutput.getInstance().increaseRounds();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }

        new RoundPassedGameEvent().invoke(null);
        DataInputOutput.getInstance().updateStoredPlayerData();
    }

    @Override
    public void onDeathGameEvent() {
        DataInputOutput.getInstance().increaseDeaths();
        DataInputOutput.getInstance().decreaseLives();
        DataInputOutput.getInstance().decreaseScore(1000);
    }

    public boolean isArenaLevel() {
        return false;
    }
}
