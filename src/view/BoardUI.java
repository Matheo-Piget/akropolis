package view;

import java.awt.*;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Represents the ui of the game board.
 * Contains both the player name label, the rock label, and the remaining tiles label.
 */
public class BoardUI extends JPanel implements View{
    private PlayerLabel playerLabel = new PlayerLabel("Player");
    private RockLabel rockLabel = new RockLabel();
    private RemainingTilesLabel remainingTilesLabel = new RemainingTilesLabel();

    private float hue = 0.0f;
    private Timer timer;
    private Color bg = new Color(255,229,180);

    public BoardUI() {
        setOpaque(true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(100, 75));

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(playerLabel, gbc);
        gbc.gridx = 1;
        topPanel.add(rockLabel, gbc);

        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(remainingTilesLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        setBackground(bg);
        applyStyle();
    }

    @Override
    public void doLayout(){
        super.doLayout();
        if (timer == null) {
            timer = new Timer(70, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    hue += 0.005f;
                    if (hue > 1.0f) {
                        hue = 0.0f;
                    }
                    bg = Color.getHSBColor(hue, 0.5f, 0.5f);
                    setBackground(bg);
                    repaint();
                }
            });
            timer.start();
        }
    }

    public void setPlayer(String playerName){
        playerLabel.setPlayer(playerName);
    }

    public void setRock(int rock){
        rockLabel.setRocks(rock);
    }

    public void setRemainingTiles(int remainingTiles){
        remainingTilesLabel.setRemainingTiles(remainingTiles);
    }

    private void applyStyle() {
        // Style pour playerLabel
        playerLabel.setForeground(Color.BLUE); // Texte en bleu
        playerLabel.setFont(playerLabel.getFont().deriveFont(Font.BOLD, 16)); // Police en gras de taille 16

        // Style pour rockLabel
        rockLabel.setForeground(Color.RED); // Texte en rouge
        rockLabel.setFont(rockLabel.getFont().deriveFont(Font.PLAIN, 14)); // Police de taille 14

        // Style pour remainingTilesLabel
        remainingTilesLabel.setForeground(Color.GREEN.darker()); // Texte en vert foncé
        remainingTilesLabel.setFont(remainingTilesLabel.getFont().deriveFont(Font.ITALIC, 12)); // Police italique de taille 12
    }
}
