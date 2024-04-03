package view;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundEffect {
    private Clip clip;
    public SoundEffect(String path){
        try{
            URL url = this.getClass().getResource(path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
            e.printStackTrace();
        }
    }
    public void play() {
        if (clip == null) return;
        stop();
        clip.setFramePosition(0); // Rewind to the beginning
        clip.start(); // Start playing
    }

    public void loop(){
        if(clip==null)return ;
        stop();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        if(clip.isRunning())clip.stop();
    }
    public static void main(String[] args){
        SoundEffect soundEffect = new SoundEffect("/res/Menu Game Button Click Sound Effect.wav");
        soundEffect.play();
    }

    
    
}
