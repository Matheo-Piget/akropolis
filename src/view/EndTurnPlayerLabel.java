package view;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.event.ActionEvent;
import util.Timeline;

class EndTurnPlayerLabel extends JLabel {
    private Timeline timeline;
    private final int ANIMATION_RATE = 20;
    private final int ANIMATION_REPEAT = 50;

    public EndTurnPlayerLabel() {
        super("Prochain Joueur: ");
        setOpaque(false);
        timeline = new Timeline(1);
        setSize(200, 50);
        // Center the label
        setHorizontalAlignment(JLabel.CENTER);
    }

    // Gets called when it gets added to the parent component
    @Override
    public void addNotify() {
        super.addNotify();
        // Create a translation animation the label starts off screen to the center
        // of the screen
        Container parent = getParent();
        int centerY = parent.getHeight() / 2;
        int centerLabelY = getHeight() + centerY / 2;
        setBounds(-getWidth(), centerY - centerLabelY, getWidth(), getHeight());
        setFont(getFont().deriveFont(20.0f));
        timeline = new Timeline(1);
        int centerX = parent.getWidth() / 2;
        int labelWidth = getWidth();
        int finalX = centerX - labelWidth / 2;
        int totalDistance = finalX - getX();
        int step = totalDistance / ANIMATION_REPEAT;
        timeline.addKeyFrame(new Timeline.KeyFrame(ANIMATION_RATE, ANIMATION_REPEAT, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLocation(getX() + step, getY());
                if (parent instanceof JComponent) {
                    ((JComponent) parent).repaint(getBounds());
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