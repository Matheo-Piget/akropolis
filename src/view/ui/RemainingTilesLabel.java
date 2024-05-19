package view.ui;

import javax.swing.JLabel;

/**
 * Represents the label that indicates to the user the
 * remaining tiles in the game.
 */
class RemainingTilesLabel extends JLabel {

    public RemainingTilesLabel() {
        super("Tuiles restantes: 0");
        setOpaque(false);
        setForeground(java.awt.Color.WHITE);
    }
}
