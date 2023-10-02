package game.level;

import game.data.DataInputOutput;
import game.localization.Localization;

import static game.localization.Localization.YOU_DIED;

public abstract class StoryLevel extends Level{
    @Override
    public String getDiedMessage() {
        int lives = DataInputOutput.getLives();
        return Localization.get(YOU_DIED).replace("%lives%", Integer.toString(lives));
    }

    public boolean isArenaLevel() {
        return false;
    }
}
