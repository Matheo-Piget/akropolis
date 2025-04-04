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
import java.util.Objects;
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
    private ScoreDetails scoreDetails;
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
    public BoardUI(int numberOfPlayers, ScoreDetails scoreDetails) {
        setOpaque(true);
        this.scoreDetails = scoreDetails;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(100, 75));
        playerColors.add(Color.BLUE);
        playerColors.add(Color.RED);
        playerColors.add(Color.GREEN.darker());
        playerColors.add(Color.MAGENTA);
        try{
            // Load all the player icons for each player
            for (int i = 0; i < numberOfPlayers; i++){
                Image icon = ImageIO.read(Objects.requireNonNull(getClass().getResource("/playerIcons/Icons_0" + (i + 1) + ".png")));
                playerIcons.add(new ImageIcon(icon));
            }
            playerImageLabel = new JLabel(playerIcons.get(0));
            playerLabel.add(playerImageLabel);
            Image img2 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/rock.png"))).getScaledInstance(60, 60, Image.SCALE_DEFAULT);
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
        remainingTilesBar = createRemainingTilesBar(numberOfPlayers);
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

    /**
     * Creates a progress bar to display the remaining tiles.
     * @return the progress bar
     */
    private JProgressBar createRemainingTilesBar(int numberOfPlayers) {
        JProgressBar bar = new JProgressBar();
        bar.setMinimum(0);
        int max = switch (numberOfPlayers) {
            case 1, 2 -> 27;
            case 3 -> 36;
            default -> 55;
        };
        bar.setMaximum(max); 
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

    /**
     * Sets the player name label to the specified player name.
     * @param playerName the name of the player
     */
    public void setPlayer(String playerName){
        playerLabel.setPlayer(playerName);
        if (playerIcons.size() > 0 && !firstTurn){
            nextPlayerIcon();
            setBackground(playerColors.get(playerIcons.indexOf(playerImageLabel.getIcon())));
        }
        else{
            firstTurn = false;
        }
    }

    /**
     * Changes the player icon to the next one in the list.
     */
    private void nextPlayerIcon(){
        int index = playerIcons.indexOf(playerImageLabel.getIcon());
        index = (index + 1) % playerIcons.size();
        playerImageLabel.setIcon(playerIcons.get(index));
        playerImageLabel.repaint();
    }

    public void setScore(int score , int[] scores, int[] mult){
        scorelabel.setScore(score);
        scoreDetails.updateScoreInfo(scores, mult);;
    }

    public void setRock(int rock){
        rockLabel.setRocks(rock);
    }

    /**
     * Applies the style to the player name label and the remaining tiles label.
     */
    private void applyStyle() {
        // Style pour playerLabel
        playerLabel.setForeground(Color.WHITE);
        playerLabel.setFont(playerLabel.getFont().deriveFont(Font.BOLD, 16));
        // Style pour remainingTilesLabel
        remainingTilesLabel.setForeground(Color.WHITE);
        remainingTilesLabel.setFont(remainingTilesLabel.getFont().deriveFont(Font.ITALIC, 12)); 
    }
}
