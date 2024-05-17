package util;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to manage the sounds of the game
 */
public class SoundManager {

    private static final Map<String, Clip> soundMap = new HashMap<>();

    private static boolean isEnable = true;

    /**
     * Load a sound from a file
     * @param soundName the name of the sound
     * @param path the path to the sound file
     */
    public static void loadSound(String soundName, String path) {
        try {
            isEnable = SettingsParser.soundEnabled();
            URL url = SoundManager.class.getResource(path);
            if (url == null) {
                System.err.println("Resource not found: " + path);
                throw new RuntimeException("Resource not found: " + path);
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundMap.put(soundName, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void free(String soundName) {
        Clip clip = soundMap.get(soundName);
        if (clip != null) {
            clip.close();
            soundMap.remove(soundName);
        }
    }

    public static boolean isSoundEnabled() {
        return isEnable;
    }

    public static void invertSoundEnabled() {
        isEnable = !isEnable;
        SettingsParser.setSoundEnabled(isEnable);
    }

    /**
     * Play a sound
     * @param soundName the name of the sound
     */
    public static void playSound(String soundName) {
        if (!isEnable) {
            return;
        }
        Clip clip = soundMap.get(soundName);
        if (clip != null) {
            clip.setFramePosition(0); // Rembobiner au début
            clip.start(); // Commencer à jouer
        }
    }

    /**
     * Stop a sound
     * @param soundName the name of the sound
     */
    public static void stopSound(String soundName) {
        Clip clip = soundMap.get(soundName);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    /**
     * Loop a sound
     * @param soundName the name of the sound
     */
    public static void loopSound(String soundName) {
        if (!isEnable) {
            return;
        }
        Clip clip = soundMap.get(soundName);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stop a looped sound
     * @param soundName the name of the sound
     */
    public static void stopLoop(String soundName) {
        Clip clip = soundMap.get(soundName);
        if (clip != null) {
            clip.stop();
        }
    }

    /**
     * Stop all sounds
     */
    public static void stopAllSounds() {
        for (Clip clip : soundMap.values()) {
            clip.stop();
        }
    }
}
