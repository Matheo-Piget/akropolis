package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.imageio.ImageIO;

public class MainMenuView extends JPanel {

    private BufferedImage backgroundImage;

    public MainMenuView() {
        super();
        // Charger l'image de fond
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/akropolisBG.jpg"));
            // Then we fill the background with the image
            setBackground(new Color(0, 0, 0, 0)); // Set the background to be transparent to draw the image in the
                                                  // paintComponent method
        } catch (IOException e) {
            e.printStackTrace();
            // We fill the background with a solid color if the image is not found
            setBackground(Color.GRAY);
        }

        setLayout(new GridBagLayout());

        AkropolisTitleLabel titleLabel = new AkropolisTitleLabel();
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.weightx = 1;
        titleGbc.weighty = 0.1; // Adjust this value to position the title higher or lower
        titleGbc.fill = GridBagConstraints.NONE;
        titleGbc.anchor = GridBagConstraints.NORTH; // This will position the title towards the top
        this.add(titleLabel, titleGbc);

        // Panneau pour les boutons avec un fond transparent
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0)); // 1 ligne, 2 colonnes, espace horizontal de 20

        // Bouton démarrer
        JButton startButton = new JButton("Démarrer");
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.addActionListener((ActionEvent e) -> startNewGame());

        //bouton regles
        JButton rulesButton = new JButton("Regles du jeu");
        rulesButton.setPreferredSize(new Dimension(150,50));
        rulesButton.addActionListener(e -> showRulesPanel());

        // Bouton quitter
        JButton quitButton = new JButton("Quitter");
        quitButton.setPreferredSize(new Dimension(150, 50));
        quitButton.addActionListener((ActionEvent e) -> System.exit(0));

        JButton creditsButton = new JButton("Crédits");
        creditsButton.addActionListener(e -> showCreditsPanel());
    


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
        this.add(buttonPanel, gbc);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void startNewGame() {
        System.out.println("Nouvelle partie démarrée");
        // Logique pour démarrer le jeu
        Object[] options = {"1 Joueur" , "2 Joueur" , "3 Joueur" , "4 Joueur"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choisissez le nombre de joeurs pour la partie", 
                "Nombre de Joueurs",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
            null,
                options,
                options[0]);

        switch (choice){
            case 0:
                System.out.println("Partie démarrée avec 1 joueur");
                break;
            case 1:
                System.out.println("Partie démarrée avec 2 joueur");
                break;
            case 2:
                System.out.println("Partie démarrée avec 3 joueur");
                break;
            case 3:
                System.out.println("Partie démarrée avec 4 joueur");
                break;
            default:
                System.out.println("Aucun choix fait, partie non démarrée");
                break;
        }
    }

    private void showRulesPanel(){
        // Création d'un nouveau dialogue 
        JDialog rulesDialog = new JDialog();
        rulesDialog.setTitle("Régles du jeu");
        rulesDialog.setSize(910 , 700 );
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
        mainPanel.add(scrollPane , BorderLayout.CENTER);

        JButton prevButton = new JButton("<");
        JButton nexButton = new JButton(">");

        final int[] currentIndex = {0};
        prevButton.addActionListener(e ->{
            // Décrémente l'index et met à jour l'image affichée quand le bouton précédent est cliqué.
            if(currentIndex[0]>0){
                currentIndex[0]--;
                imagLabel.setIcon(rulesImages.get(currentIndex[0]));
            }
        });

        nexButton.addActionListener(e -> {
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
    rightButtonPanel.add(nexButton, BorderLayout.CENTER);

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
        creditsPanel.add(new JLabel("    Développeur 1: CHETOUANI Bilal "));
        creditsPanel.add(new JLabel("    Développeur 2: BENZERDJEB Rayene"));
        creditsPanel.add(new JLabel("    Développeur 3: MOUSSA Nidhal"));
        creditsPanel.add(new JLabel("    Développeur 4: PIGET Matheo "));
        creditsPanel.add(new JLabel("    Développeur 5: GBAGUIDI Nerval "));
        
        // Ajoutez autant de JLabel que nécessaire pour les noms des membres de votre équipe
    
        // Ajout des panels au dialogue
        overlayPanel.add(creditsPanel, BorderLayout.CENTER);
        creditsDialog.add(overlayPanel);
    
        creditsDialog.setModal(true); // Bloque l'interaction avec la fenêtre principale
        creditsDialog.setVisible(true); // Affiche le dialogue des crédits
    }
    
}
