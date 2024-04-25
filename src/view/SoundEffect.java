package view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class SoundEffect {
    private Clip clip;

    public SoundEffect(String path) {
        try {
            URL url = this.getClass().getResource(path);
            if (url == null) {
                System.err.println("Resource not found: " + path);
                throw new RuntimeException("Resource not found: " + path);
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null)
            return;
        stop();
        clip.setFramePosition(0); // Rewind to the beginning
        clip.start(); // Start playing
    }

    public void loop() {
        if (clip == null)
            return;
        stop();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null && clip.isRunning()) clip.stop();
    }
}
