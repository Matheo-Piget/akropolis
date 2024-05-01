package view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

// FIXME: Complete cette classe Nidhal
public class ScoreDetails extends JPanel {
    
    public ScoreDetails(BoardView boardView) {
        JButton back = new JButton("Retour");
        back.addActionListener(e -> {
            boardView.resumeGame();
        });
        add(back);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 10); // Remplacez 300, 200 par la largeur et la hauteur souhaitées
    }
}
