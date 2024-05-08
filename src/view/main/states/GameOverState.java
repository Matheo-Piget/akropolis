package view.main.states;

import util.State;
import util.Pair;
import model.LeaderBoard;
import util.Timeline;
import view.main.App;
import view.ui.UIFactory;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.event.KeyListener;

public class GameOverState extends State {

    private static final GameOverState INSTANCE = new GameOverState();
    private Timeline timeline;
    private JPanel blackScreen;
    private List<Pair<String,Integer>> ranking;

    public static GameOverState getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        System.out.println("Entering GameOver State");
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
        JLabel logo = new JLabel("Le Gagnant est : " + ranking.get(0).getKey() + " !");
        // White transparent color
        logo.setForeground(new java.awt.Color(255, 255, 255, 0));
        logo.setHorizontalAlignment(JLabel.CENTER);
        logo.setVerticalAlignment(JLabel.CENTER);
        blackScreen.add(logo, java.awt.BorderLayout.CENTER);
        blackScreen.revalidate();
        logo.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 25));
        timeline = new Timeline(0);
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
        System.out.println("Exiting Game Over State");
        // Remove all components from the screen
        App.getInstance().getScreen().removeAll();
    }

    /**
     * Handles the exit of the state
     */
    private void handleExit() {
        timeline.stop();
        blackScreen.removeAll();

        blackScreen.setLayout(new GridBagLayout());
        String category = "";
        switch (ranking.size()) {
            case 1:
                category = "ONE_PLAYER";
                break;
            case 2:
                category = "TWO_PLAYER";
                break;
            case 3:
                category = "THREE_PLAYER";
                break;
            default:
                category = "FOUR_PLAYER";
                break;
        }
        LeaderBoard leaderBoard = new LeaderBoard();
        // Write the new score to the file
        for (Pair<String, Integer> pair : ranking) {
            leaderBoard.addScore(category, pair.getValue(), pair.getKey());
        }

        JPanel rankingPanel = new JPanel(new GridLayout(ranking.size() + 1, 1));
        rankingPanel.setOpaque(false);
        JLabel title = new JLabel("Classement");
        title.setFont(new Font("Serif", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        rankingPanel.add(title);
        for (int i = 0; i < ranking.size(); i++) {
            JLabel label = new JLabel((i + 1) + " - " + ranking.get(i).getKey() + " : " + ranking.get(i).getValue());
            label.setFont(new Font("Serif", Font.BOLD, 25));
            label.setForeground(Color.WHITE);
            label.setOpaque(false);
            rankingPanel.add(label);
        }

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.insets = new Insets(10, 10, 10, 10);
        gbc2.anchor = GridBagConstraints.CENTER;

        blackScreen.add(rankingPanel, gbc2);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel leaderBoardPanel = new JPanel(new GridLayout(6, 1));
        leaderBoardPanel.setOpaque(false);
        JLabel leaderBoardTitle = new JLabel("Meilleurs scores");
        leaderBoardTitle.setFont(new Font("Serif", Font.BOLD, 40));
        leaderBoardTitle.setForeground(Color.WHITE);
        leaderBoardPanel.add(leaderBoardTitle);
        LinkedHashMap<String, Integer> leaderBoardValues = leaderBoard.getScores(category);
        for (String key : leaderBoardValues.keySet()) {
            JLabel label = new JLabel(key + " : " + leaderBoardValues.get(key));
            label.setFont(new Font("Serif", Font.BOLD, 25));
            label.setForeground(Color.WHITE);
            label.setOpaque(false);
            leaderBoardPanel.add(label);
        }
        blackScreen.add(leaderBoardPanel, gbc);

        JButton button = UIFactory.createStyledButton("Retour au menu principal",
                e -> App.getInstance().appState.changeState(StartState.getInstance()));
        // Calculte the size to allocate based on the text
        FontMetrics metrics = button.getFontMetrics(button.getFont());
        int width = metrics.stringWidth(button.getText()) + 20;
        int height = metrics.getHeight() + 10;
        button.setPreferredSize(new java.awt.Dimension(width, height));

        // Center the button
        gbc.anchor = GridBagConstraints.CENTER;
        blackScreen.add(button, gbc);

        blackScreen.revalidate();
    }

    public void setRanking(List<Pair<String,Integer>> ranking) {
        this.ranking = ranking;
    }
}
