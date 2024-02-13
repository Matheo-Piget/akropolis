package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainView extends JFrame {

    private Image backgroundImage;

    // Constructeur de MainView
    public MainView() {
        // Configuration de base de la fenêtre
        setTitle("Akropolis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Charger l'image de fond
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/resources/Akropolis.png"));
        } catch (IOException e) {
            e.printStackTrace();
            getContentPane().setBackground(Color.LIGHT_GRAY);
        }

        // Panneau personnalisé pour l'arrière-plan
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); // Utiliser GridBagLayout pour plus de contrôle
        setContentPane(backgroundPanel);

        // Panneau pour les boutons avec un fond transparent
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0)); // 1 ligne, 2 colonnes, espace horizontal de 20

        // Bouton démarrer
        JButton startButton = new JButton("Démarrer");
        startButton.setPreferredSize(new Dimension(150, 50)); // Définir la taille préférée du bouton
        startButton.addActionListener((ActionEvent e) -> startNewGame());

        // Bouton quitter
        JButton quitButton = new JButton("Quitter");
        quitButton.setPreferredSize(new Dimension(150, 50)); // Définir la taille préférée du bouton
        quitButton.addActionListener((ActionEvent e) -> System.exit(0));

        // Ajouter les boutons au panneau de boutons
        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);

        // Contraintes pour centrer le panneau de boutons dans l'arrière-plan
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE; // Ne pas redimensionner les boutons pour remplir l'espace

        // Ajouter le panneau de boutons au panneau d'arrière-plan avec les contraintes
        backgroundPanel.add(buttonPanel, gbc);
    }

    private void startNewGame() {
        System.out.println("Nouvelle partie démarrée");
        // Logique pour démarrer le jeu
    }

    // Méthode principale pour exécuter l'application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
