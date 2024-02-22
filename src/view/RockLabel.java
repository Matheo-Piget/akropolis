package view;

import javax.swing.JLabel;

/**
 * Represents the label that indicates to the user their current rock count.
 */
public class RockLabel extends JLabel {
    
    public RockLabel(){
        super("Rocks: 0");
    }
    
    public void setRocks(int rocks){
        this.setText("Rocks: " + rocks);
        validate();
        repaint();
    }
}
