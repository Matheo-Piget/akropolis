package view;

import view.main.App;
import view.main.states.PlayingState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MainMenuView extends JPanel {

    private BufferedImage backgroundImage;

    public MainMenuView() {
        super();
        // Charger l'image de fond
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/akropolisBG.jpg"));
            // Rendre le fond transparent pour afficher l'image
            setOpaque(false);
        } catch (IOException e) {
            e.printStackTrace();
            // Remplacer l'image manquante par une couleur de fond grise
            setBackground(Color.GRAY);
        }

        setLayout(new GridBagLayout());

        addTitleLabel();
        addButtonsPanel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dessiner l'image de fond
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void addTitleLabel() {
        AkropolisTitleLabel titleLabel = new AkropolisTitleLabel();
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.weightx = 1;
        titleGbc.weighty = 0.1;
        titleGbc.fill = GridBagConstraints.NONE;
        titleGbc.anchor = GridBagConstraints.NORTH;
        add(titleLabel, titleGbc);
    }

    private void addButtonsPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setOpaque(false); // Rendre le panneau transparent

        // Bouton démarrer
        JButton startButton = createStyledButton("Démarrer", e -> startNewGame());
        // Bouton règles
        JButton rulesButton = createStyledButton("Règles du jeu", e -> showRulesPanel());
        // Bouton crédits
        JButton creditsButton = createStyledButton("Crédits", e -> showCreditsPanel());
        // Bouton quitter
        JButton quitButton = createStyledButton("Quitter", e -> System.exit(0));

        // Ajouter les boutons au panneau de boutons
        buttonPanel.add(startButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(creditsButton);
        buttonPanel.add(quitButton);

        // Contraintes pour centrer le panneau de boutons dans l'arrière-plan
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Ajouter le panneau de boutons au panneau d'arrière-plan avec les contraintes
        add(buttonPanel, gbc);
    }

    private JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
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
        // Ajouter l'écouteur d'événements
        button.addActionListener(actionListener);
        return button;
    }
    private void startNewGame() {
        System.out.println("Nouvelle partie démarrée");

        // Utilisation de JComboBox pour sélectionner le nombre de joueurs
        JComboBox<Integer> playerNumberComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        playerNumberComboBox.setSelectedIndex(0); // Sélectionne la première option par défaut

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dialogPanel.add(new JLabel("Choisissez le nombre de joueurs pour la partie:"), BorderLayout.NORTH);
        dialogPanel.add(playerNumberComboBox, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this,
                dialogPanel,
                "Nombre de Joueurs",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            int numberOfPlayers = (int) playerNumberComboBox.getSelectedItem();
            List<String> playerNames = new ArrayList<>();

            for (int i = 1; i <= numberOfPlayers; i++) {
                playerNames.add(collectPlayerName(i));
            }

            if (!playerNames.isEmpty()) {
                PlayingState playingState = PlayingState.getInstance();
                playingState.setPlayers(playerNames);
                App.getInstance().appState.changeState(playingState);
            }
        } else {
            System.out.println("Aucun choix fait, partie non démarrée");
            JOptionPane.showMessageDialog(this, "Aucune sélection effectuée. La partie ne démarrera pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    private String collectPlayerName(int playerNumber) {
        String playerName;
        do {
            playerName = JOptionPane.showInputDialog(this, "Entrez le nom du joueur " + playerNumber + " :", "Nom du Joueur", JOptionPane.PLAIN_MESSAGE);
            if (playerName == null || playerName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom du joueur ne peut pas être vide.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        } while (playerName == null || playerName.trim().isEmpty());
        return playerName.trim();
    }

    private void showRulesPanel() {
        // Création d'un nouveau dialogue
        JDialog rulesDialog = new JDialog();
        rulesDialog.setTitle("Règles du jeu");
        rulesDialog.setSize(910, 700);
        rulesDialog.setLocationRelativeTo(null);// Centre le dialogue sur l'écran.
        // Crée un panel principal utilisant BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        //liste pour stocker les images des règles du jeu.
        List<ImageIcon> rulesImages= new ArrayList<>();
        for(int i = 7; i >= 1; i--) {
            try {
                BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/regles" + i + ".png"));
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
        // Utilise un JScrollPane pour permettre le défilement si l'image est plus grande que le panel.
        JScrollPane scrollPane = new JScrollPane(imagLabel);
        scrollPane.getVerticalScrollBar().setBackground(new Color(240, 240, 240)); // Couleur de fond de la barre de défilement
        scrollPane.getVerticalScrollBar().setForeground(new Color(255, 215, 0)); // Couleur de la barre de défilement
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0)); // Taille de la barre de défilement

        mainPanel.add(scrollPane , BorderLayout.CENTER);

        JButton prevButton = createStyledNavigationButton("<");
        JButton nextButton = createStyledNavigationButton(">");

        final int[] currentIndex = {0};
        prevButton.addActionListener(e ->{
            // Décrémente l'index et met à jour l'image affichée quand le bouton précédent est cliqué.
            if(currentIndex[0]>0){
                currentIndex[0]--;
                imagLabel.setIcon(rulesImages.get(currentIndex[0]));
            }
        });

        nextButton.addActionListener(e -> {
            // incrémente l'index et met à jour l'image affichée quand le bouton précédent est cliqué.
            if(currentIndex[0] < rulesImages.size() - 1) {
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

    private void showCreditsPanel() {
        // Création du dialogue pour les crédits
        JDialog creditsDialog = new JDialog();
        creditsDialog.setTitle("Crédits");
        creditsDialog.setSize(400, 300); // Taille ajustable selon vos besoins
        creditsDialog.setLocationRelativeTo(this); // Centre par rapport à MainMenuView
        creditsDialog.setLayout(new BorderLayout());
    
        // Panel semi-transparent qui assombrit l'arrière-plan
        JPanel overlayPanel = new JPanel();
        overlayPanel.setLayout(new BorderLayout());
        overlayPanel.setBackground(new Color(0, 0, 0, 150)); // Couleur semi-transparente
    
        // Panel pour les noms des développeurs
        JPanel creditsPanel = new JPanel();
        creditsPanel.setLayout(new GridLayout(0, 1)); // Disposition en colonne pour les noms
        // Ajoutez ici les noms des membres de l'équipe
        creditsPanel.add(createStyledCreditLabel("Développeur 1: CHETOUANI Bilal"));
        creditsPanel.add(createStyledCreditLabel("Développeur 2: BENZERDJEB Rayene"));
        creditsPanel.add(createStyledCreditLabel("Développeur 3: MOUSSA Nidhal"));
        creditsPanel.add(createStyledCreditLabel("Développeur 4: PIGET Matheo"));
        creditsPanel.add(createStyledCreditLabel("Développeur 5: GBAGUIDI Nerval"));

        // Ajoutez autant de JLabel que nécessaire pour les noms des membres de votre équipe
    
        // Ajout des panels au dialogue
        overlayPanel.add(creditsPanel, BorderLayout.CENTER);
        creditsDialog.add(overlayPanel);
    
        creditsDialog.setModal(true); // Bloque l'interaction avec la fenêtre principale
        creditsDialog.setVisible(true); // Affiche le dialogue des crédits
    }

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

    private JLabel createStyledCreditLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        return label;
    }
    
}
