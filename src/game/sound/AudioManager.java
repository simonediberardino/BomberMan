package game.sound;


import game.utils.Paths;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.sound.sampled.*;

public class AudioManager {
    private String currentBackgroundSong = "";
    private final HashMap<String, LinkedList<Clip>> audioHashMap = new HashMap<>();
    private static AudioManager instance;

    public static AudioManager getInstance() {
        if (instance == null)
            instance = new AudioManager();
        return instance;
    }

    private AudioManager() {}

    public Clip play(SoundModel soundModel){
        return play(soundModel, false);
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

    public Clip play(SoundModel soundModel,boolean loop, int volumePercentage){
        return play(soundModel.toString(),loop,volumePercentage);
    }

    public Clip play(String sound, boolean loop, int volumePercentage){
        try {
            InputStream in = new BufferedInputStream(getClass().getResourceAsStream(String.format("/%s", sound)));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
            Clip clip = AudioSystem.getClip();

            clip.open(audioIn);

            // Set the volume of the clip to 1/5 of the available range
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volumeValue = (volume.getMaximum()) / 100*volumePercentage;
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

    public Clip play(String sound, boolean loop){
        return play(sound,loop,20);
    }

    public void addSoundToHashMap(String soundModelString, Clip obj){
        List<Clip> tempList =  audioHashMap.get(soundModelString);
        if(tempList == null) audioHashMap.put(soundModelString, new LinkedList<> (Collections.singletonList(obj)));
        else tempList.add(obj);
    }

    public Clip removeSoundFromHashMap(String soundModelString) {
        LinkedList<Clip> map = audioHashMap.get(soundModelString);

        if (map == null) {
            return null;
        }

        Clip c = map.pop();
        if(map.isEmpty()) audioHashMap.remove(soundModelString);
        return c;
    }

    public void stopAllInstancesOfSound(String soundModelString) {
        Clip c;
        while((c = removeSoundFromHashMap(soundModelString)) != null){
            c.stop();
        }
    }

    public void playBackgroundSong(){
        playBackgroundSong(Paths.getDefaultSoundTrack());
    }

    public void playBackgroundSong(String newSong) {
        if(currentBackgroundSong.equals(newSong)) return;

        if(!currentBackgroundSong.isBlank()){
            stopAllInstancesOfSound(currentBackgroundSong);
        }

        currentBackgroundSong = newSong;
        play(currentBackgroundSong, true);
    }

    public void stopBackgroundSong() {
        stopAllInstancesOfSound(currentBackgroundSong);
        currentBackgroundSong = "";
    }

    public void stop(String soundModelString){
        Clip c = removeSoundFromHashMap(soundModelString);
        if(c != null) c.stop();
    }

}
