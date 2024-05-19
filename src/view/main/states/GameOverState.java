package view.main.states;

import util.State;
import util.Pair;
import model.LeaderBoard;
import util.Timeline;
import view.main.App;
import view.ui.UIFactory;
import java.util.LinkedHashMap;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;
import java.awt.GridLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyListener;

/**
 * Represents the game over state.
 * This class is used to display the game over screen.
 */
public class GameOverState extends State {

    private static final GameOverState INSTANCE = new GameOverState();
    private Timeline timeline;
    private JPanel blackScreen;
    private List<Pair<String, Integer>> ranking;

    public static GameOverState getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        blackScreen = new JPanel(new BorderLayout());
        blackScreen.setSize(App.getInstance().getScreen().getSize());
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                // Skip the logo animation if a key is pressed
                System.out.println("Key pressed, skipping logo animation");
                // Remove the key listener
                blackScreen.removeKeyListener(this);
                handleExit();
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
            }
        };
        blackScreen.setFocusable(true);
        blackScreen.setOpaque(true);
        blackScreen.requestFocus();
        blackScreen.requestFocusInWindow();
        blackScreen.addKeyListener(keyListener);
        blackScreen.setBackground(java.awt.Color.BLACK);
        App.getInstance().getScreen().add(blackScreen, java.awt.BorderLayout.CENTER);
        App.getInstance().getScreen().validate();
        JLabel gameFininshed = new JLabel("Les jeux sont faits !");
        customizeLabel(gameFininshed, 30, new java.awt.Color(255, 255, 255, 0), Font.BOLD);
        String labelText = ranking.size() == 1 ? "Avons-nous un nouveau record ?" : "Le gagnant est : " + ranking.get(0).getKey() + " !";
        JLabel logo = new JLabel(labelText);
        // White transparent color
        logo.setForeground(new java.awt.Color(255, 255, 255, 0));
        logo.setHorizontalAlignment(JLabel.CENTER);
        logo.setVerticalAlignment(JLabel.CENTER);
        blackScreen.add(gameFininshed, java.awt.BorderLayout.CENTER);
        blackScreen.revalidate();
        logo.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 25));
        timeline = new Timeline(0);
        timeline.addKeyFrame(new Timeline.KeyFrame(15, 100, e -> {
            // Increase the label's opacity by a step each time the action is performed
            int alpha = gameFininshed.getForeground().getAlpha();
            alpha = Math.min(alpha + 255 / 50, 255);
            gameFininshed.setForeground(new java.awt.Color(logo.getForeground().getRed(), logo.getForeground().getGreen(),
                    logo.getForeground().getBlue(), alpha));
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(15, 50, e -> {
            // Decrease the label's opacity by a step each time the action is performed
            int alpha = gameFininshed.getForeground().getAlpha();
            alpha = Math.max(alpha - 255 / 50, 0);
            gameFininshed.setForeground(new java.awt.Color(logo.getForeground().getRed(), gameFininshed.getForeground().getGreen(),
                    gameFininshed.getForeground().getBlue(), alpha));
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(1000, 1, e -> {
            // Remove the game finished label
            if (gameFininshed.getParent() != null) {
                blackScreen.remove(gameFininshed);
            }
            // Add the logo label
            SwingUtilities.invokeLater(() -> blackScreen.add(logo, java.awt.BorderLayout.CENTER));
            blackScreen.revalidate();
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(15, 50, e -> {
            // Increase the label's opacity by a step each time the action is performed
            int alpha = logo.getForeground().getAlpha();
            alpha = Math.min(alpha + 255 / 50, 255);
            logo.setForeground(new java.awt.Color(logo.getForeground().getRed(), logo.getForeground().getGreen(),
                    logo.getForeground().getBlue(), alpha));
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(1000, 1, e -> {
            // Just wait
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(15, 50, e -> {
            // Decrease the label's opacity by a step each time the action is performed
            int alpha = logo.getForeground().getAlpha();
            alpha = Math.max(alpha - 255 / 50, 0);
            logo.setForeground(new java.awt.Color(logo.getForeground().getRed(), logo.getForeground().getGreen(),
                    logo.getForeground().getBlue(), alpha));
        }));
        timeline.setOnFinished(e -> handleExit());
        timeline.start();
    }

    @Override
    public void exit() {
        // Remove all components from the screen
        App.getInstance().getScreen().removeAll();
    }

    /**
     * Handles the exit of the state
     */
    private void handleExit() {
        timeline.stop();
        blackScreen.removeAll();

        blackScreen.setLayout(new GridLayout(1, 3));

        String category = switch (ranking.size()) {
            case 1 -> "ONE_PLAYER";
            case 2 -> "TWO_PLAYER";
            case 3 -> "THREE_PLAYER";
            default -> "FOUR_PLAYER";
        };

        // Création du panneau de classement à gauche
        JPanel rankingPanel = createRankingPanel();
        blackScreen.add(rankingPanel);

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setOpaque(true);
        middlePanel.setBackground(Color.BLACK);
        JButton button = createButton();

        middlePanel.add(button, BorderLayout.SOUTH);
        blackScreen.add(middlePanel);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();

        // A custom panel to display the trophy animation because GIFs lags and have visual artefacts sometimes
        class ImagePanel extends JPanel {
            private VolatileImage vImg;
        
            public ImagePanel() {
                this.setOpaque(false);
            }
        
            public void setImage(BufferedImage img) {
                if (vImg != null) {
                    vImg.flush();
                }
                vImg = gc.createCompatibleVolatileImage(img.getWidth(), img.getHeight(), Transparency.TRANSLUCENT);
                Graphics2D g = vImg.createGraphics();
                g.setComposite(java.awt.AlphaComposite.Src);
                g.drawImage(img, 0, 0, this);
                g.dispose();
            }
        
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (vImg != null) {
                    g.drawImage(vImg, 0, 0, this);
                }
            }
        }        

        try {
            // GIF create visual artefacts so WE WILL BRUTE FORCE IT USING A TIMER AND A LOT OF IMAGES HAHAHA
            ImagePanel trophyLabel = new ImagePanel();
            trophyLabel.setOpaque(false);
            BufferedImage[] images = new BufferedImage[40];
            for (int i = 0; i < 40; i++) {
                try {
                    images[i] = ImageIO.read(getClass().getResource("/menu/animTrophy/frame" + (i + 1) + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            trophyLabel.setSize(images[0].getWidth(), images[0].getHeight());
            middlePanel.add(trophyLabel, BorderLayout.CENTER);
            middlePanel.validate();
            middlePanel.repaint();
            int delay = 40;
            final int [] index = { 0 }; // The ultimate hack to avoid final variable in lambda
            new javax.swing.Timer(delay, e -> {
                trophyLabel.setImage(images[index[0]]);
                trophyLabel.repaint();
                index[0]++;
                if (index[0] == 40) {
                    index[0] = 0;
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the leaderboard panel on the right
        JPanel leaderBoardPanel = createLeaderBoardPanel(category);
        blackScreen.add(leaderBoardPanel);

        blackScreen.revalidate();
    }

    /**
     * Customizes a label
     * 
     * @param label The label to customize
     * @param size  The size of the label
     * @param color The color of the label
     * @param style The style of the label
     */
    public void customizeLabel(JLabel label, int size, Color color, int style) {
        label.setFont(new Font("Arial", style, size));
        label.setForeground(color);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    }

    /**
     * Creates the ranking panel
     * 
     * @return The ranking panel
     */
    private JPanel createRankingPanel() {
        JPanel rankingPanel = new JPanel();
        rankingPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.Y_AXIS));
        rankingPanel.setOpaque(false);

        JLabel title = new JLabel("Classement");
        customizeLabel(title, 40, Color.WHITE, Font.BOLD);
        rankingPanel.add(title);

        for (int i = 0; i < ranking.size(); i++) {
            JLabel label = new JLabel((i + 1) + " - " + ranking.get(i).getKey() + " : " + ranking.get(i).getValue());
            customizeLabel(label, 25, Color.WHITE, Font.BOLD);
            rankingPanel.add(Box.createVerticalStrut(10));
            rankingPanel.add(label);
        }

        return rankingPanel;
    }

    /**
     * Creates the leaderboard panel
     * 
     * @param category The category of the leaderboard
     * @return The leaderboard panel
     */
    private JPanel createLeaderBoardPanel(String category) {
        LeaderBoard leaderBoard = new LeaderBoard();
        for (Pair<String, Integer> pair : ranking) {
            leaderBoard.addScore(category, pair.getValue(), pair.getKey());
        }
        LinkedHashMap<String, Integer> leaderBoardValues = leaderBoard.getScores(category);

        JPanel leaderBoardPanel = new JPanel();
        leaderBoardPanel.setLayout(new BoxLayout(leaderBoardPanel, BoxLayout.Y_AXIS));
        leaderBoardPanel.setOpaque(false);

        JLabel leaderBoardTitle = new JLabel("Meilleurs scores");
        customizeLabel(leaderBoardTitle, 40, Color.WHITE, Font.BOLD);
        leaderBoardPanel.add(leaderBoardTitle);

        for (String key : leaderBoardValues.keySet()) {
            JLabel label = new JLabel(key + " : " + leaderBoardValues.get(key));
            customizeLabel(label, 25, Color.WHITE, Font.BOLD);
            leaderBoardPanel.add(Box.createVerticalStrut(10)); // Add space
            leaderBoardPanel.add(label);
        }

        return leaderBoardPanel;
    }

    /**
     * Creates the button to return to the main menu
     * 
     * @return The button to return to the main menu
     */
    private JButton createButton() {
        return UIFactory.createStyledButton("Retour au menu principal",
                e -> App.getInstance().appState.changeState(StartState.getInstance()));
    }

    public void setRanking(List<Pair<String, Integer>> ranking) {
        this.ranking = ranking;
    }
}
