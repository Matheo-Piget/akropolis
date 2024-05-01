package view.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import view.View;

/**
 * Represents the ui of the game board.
 * Contains both the player name label, the rock label, and the remaining tiles label.
 */
public class BoardUI extends JPanel implements View {
    private PlayerLabel playerLabel = new PlayerLabel("Player");
    private final ArrayList<Color> playerColors = new ArrayList<Color>();
    private ArrayList<ImageIcon > playerIcons = new ArrayList<ImageIcon>();
    private ScoreLabel scorelabel = new ScoreLabel(0);
    private RemainingTilesLabel remainingTilesLabel = new RemainingTilesLabel();
    private ImageIcon playerIcon = null;
    private JLabel playerImageLabel = new JLabel(playerIcon);
    private RockLabel rockLabel = new RockLabel();
    private ImageIcon rockIcon;
    private JLabel rockImageLabel = new JLabel();
    private boolean firstTurn = true;
    private JProgressBar remainingTilesBar ;

    /**
     * Constructor for the BoardUI class.
     * Initializes the player name label, the rock label, and the remaining tiles label.
     */
    public BoardUI(int numberOfPlayers) {
        setOpaque(true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(100, 75));
        playerColors.add(Color.BLUE);
        playerColors.add(Color.RED);
        playerColors.add(Color.GREEN.darker());
        playerColors.add(Color.MAGENTA);
        try{
            // Load all the player icons for each player
            for (int i = 0; i < numberOfPlayers; i++){
                Image icon = ImageIO.read(getClass().getResource("/Icons_0" + (i + 1) + ".png"));
                playerIcons.add(new ImageIcon(icon));
            }
            playerImageLabel = new JLabel(playerIcons.get(0));
            playerLabel.add(playerImageLabel);
            Image img2 = ImageIO.read(getClass().getResource("/rock.png")).getScaledInstance(60, 60, Image.SCALE_DEFAULT);
            rockIcon = new ImageIcon(img2);
            rockImageLabel = new JLabel(rockIcon);
            
        } catch (Exception e){
            e.printStackTrace();
        }

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        playerLabel.setBorder(new EmptyBorder(0, 0, 0, 450));
        playerImageLabel.setBorder(new EmptyBorder(0, 0, 0, 600));
        topPanel.add(playerLabel, gbc);        
        topPanel.add(playerImageLabel, gbc);        
        gbc.gridx = 2;
        rockImageLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(rockImageLabel, gbc);
        topPanel.add(rockLabel, gbc);
        remainingTilesBar = createRemainingTilesBar();
        gbc.gridx = 3;
        topPanel.add(remainingTilesBar, gbc);
        topPanel.add(remainingTilesLabel, gbc);
        scorelabel.setBorder(new EmptyBorder(0, 150, 0, 0));
        scorelabel.setFont(new Font("Serif", Font.BOLD, 19)); 

        gbc.gridx = 0;
        topPanel.add(scorelabel, gbc);

        add(topPanel, BorderLayout.NORTH);
    
        setBackground(playerColors.get(0));
        applyStyle();
    }

    private JProgressBar createRemainingTilesBar() {
        JProgressBar bar = new JProgressBar();
        bar.setMinimum(0);
        bar.setMaximum(57); 
        bar.setStringPainted(true);
        bar.setFont(new Font("Serif", Font.BOLD, 16)); 
        bar.setForeground(Color.GRAY); 
        bar.setBackground(Color.WHITE); 
        return bar;
    }

    public void setRemainingTiles(int remainingTiles){
        remainingTilesBar.setValue(remainingTiles);
        remainingTilesBar.setString("Tuiles restantes: " + remainingTiles);
    }

    public void setPlayer(String playerName){
        playerLabel.setPlayer(playerName);
        System.out.println(playerName);
        if (playerIcons.size() > 0 && !firstTurn){
            nextPlayerIcon();
            setBackground(playerColors.get(playerIcons.indexOf(playerImageLabel.getIcon())));
        }
        else{
            firstTurn = false;
        }
    }

    private void nextPlayerIcon(){
        int index = playerIcons.indexOf(playerImageLabel.getIcon());
        index = (index + 1) % playerIcons.size();
        playerImageLabel.setIcon(playerIcons.get(index));
        playerImageLabel.repaint();
    }

    public void setScore(int score , int arg1, int arg2 , int arg3 , int arg4, int arg5, int arg11, int arg22 , int arg33 , int arg44, int arg55) {
        scorelabel.setScore(score, arg1, arg2, arg3, arg4, arg5, arg11, arg22, arg33, arg44, arg55);
    }

    public void setRock(int rock){
        rockLabel.setRocks(rock);
    }

    private void applyStyle() {
        // Style pour playerLabel
        playerLabel.setForeground(Color.WHITE);
        playerLabel.setFont(playerLabel.getFont().deriveFont(Font.BOLD, 16)); // Police en gras de taille 16
        // Style pour remainingTilesLabel
        remainingTilesLabel.setForeground(Color.WHITE);
        remainingTilesLabel.setFont(remainingTilesLabel.getFont().deriveFont(Font.ITALIC, 12)); // Police italique de taille 12
    }
}
