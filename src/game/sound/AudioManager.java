package game.sound;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.sound.sampled.*;

public class AudioManager {
    private HashMap<SoundModel, List<Clip>> audioHashMap = new HashMap<>();
    private static AudioManager instance;


    public static AudioManager getInstance() {
        if (instance == null)
            instance = new AudioManager();
        return instance;
    }

    private AudioManager() {}

    public void play(SoundModel soundModel){
        play(soundModel, false);
    }

    /**
     * Plays the specified sound model.
     *  @param soundModel The sound model to play.
     * @param loop       Determines whether the sound should be played in a loop.
     * @return
     */
    public Clip play(SoundModel soundModel, boolean loop) {
        return play(soundModel.toString(), loop);
    }
    public void addSoundToHashMap(SoundModel key, Clip obj){
        HashMap<SoundModel, List<Clip>> sounds = audioHashMap;
        List<Clip> tempList =  sounds.get(key);
        if(tempList == null) audioHashMap.put(key, Collections.singletonList(obj));
        else tempList.add(obj);
    }

    public Clip play(String sound, boolean loop){
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(sound));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
            Clip clip = AudioSystem.getClip();

            clip.open(audioIn);

            // Set the volume of the clip to 1/5 of the available range
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volumeValue = (volume.getMinimum() + volume.getMaximum()) / 5;
            volume.setValue(volumeValue);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            // Start playing the clip
            clip.start();
            //
            return clip;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
            e1.printStackTrace();
        }

        return null;
    }
}
