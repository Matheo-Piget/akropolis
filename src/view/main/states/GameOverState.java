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
import java.awt.GridLayout;
import java.awt.Font;
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

        blackScreen.setLayout(new GridLayout(1, 3));

        // Récupération de la catégorie en fonction de la taille du classement
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
        middlePanel.setOpaque(false);
        middlePanel.setBackground(Color.BLACK);
        JButton button = createButton();

        middlePanel.add(button, BorderLayout.SOUTH);
        blackScreen.add(middlePanel);



        // Création du panneau des meilleurs scores à droite
        JPanel leaderBoardPanel = createLeaderBoardPanel(category);
        blackScreen.add(leaderBoardPanel);

        blackScreen.revalidate();
    }

    /**
     * Customizes a label
     * @param label The label to customize
     * @param size The size of the label
     * @param color The color of the label
     * @param style The style of the label
     */
    public void customizeLabel(JLabel label, int size, Color color, int style) {
        label.setFont(new Font("Arial", style, size));
        label.setForeground(color);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
    }

    /**
     * Creates the ranking panel
     * @return The ranking panel
     */
    private JPanel createRankingPanel() {
        JPanel rankingPanel = new JPanel(new GridLayout(ranking.size() + 1, 1));
        rankingPanel.setOpaque(false);

        JLabel title = new JLabel("Classement");
        customizeLabel(title, 40, Color.WHITE, Font.BOLD);
        rankingPanel.add(title);

        for (int i = 0; i < ranking.size(); i++) {
            JLabel label = new JLabel((i + 1) + " - " + ranking.get(i).getKey() + " : " + ranking.get(i).getValue());
            customizeLabel(label, 25, Color.WHITE, Font.BOLD);
            rankingPanel.add(label);
        }

        return rankingPanel;
    }

    // Création du panneau des meilleurs scores
    private JPanel createLeaderBoardPanel(String category) {
        LeaderBoard leaderBoard = new LeaderBoard();
        LinkedHashMap<String, Integer> leaderBoardValues = leaderBoard.getScores(category);

        JPanel leaderBoardPanel = new JPanel(new GridLayout(6, 1));
        leaderBoardPanel.setOpaque(false);

        JLabel leaderBoardTitle = new JLabel("Meilleurs scores");
        customizeLabel(leaderBoardTitle, 40, Color.WHITE, Font.BOLD);
        leaderBoardPanel.add(leaderBoardTitle);

        for (String key : leaderBoardValues.keySet()) {
            JLabel label = new JLabel(key + " : " + leaderBoardValues.get(key));
            customizeLabel(label, 25, Color.WHITE, Font.BOLD);
            leaderBoardPanel.add(label);
        }

        return leaderBoardPanel;
    }

    /**
     * Creates the button to return to the main menu
     * @return The button to return to the main menu
     */
    private JButton createButton() {
        return UIFactory.createStyledButton("Retour au menu principal",
                e -> App.getInstance().appState.changeState(StartState.getInstance()));
    }

    public void setRanking(List<Pair<String,Integer>> ranking) {
        this.ranking = ranking;
    }
}
