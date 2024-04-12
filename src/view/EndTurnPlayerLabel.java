package view;

import javax.swing.JComponent;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.event.ActionEvent;
import util.Timeline;

class EndTurnPlayerLabel extends JLabel {
    private Timeline timeline;
    private Color outlineColor = Color.WHITE;
    private final int ANIMATION_RATE = 17;
    private final int ANIMATION_REPEAT = 50;

    public EndTurnPlayerLabel() {
        super("Prochain Joueur:");
        setForeground(Color.BLACK);
        timeline = new Timeline(1);
        setSize(200, 100);
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
        setFont(new Font("Serif", Font.BOLD, 25));
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

    @Override
    protected void paintComponent(Graphics g) {
        String labelText = getText();
        String[] words = labelText.split("\\s+");
        Font labelFont = getFont();
        FontMetrics metrics = g.getFontMetrics(labelFont);

        int x = (getWidth() - metrics.stringWidth(words[0])) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(outlineColor);
        g2d.setFont(labelFont);

        for (String word : words) {
            g2d.drawString(word, x - 1, y - 1);
            g2d.drawString(word, x - 1, y + 1);
            g2d.drawString(word, x + 1, y - 1);
            g2d.drawString(word, x + 1, y + 1);
            y += g2d.getFontMetrics().getHeight();
        }

        y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        for (String word : words) {
            g2d.setColor(Color.BLACK);
            g2d.drawString(word, x, y);
            y += g2d.getFontMetrics().getHeight();
        }
    }

    public void setPlayerName(String playerName) {
        setText("Prochain Joueur: " + playerName);
    }

    public void play() {
        timeline.start();
    }

    public void play(String playerName) {
        setPlayerName(playerName);
        play();
    }

    public void stop() {
        timeline.stop();
    }
}