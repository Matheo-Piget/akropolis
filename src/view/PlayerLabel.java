package view;

import util.OutlinedLabel;

/**
 * Represents the label that indicates to the user which player's turn it is.
 */
public class PlayerLabel extends OutlinedLabel {
    
    public PlayerLabel(String playerName){
        super("Player: " + playerName);
    }

    public void setPlayer(String playerName){
        this.setText("Player: " + playerName);
        validate();
        repaint();
    }
}
