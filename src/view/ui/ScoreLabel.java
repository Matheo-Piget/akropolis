package view.ui;

import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Color;

class ScoreLabel extends JLabel {
    private Timer timer;
    private boolean isBlinking = false;

    public ScoreLabel(int score) {
        super("Score : " + score);
        setOpaque(false);
        setForeground(java.awt.Color.WHITE);
    }

    public void setScore(int score) {
        setText("Score : " + score);
        validate();
        startBlinking();
        repaint();
    }
    

    /**
     * Starts the blinking animation.
     */
    private void startBlinking() {
        if (timer != null && timer.isRunning()) {
            return;
        }

        timer = new Timer(900, e -> {
            isBlinking = !isBlinking;
            setForeground(isBlinking ? Color.RED : Color.WHITE);
            if (!isBlinking) {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }
}
