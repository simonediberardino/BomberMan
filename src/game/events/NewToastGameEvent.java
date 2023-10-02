package game.events;

import game.Bomberman;
import game.sound.AudioManager;

import static game.sound.SoundModel.BONUS_ALERT;

public class NewToastGameEvent implements GameEvent {
    @Override
    public void invoke(Object arg) {
        Bomberman.getBombermanFrame().getParentPanel().repaint();
        if((boolean) arg){
            AudioManager.getInstance().play(BONUS_ALERT);
        }
    }
}
