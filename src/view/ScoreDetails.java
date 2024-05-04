package view;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ScoreDetails extends JPanel {
    
    public ScoreDetails() {
        setOpaque(true);
        JButton back = new JButton("Retour");
        back.addActionListener(e -> {
            setSize(0, 0);
        });
        add(back);
    }
}
