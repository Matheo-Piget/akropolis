package view.panel;

import util.SoundManager;
import view.main.App;
import view.main.states.PlayingState;
import view.ui.UIFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.Box;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.imageio.ImageIO;
import java.awt.CardLayout;

/**
 * Represents the main menu view.
 * This class is used to display the main menu of the game.
 */
public class MainMenuView extends JPanel {

    private final JPanel choicePanel = new JPanel(new GridBagLayout());
    private BufferedImage backgroundImage;
    private final CardLayout switcher = new CardLayout();
    private AkropolisTitleLabel titleLabel;

    public MainMenuView() {
        super();
        this.setLayout(switcher);
        // load the background image
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/menu/akropolisBG.jpg")));
            // Set the panel to be transparent
            setOpaque(false);
        } catch (IOException e) {
            // If the image is not found, set the background to gray
            setBackground(Color.GRAY);
        }
        SoundManager.loopSound("menu");
        choicePanel.setOpaque(false);
        add(choicePanel, "choicePanel");
        add(new SettingsPanel(switcher), "settingsPanel");
        switcher.show(this, "choicePanel");
        addTitleLabel();
        addButtonsPanel();

    }

    /**
     * Set the background image
     *
     * @param g the graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dessiner l'image de fond
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    /**
     * Stop the music when the panel is removed
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        SoundManager.stopSound("menu");
        titleLabel.stop();
    }

    /**
     * Add the title label to the panel
     */
    private void addTitleLabel() {
        titleLabel = new AkropolisTitleLabel();
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.weightx = 1;
        titleGbc.weighty = 0.1;
        titleGbc.fill = GridBagConstraints.NONE;
        titleGbc.anchor = GridBagConstraints.NORTH;
        choicePanel.add(titleLabel, titleGbc);
    }

    private void addButtonsPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2,2, 20, 20));
        buttonPanel.setOpaque(false);
    
        JButton startButton = UIFactory.createStyledButton("Démarrer", e -> startNewGame());
        JButton rulesButton = UIFactory.createStyledButton("Règles du jeu", e -> UIFactory.showRulesPanel());
        JButton creditsButton = UIFactory.createStyledButton("Crédits", e -> showCreditsPanel());
        JButton quitButton = UIFactory.createStyledButton("Quitter", e -> System.exit(0));
    
        buttonPanel.add(rulesButton);
        buttonPanel.add(startButton);
        buttonPanel.add(creditsButton);
        buttonPanel.add(quitButton);
    
        buttonPanel.add(Box.createVerticalStrut(10));
    
        JButton settingsButton = UIFactory.createStyledButton("Paramètres", e -> switcher.show(this, "settingsPanel"));
        buttonPanel.add(settingsButton);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
    
        choicePanel.add(buttonPanel, gbc);
    }

    /**
     * Start a new game
     */
    private void startNewGame() {
        // Utilisation de JComboBox pour sélectionner le nombre de joueurs
        JComboBox<Integer> playerNumberComboBox = new JComboBox<>(new Integer[] { 1, 2, 3, 4 });
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
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (playerNumberComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Aucune sélection effectuée. La partie ne démarrera pas.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int numberOfPlayers = (int) playerNumberComboBox.getSelectedItem();
            List<String> playerNames = new ArrayList<>();

            for (int i = 1; i <= numberOfPlayers; i++) {
                String playerName = collectPlayerName(i);
                if (playerName == null) {
                    // That means the user clicked cancel
                    // So we should stop the game creation process
                    return;
                }
                playerNames.add(playerName);
            }
            if (!playerNames.isEmpty()) {
                PlayingState playingState = PlayingState.getInstance();
                playingState.setPlayers(playerNames);
                App.getInstance().appState.changeState(playingState);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Aucune sélection effectuée. La partie ne démarrera pas.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Collect the player name
     *
     * @param playerNumber the player number
     * @return the player name
     */
    private String collectPlayerName(int playerNumber) {
        String playerName;
        do {
            playerName = JOptionPane.showInputDialog(this, "Entrez le nom du joueur " + playerNumber + " :",
                    "Nom du Joueur", JOptionPane.PLAIN_MESSAGE);
            if (playerName == null) {
                return null;
            } else if (playerName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom du joueur ne peut pas être vide.", "Erreur",
                        JOptionPane.WARNING_MESSAGE);
                playerName = null;
            } else if (!playerName.matches("[a-zA-Z0-9]{1,10}")) {
                JOptionPane.showMessageDialog(this,
                        "Le nom du joueur ne peut contenir que des caractères alphanumériques et doit être de 10 caractères maximum.",
                        "Erreur",
                        JOptionPane.WARNING_MESSAGE);
                playerName = null;
            }
        } while (playerName == null);
        return playerName.trim();
    }

    /**
     * showCreditsPanel
     */
    private void showCreditsPanel() {
        JDialog creditsDialog = new JDialog();
        creditsDialog.setTitle("Crédits");
        creditsDialog.setSize(400, 300); 
        creditsDialog.setLocationRelativeTo(this); 
        creditsDialog.setLayout(new BorderLayout());

        // Semi-transparent overlay panel
        JPanel overlayPanel = new JPanel();
        overlayPanel.setLayout(new BorderLayout());
        overlayPanel.setBackground(new Color(0, 0, 0, 150)); 

        JPanel creditsPanel = new JPanel();
        creditsPanel.setLayout(new GridLayout(0, 1)); 
        creditsPanel.add(createStyledCreditLabel("Développeur 1: CHETOUANI Bilal"));
        creditsPanel.add(createStyledCreditLabel("Développeur 2: BENZERDJEB Rayene"));
        creditsPanel.add(createStyledCreditLabel("Développeur 3: MOUSSA Nidhal"));
        creditsPanel.add(createStyledCreditLabel("Développeur 4: PIGET Matheo"));
        creditsPanel.add(createStyledCreditLabel("Développeur 5: GBAGUIDI Nerval"));


        overlayPanel.add(creditsPanel, BorderLayout.CENTER);
        creditsDialog.add(overlayPanel);

        creditsDialog.setModal(true); // Stop interaction with MainMenuView
        creditsDialog.setVisible(true); // Show the credits dialog
    }

    private JLabel createStyledCreditLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        return label;
    }

}
