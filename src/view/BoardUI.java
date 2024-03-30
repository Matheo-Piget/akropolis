package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * Represents the ui of the game board.
 * Contains both the player name label, the rock label, and the remaining tiles label.
 */
public class BoardUI extends JPanel implements View{
    private PlayerLabel playerLabel = new PlayerLabel("Player");
    private RemainingTilesLabel remainingTilesLabel = new RemainingTilesLabel();
    private ArrayList<JLabel> rockImages = new ArrayList<>();


    private float hue = 0.0f;
    private Timer timer;
    private Color bg = new Color(255,229,180);

    /**
     * created a BoardUI that contains player info and stack infos
     */
    public BoardUI() {
        setOpaque(true);
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        setLayout(layout);
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
        gbc.gridx = 2;
        topPanel.add(remainingTilesLabel, gbc);

        add(topPanel, BorderLayout.NORTH);
    

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
        while (rockImages.size() > 3) {// 3 pour tester
            // Supprimez une image si une pierre est perdue
            remove(rockImages.remove(rockImages.size() - 1));
        }
        while (rockImages.size() < 3) {
            ImageIcon image = new ImageIcon("res\\rock.PNG");
            Image scaledImage = image.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            JLabel newRockImage = new JLabel(new ImageIcon(scaledImage));
            newRockImage.setBorder(new EmptyBorder(0, 0, 30, 0)); // Ajoutez un bord vide en bas

            rockImages.add(newRockImage);
            add(newRockImage);
        }
        validate();
        repaint();
        revalidate(); // Réorganisez les composants

    }
    

    public void setRemainingTiles(int remainingTiles){
        remainingTilesLabel.setRemainingTiles(remainingTiles);
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
