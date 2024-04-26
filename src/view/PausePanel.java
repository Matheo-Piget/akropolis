package view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import view.main.App;
import view.main.states.StartState;

/**
 * Represents the pause panel.
 * Here the player can choose to resume the game or quit.
 * It also displays the game's controls and there is a button to display the game's rules.
 * We can also see who has make the pause.
 */
public class PausePanel extends JPanel {
    /**
     * Constructor for the PausePanel class.
     * Initializes the pause panel.
     */
    public PausePanel(BoardView boardView) {
        setSize(App.getInstance().getScreen().getSize());
        System.out.println("PausePanel size: " + getSize());
        setLayout(new GridLayout(7, 1));
        JLabel left_click = new JLabel("Click gauche: placer une tuile/ Selectionner une tuile");
        JLabel right_click = new JLabel("Click droit: glisser sur le plateau");
        JLabel r_button = new JLabel("R: tourner la tuile");
        JLabel esc_button = new JLabel("Echap: mettre en pause");
        JLabel c_button = new JLabel("C: centrer la vue");
        JButton resume = new JButton("Reprendre");
        resume.addActionListener(e -> {boardView.resumeGame();
        });
        JButton quit = new JButton("Quitter");
        quit.addActionListener(e -> {App.getInstance().appState.changeState(StartState.getInstance());
        });
        // I will make the rules button later
        JButton rules = new JButton("Règles");
        rules.addActionListener(e -> {
            System.out.println("Rules button clicked");
        });
        add(left_click);
        add(right_click);
        add(r_button);
        add(esc_button);
        add(c_button);
        add(resume);
        add(quit);
        validate();
        repaint();
    }
}
