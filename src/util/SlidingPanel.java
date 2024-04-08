package util;

import javax.swing.JPanel;
/**
 * Represents an animated panel that can slide in and out of view.
 */
public class SlidingPanel extends JPanel {
    Timeline slideIn;
    Timeline slideOut;

    public SlidingPanel(JPanel parent){
        slideIn = new Timeline(1);
        slideOut = new Timeline(1);
        slideIn.addKeyFrame(new Timeline.KeyFrame(10, 100, e -> {
            setLocation(getX() + 1/2 * parent.getWidth() / 100 , getY());
        }));
        slideOut.addKeyFrame(new Timeline.KeyFrame(10, 100, e -> {
            setLocation(getX() - 1/2 * parent.getWidth() / 100 , getY());
        }));
        // Set the initial location of the label to be off screen
        setLocation(-getWidth(), parent.getHeight() / 2 - getHeight() / 2);
    }

    public void slideIn(){
        slideIn.start();
    }

    public void slideOut(){
        slideOut.start();
    }
}
