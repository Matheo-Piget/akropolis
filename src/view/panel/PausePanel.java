package view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.SettingsParser;
import util.SoundManager;
import view.main.App;
import view.main.states.StartState;
import view.ui.UIFactory;

/**
 * Represents the pause panel.
 * This class is used to display the pause menu of the game.
 */
class PausePanel extends JPanel {
    private static final String LEFT_CLICK_LABEL = "Click gauche: placer une tuile/ Sélectionner une tuile";
    private static final String RIGHT_CLICK_LABEL = "Click droit: glisser sur le plateau";
    private static final String ROTATE_LABEL = "R: tourner la tuile";
    private static final String PAUSE_LABEL = "Echap: mettre en pause";
    private static final String CENTER_LABEL = "C: centrer la vue";
    private static final String RESUME_BUTTON_LABEL = "Reprendre";
    private static final String QUIT_BUTTON_LABEL = "Quitter";
    private static final String RULES_BUTTON_LABEL = "Règles";

    private BoardView boardView;

    /**
     * Constructor for the PausePanel class.
     * Initializes the pause panel with the given board view.
     * @param boardView The board view to associate with the pause panel.
     */
    public PausePanel(BoardView boardView) {
        setSize(new Dimension(400, 400));
        this.boardView = boardView;
        this.setLayout(new GridLayout(9, 1));
        initializeComponents();
    }

    /**
     * Initializes the components of the pause panel.
     */
    private void initializeComponents() {
        addControlLabels();
        addButtons();
    }

    /**
     * Adds the control labels to the pause panel.
     */
    private void addControlLabels() {
        addLabel(LEFT_CLICK_LABEL);
        addLabel(RIGHT_CLICK_LABEL);
        addLabel(ROTATE_LABEL);
        addLabel(PAUSE_LABEL);
        addLabel(CENTER_LABEL);
    }

    /**
     * Adds a label with the given text to the pause panel.
     * @param text The text to display in the label.
     */
    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.BLACK); // Couleur du texte
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Police et taille du texte

        // Ajout des écouteurs pour changer l'apparence lors du survol
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                label.setForeground(Color.BLUE); // Changement de couleur lors du survol
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                label.setForeground(Color.BLACK); // Restauration de la couleur d'origine
            }
        });

        add(label);
    }

    /**
     * Adds the buttons to the pause panel.
     */
    private void addButtons() {
        addButton(RESUME_BUTTON_LABEL, e -> boardView.resumeGame());
        String soundButtonText = SettingsParser.soundEnabled() ? "Désactiver le son" : "Activer le son";
        addButton(soundButtonText, e -> toggleSound());
        addButton(RULES_BUTTON_LABEL, e -> UIFactory.showRulesPanel());
        addButton(QUIT_BUTTON_LABEL, e -> App.getInstance().appState.changeState(StartState.getInstance()));
    }

    /**
     * Adds a button with the given label and action listener to the pause panel.
     * @param label The label to display on the button.
     * @param actionListener The action listener to associate with the button.
     */
    private void addButton(String label, ActionListener actionListener) {
        JButton button = createButton(label, actionListener);
        add(button);
    }

    /**
     * Creates a styled button with the given label and action listener.
     * @param label The label to display on the button.
     * @param actionListener The action listener to associate with the button.
     * @return The created button.
     */
    private JButton createButton(String label, ActionListener actionListener) {
        JButton button = new JButton(label);
        button.addActionListener(actionListener);
        button.setPreferredSize(new Dimension(50, 50));
        button.setBackground(new Color(255, 215, 0));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));

        // Ajout des écouteurs pour changer l'apparence lors du survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 235, 59));
                button.setForeground(Color.BLACK);
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 235, 59), 2));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 215, 0));
                button.setForeground(Color.BLACK);
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
            }
        });
        return button;
    }

    private void updateButton() {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JButton button) {
                if (SettingsParser.soundEnabled() && button.getText().equals("Activer le son")){
                    button.setText("Désactiver le son");
                }
                else if (!SettingsParser.soundEnabled() && button.getText().equals("Désactiver le son")){
                    button.setText("Activer le son");
                }
            }
        }
    }

    /**
     * Toggles the sound on or off.
     */
    private void toggleSound() {
        SoundManager.invertSoundEnabled();
        if (SoundManager.isSoundEnabled()) {
            SoundManager.loopSound("game");
        } else {
            SoundManager.stopAllSounds();
        }
        updateButton();
    }
}
