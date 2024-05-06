package view.ui;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import view.SoundEffect;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * This class is a factory for creating UI elements.
 * It is used to create UI elements for the game.
 */
public class UIFactory {
    private static final SoundEffect buttonClickSound = new SoundEffect("/sound/GameButton.wav");
    /**
     * Create a styled navigation button
     *
     * @param text  the text to display on the button
     * @return a styled navigation button
     */
    public static JButton createStyledNavigationButton(String text) {
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

    /**
     * Create a styled button
     *
     * @param text           the text to display on the button
     * @param actionListener the action listener for the button
     * @return a styled button
     */
    public static JButton createStyledButton(String text, Dimension dimension, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setPreferredSize(dimension);
        button.setBackground(new Color(255, 215, 0));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 235, 59));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 215, 0));
            }
        });
        // Ajout l'écouteur d'événements
        button.addActionListener(e -> {
            buttonClickSound.play(); // pour Jouez l'effet sonore ici
            actionListener.actionPerformed(e);
        });
        return button;
    }

    /**
     * Shows the rules panel with the rules of the game.
     */
    public static void showRulesPanel() {
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
                        .read(Objects.requireNonNull(UIFactory.class.getResourceAsStream("/rules/regles" + i + ".png")));
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

        JButton prevButton = UIFactory.createStyledNavigationButton("<");
        JButton nextButton = UIFactory.createStyledNavigationButton(">");

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
}
