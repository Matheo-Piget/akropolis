package view;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.event.ActionEvent;
import util.Timeline;

class EndTurnPlayerLabel extends JLabel {
    private Timeline timeline;

    public EndTurnPlayerLabel() {
        super("Next Player: ");
        setOpaque(true);
        // Make the label transparent
        timeline = new Timeline(1);
        // Set the size of the label
        setSize(200, 50);
    }

    // Call it when it gets added to the parent component
    @Override
    public void addNotify() {
        super.addNotify();
        // Create a translation animation the label starts off screen to the center
        // of the screen
        Container parent = getParent();
        setLocation(-getWidth(), parent.getHeight() / 2 - getHeight() / 2);
        timeline = new Timeline(1);
        timeline.addKeyFrame(new Timeline.KeyFrame(20, 50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLocation((int) (getX() + 1.0 / 2 * parent.getWidth() / 50), getHeight());
                if (parent instanceof JComponent){
                    ((JComponent) parent).repaint();
                }
            }
        }));
        timeline.setOnFinished(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeline.reset();
                setLocation(-getWidth(), parent.getHeight() / 2 - getHeight() / 2);
            }
        });
    }

    public void setPlayerName(String playerName) {
        setText("Next Player: " + playerName);
    }

    public void play() {
        timeline.start();
    }

    public void stop() {
        timeline.stop();
    }
}