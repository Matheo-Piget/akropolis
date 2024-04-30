package view;

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
}
