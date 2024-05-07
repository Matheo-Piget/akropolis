package view;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import view.ui.UIFactory;

public class ScoreDetails extends JPanel {
    
    public ScoreDetails() {
        setOpaque(true);
        JButton back = UIFactory.createStyledButton("Cacher", new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                setSize(0, 0);
            }
        });
        add(back);
    }
}
