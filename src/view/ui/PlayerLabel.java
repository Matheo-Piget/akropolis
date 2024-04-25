package view.ui;

import javax.swing.JLabel;

/**
 * Represents the label that indicates to the user which player's turn it is.
 */
class PlayerLabel extends JLabel {
    
    public PlayerLabel(String playerName){
        super("Joueur: " + playerName);
        setOpaque(false);
        setForeground(java.awt.Color.WHITE);
    }

    public void setPlayer(String playerName){
        this.setText("Joueur: " + playerName);
        validate();
        repaint();
    }
}
