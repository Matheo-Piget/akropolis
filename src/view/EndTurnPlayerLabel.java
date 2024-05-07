package view;

import javax.swing.JComponent;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Container;
import util.Timeline;

class EndTurnPlayerLabel extends JLabel {
    private Timeline timeline;
    private final Color outlineColor = Color.WHITE;
    private final int ANIMATION_REPEAT = 50;

    public EndTurnPlayerLabel() {
        super("Prochain Joueur:");
        setForeground(Color.BLACK);
        timeline = new Timeline(1);
        setSize(400, 75);
        // Center the label
        setHorizontalAlignment(JLabel.CENTER);
    }

    // Gets called when it gets added to the parent component
    @Override
    public void addNotify() {
        super.addNotify();
        Container parent = getParent();
        // Ugly code but hey, it works
        BoardView boardView = (BoardView) parent.getParent().getParent().getParent();
        setFont(new Font("Serif", Font.BOLD, 25));
        timeline = new Timeline(0);

        // Set the label's initial position to the center of the parent
        int centerX = parent.getWidth() / 2;
        int centerY = parent.getHeight() / 2;
        int labelWidth = getWidth();
        int labelHeight = getHeight() * 2;
        setBounds(centerX - labelWidth, centerY - labelHeight, labelWidth, labelHeight);

        // Set the label's initial opacity to 0 (fully transparent)
        setOpaque(false);
        setVisible(false);
        setForeground(new Color(getForeground().getRed(), getForeground().getGreen(), getForeground().getBlue(), 0));

        int ANIMATION_RATE = 17;
        timeline.addKeyFrame(new Timeline.KeyFrame(ANIMATION_RATE, ANIMATION_REPEAT, e -> {
            setVisible(true);
            // Increase the label's opacity by a step each time the action is performed
            int alpha = getForeground().getAlpha();
            alpha = Math.min(alpha + 255 / ANIMATION_REPEAT, 255);
            setForeground(new Color(getForeground().getRed(), getForeground().getGreen(), getForeground().getBlue(),
                    alpha));
            if (parent instanceof JComponent) {
                ((JComponent) parent).repaint(getBounds());
            }
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(900, 1, e -> {
            // Do nothing
        }));
        timeline.setOnFinished(e -> {
            timeline.reset();
            setVisible(false);
            // Reset the label's opacity to 0
            setForeground(
                    new Color(getForeground().getRed(), getForeground().getGreen(), getForeground().getBlue(), 0));
            boardView.unfreeze();
            boardView.displayNextBoard();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!isVisible()) {
            return;
        }
        // Draw a blue rectangle with rounded corners
        g.setColor(new Color(0, 0, 255, 100));
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        String labelText = getText();
        String[] words = labelText.split("\\s+");
        Font labelFont = getFont();
        FontMetrics metrics = g.getFontMetrics(labelFont);

        int x = (getWidth() - metrics.stringWidth(words[0])) / 2;
        int totalTextHeight = words.length * metrics.getHeight();
        int y = ((getHeight() - totalTextHeight) / 2) + metrics.getAscent();

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

        y = ((getHeight() - totalTextHeight) / 2) + metrics.getAscent();
        for (String word : words) {
            g2d.setColor(getForeground());
            g2d.drawString(word, x, y);
            y += g2d.getFontMetrics().getHeight();
        }
    }

    public void setPlayerName(String playerName) {
        setText("Prochain Joueur: " + playerName);
    }


    // Start the animation
    public void play(String playerName) {
        setPlayerName(playerName);
        timeline.start();
    }

    public void stop() {
        timeline.stop();
    }
}