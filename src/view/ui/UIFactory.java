package view.ui;

import util.SoundManager;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    // To ensure that only one rules panel is open at a time
    private static boolean isRulesPanelOpen = false;
    // To store the rules dialog and bring it to the front when needed
    private static JDialog rulesDialog;
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
    public static JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
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
            SoundManager.playSound("gameButton2"); // pour Jouez l'effet sonore ici
            actionListener.actionPerformed(e);
        });
        return button;
    }

    /**
     * Shows the rules panel with the rules of the game.
     */
    public static void showRulesPanel() {
        if (isRulesPanelOpen) {
            // Display the previous rules panel if it is already open
            rulesDialog.toFront();
            return;
        }
        // Else, create a new rules panel
        rulesDialog = new JDialog();
        rulesDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                isRulesPanelOpen = false;
            }
        });
        isRulesPanelOpen = true;
        rulesDialog.setTitle("Règles du jeu");
        rulesDialog.setSize(910, 700);
        rulesDialog.setLocationRelativeTo(null);// Center the dialog on the screen
        JPanel mainPanel = new JPanel(new BorderLayout());
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
        // Use JScrollPane to allow scrolling through the images
        JScrollPane scrollPane = new JScrollPane(imagLabel);
        scrollPane.getVerticalScrollBar().setBackground(new Color(240, 240, 240)); 
        scrollPane.getVerticalScrollBar().setForeground(new Color(255, 215, 0)); 
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton prevButton = UIFactory.createStyledNavigationButton("<");
        JButton nextButton = UIFactory.createStyledNavigationButton(">");

        final int[] currentIndex = { 0 };
        prevButton.addActionListener(e -> {
            // Update the displayed image when the previous button is clicked
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                imagLabel.setIcon(rulesImages.get(currentIndex[0]));
            }
        });

        nextButton.addActionListener(e -> {
            // Update the displayed image when the next button is clicked
            if (currentIndex[0] < rulesImages.size() - 1) {
                currentIndex[0]++;
                imagLabel.setIcon(rulesImages.get(currentIndex[0]));
            }
        });

        // Setup the layout for the buttons
        JPanel leftButtonPanel = new JPanel(new BorderLayout());
        leftButtonPanel.add(prevButton, BorderLayout.CENTER);

        JPanel rightButtonPanel = new JPanel(new BorderLayout());
        rightButtonPanel.add(nextButton, BorderLayout.CENTER);

        mainPanel.add(leftButtonPanel, BorderLayout.WEST);
        mainPanel.add(rightButtonPanel, BorderLayout.EAST);

        rulesDialog.add(mainPanel); 
        rulesDialog.setVisible(true);
    }
}
