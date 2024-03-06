package view;

import javax.swing.JLabel;

/**
 * Represents the label that indicates to the user which player's turn it is.
 */
public class PlayerLabel extends JLabel {
    
    public PlayerLabel(String playerName){
        super("Player: " + playerName);
        setOpaque(false);
        setForeground(java.awt.Color.WHITE);
    }

    public void setPlayer(String playerName){
        this.setText("Player: " + playerName);
        validate();
        repaint();
    }
}
