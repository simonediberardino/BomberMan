package game.sound;


import game.data.DataInputOutput;
import game.utils.Paths;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.sound.sampled.*;

public class AudioManager {
    private static int MAX_VOLUME_MANAGER = 20;
    private static float MAX_VOLUME = 5f;
    private static float MIN_VOLUME = -5f;
    private static AudioManager instance;
    private final HashMap<String, LinkedList<Clip>> audioHashMap = new HashMap<>();
    private String currentBackgroundSong = "";

    private AudioManager() {
    }

    public static AudioManager getInstance() {
        if (instance == null)
            instance = new AudioManager();
        return instance;
    }

    public Clip play(SoundModel soundModel) {
        return play(soundModel, false);
    }

    /**
     * Plays the specified sound model.
     *
     * @param soundModel The sound model to play.
     * @param loop       Determines whether the sound should be played in a loop.
     * @return
     */
    public Clip play(SoundModel soundModel, boolean loop) {
        return play(soundModel.toString(), loop, calculateVolumeFromStorage());
    }

    public Clip play(String sound, boolean loop) {
        return play(sound, loop, calculateVolumeFromStorage());
    }

    public Clip play(SoundModel soundModel, boolean loop, int volumePercentage) {
        return play(soundModel.toString(), loop, volumePercentage);
    }


    public Clip play(String sound, boolean loop, int volumePercentage) {
        try {
            InputStream in = new BufferedInputStream(getClass().getResourceAsStream(String.format("/%s", sound)));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
            Clip clip = AudioSystem.getClip();

            clip.open(audioIn);

            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float maxVolume = volume.getMaximum();
            float minVolume = volume.getMinimum();
            float diffVolume = Math.abs(maxVolume - minVolume);
            float volumeValue = minVolume + diffVolume * (volumePercentage / 100f);
            volume.setValue(volumeValue);

            addSoundToHashMap(sound, clip);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                return clip;
            }

            //if clip doesn't loop continuosly, it is removed automatically from hashmap when it stops
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    removeSoundFromHashMap(sound);
                }
            });
            clip.start();

            return clip;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
            e1.printStackTrace();
        }

        return null;
    }

    public int calculateVolumeFromStorage() {
        int storedVolume = DataInputOutput.getInstance().getVolume();

        double percentage = ((double) storedVolume + 5) / 10.0 * 100.0;
        return (int) Math.max(percentage, 0.0); // Ensure the percentage is not less than 0%
    }

    public void addSoundToHashMap(String soundModelString, Clip obj) {
        List<Clip> tempList = audioHashMap.get(soundModelString);
        if (tempList == null) audioHashMap.put(soundModelString, new LinkedList<>(Collections.singletonList(obj)));
        else tempList.add(obj);
    }

    public Clip removeSoundFromHashMap(String soundModelString) {
        LinkedList<Clip> map = audioHashMap.get(soundModelString);

        if (map == null) {
            return null;
        }

        Clip c = map.pop();
        if (map.isEmpty()) audioHashMap.remove(soundModelString);
        return c;
    }

    public void stopAllInstancesOfSound(String soundModelString) {
        Clip c;
        while ((c = removeSoundFromHashMap(soundModelString)) != null) {
            c.stop();
        }
    }

    public void playBackgroundSong() {
        playBackgroundSong(Paths.getDefaultSoundTrack());
    }

    public void playBackgroundSong(String newSong) {
        if (currentBackgroundSong.equals(newSong)) return;

        if (!currentBackgroundSong.isBlank()) {
            stopAllInstancesOfSound(currentBackgroundSong);
        }

        currentBackgroundSong = newSong;
        play(currentBackgroundSong, true);
    }

    public void stopBackgroundSong() {
        stopAllInstancesOfSound(currentBackgroundSong);
        currentBackgroundSong = "";
    }

    public void stop(String soundModelString) {
        Clip c = removeSoundFromHashMap(soundModelString);
        if (c != null) c.stop();
    }

}
