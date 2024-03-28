package view;

import javax.swing.JLabel;

/**
 * Represents the label that indicates to the user the
 * remaining tiles in the game.
 */
public class RemainingTilesLabel extends JLabel {
    // TODO : Add an icon to make the label more visually appealing
    public RemainingTilesLabel() {
        super("Remaining Tiles: 0");
        setOpaque(false);
        setForeground(java.awt.Color.WHITE);
    }

    public void setRemainingTiles(int remainingTiles) {
        this.setText("                                                                                              Remaining Tiles: " + remainingTiles+"                                                   ");
        validate();
        repaint();
    }
}
