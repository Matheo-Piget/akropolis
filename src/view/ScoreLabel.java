package view;

import javax.swing.JLabel;

public class ScoreLabel extends JLabel {
    public ScoreLabel(int score){
        super("Score du joueur : " + score );
        setOpaque(false);
        setForeground(java.awt.Color.WHITE);
    }

    public void setScore(int  score ){
        this.setText("Score du joueur : " + score );
        validate();
        repaint();
    }
}
