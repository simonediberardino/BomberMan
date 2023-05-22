package game.sound;

import game.utils.Paths;

import java.util.Locale;

public enum SoundModel {
    EXPLOSION,
    BOMBERMAN_STEP,
    POWERUP,
    MOUSE_HOVER,
    CLICK,
    SOUNDTRACK,
    PLAYER_DEATH,
    BONUS_ALERT,
    BOMB_CLOCK,
    ENTITY_DEATH;

    public String toString() {
        return String.format("%s/%s.wav", Paths.getSoundsPath(), super.toString().toLowerCase());
    }
}