package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;

import view.main.App;
import view.main.states.StartState;

public class PausePanel extends JPanel {
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
        this.setLayout(new GridLayout(8, 1));
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
                label.setForeground(Color.YELLOW); // Changement de couleur lors du survol
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
        addButton(QUIT_BUTTON_LABEL, e -> App.getInstance().appState.changeState(StartState.getInstance()));
        addButton(RULES_BUTTON_LABEL, e -> showRulesPanel());
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

    /**
     * Shows the rules panel with the rules of the game.
     */
    private void showRulesPanel() {
        // Création d'un nouveau dialogue
        JDialog rulesDialog = new JDialog();
        rulesDialog.setTitle("Règles du jeu");
        rulesDialog.setSize(910, 700);
        rulesDialog.setLocationRelativeTo(null);// Centre le dialogue sur l'écran.
        // Crée un panel principal utilisant BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        // liste pour stocker les images des règles du jeu.
        List<ImageIcon> rulesImages = new ArrayList<>();
        for (int i = 7; i >= 1; i--) {
            try {
                BufferedImage img = ImageIO
                        .read(Objects.requireNonNull(getClass().getResourceAsStream("/regles" + i + ".png")));
                ImageIcon icon = new ImageIcon(img);
                rulesImages.add(icon);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Erreur lors du chargement de l'image /res/regles" + i + ".png");
            }
        }
        JLabel imagLabel = new JLabel();
        if (!rulesImages.isEmpty()) {
            imagLabel.setIcon(rulesImages.get(0)); // Affiche la première image
        }
        // Utilise un JScrollPane pour permettre le défilement si l'image est plus
        // grande que le panel.
        JScrollPane scrollPane = new JScrollPane(imagLabel);
        scrollPane.getVerticalScrollBar().setBackground(new Color(240, 240, 240)); // Couleur de fond de la barre de
        // défilement
        scrollPane.getVerticalScrollBar().setForeground(new Color(255, 215, 0)); // Couleur de la barre de défilement
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0)); // Taille de la barre de défilement

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton prevButton = createStyledNavigationButton("<");
        JButton nextButton = createStyledNavigationButton(">");

        final int[] currentIndex = { 0 };
        prevButton.addActionListener(e -> {
            // Décrémente l'index et met à jour l'image affichée quand le bouton précédent
            // est cliqué.
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                imagLabel.setIcon(rulesImages.get(currentIndex[0]));
            }
        });

        nextButton.addActionListener(e -> {
            // incrémente l'index et met à jour l'image affichée quand le bouton précédent
            // est cliqué.
            if (currentIndex[0] < rulesImages.size() - 1) {
                currentIndex[0]++;
                imagLabel.setIcon(rulesImages.get(currentIndex[0]));
            }
        });

        // Panels pour positionner les boutons sur les côtés de l'image
        JPanel leftButtonPanel = new JPanel(new BorderLayout());
        leftButtonPanel.add(prevButton, BorderLayout.CENTER);

        JPanel rightButtonPanel = new JPanel(new BorderLayout());
        rightButtonPanel.add(nextButton, BorderLayout.CENTER);

        // Ajoute les boutons de navigation à gauche et à droite de l'image
        mainPanel.add(leftButtonPanel, BorderLayout.WEST);
        mainPanel.add(rightButtonPanel, BorderLayout.EAST);

        rulesDialog.add(mainPanel); // Ajoute le panel principal au dialogue
        rulesDialog.setVisible(true);
    }

    /**
     * Creates a styled navigation button with the given text.
     * @param text The text to display on the button.
     * @return The created button.
     */
    private JButton createStyledNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(50, 50));
        button.setBackground(new Color(255, 215, 0));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 235, 59));
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 215, 0));
                button.setForeground(Color.BLACK);
            }
        });
        return button;
    }
}
