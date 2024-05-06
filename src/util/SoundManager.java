package util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static final Map<String, Clip> soundMap = new HashMap<>();

    public static boolean isEnable = true;

    /**
     * Load a sound from a file
     * @param soundName the name of the sound
     * @param path the path to the sound file
     */
    public static void loadSound(String soundName, String path) {
        try {
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
