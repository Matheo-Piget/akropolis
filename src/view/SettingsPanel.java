package view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLayeredPane;
import java.awt.event.ActionListener;
import java.awt.Image;
import javax.swing.JPanel;
import model.LeaderBoard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.ImageIcon;
import java.util.Arrays;
import java.util.HashMap;

import util.SettingsParser;
import view.main.App;
import view.ui.UIFactory;

/**
 * In this panel, the user can change the settings of the game.
 * We can change the window size here.
 */
public class SettingsPanel extends JLayeredPane {
    private JComboBox<String> windowSize = new JComboBox<>();
    private JLabel backGroundLabel = new JLabel();
    private String[] WindowSizes = { "1200x560", "1350x630", "1500x700", "1650x770", "1800x840" };

    /**
     * Constructor for the SettingsPanel class.
     * Initializes the spinner.
     */
    @SuppressWarnings({ "removal" })
    public SettingsPanel(CardLayout switcher) {
        loadBackgroundImage();
        add(backGroundLabel, new Integer(0));
        int userWindowWidth = SettingsParser.getResolutionWidth();
        int userWindowHeight = SettingsParser.getResolutionHeight();
        backGroundLabel.setBounds(0, 0, userWindowWidth, userWindowHeight);
        // Handle the case where the user has a custom window size which is not in the
        // array we add it to the array
        String userWindowSize = userWindowWidth + "x" + userWindowHeight;
        if (!Arrays.asList(WindowSizes).contains(userWindowSize)) {
            String[] newWindowSizes = new String[WindowSizes.length + 1];
            System.arraycopy(WindowSizes, 0, newWindowSizes, 0, WindowSizes.length);
            newWindowSizes[WindowSizes.length] = userWindowSize;
            WindowSizes = newWindowSizes;
        }
        JPanel leaderBoardPanel = new JPanel();
        CardLayout leaderBoardLayout = new CardLayout();
        leaderBoardPanel.setLayout(leaderBoardLayout);
        JButton nextCategory = UIFactory.createStyledButton(">",new Dimension(50,100), new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                leaderBoardLayout.next(leaderBoardPanel);
            }
        });
        JButton previousCategory = UIFactory.createStyledButton("<",new Dimension(50,100), new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                leaderBoardLayout.previous(leaderBoardPanel);
            }
        });
        LeaderBoard leaderBoard = new LeaderBoard();
        JPanel singlePlayer = new JPanel(new BorderLayout());
        singlePlayer.setSize(userWindowWidth / 2, userWindowHeight);
        singlePlayer.add(new JLabel("Classement des scores en mode solo", JLabel.CENTER), BorderLayout.NORTH);
        singlePlayer.add(previousCategory, BorderLayout.WEST);
        singlePlayer.add(nextCategory, BorderLayout.EAST);
        JPanel singlePlayerScores = new JPanel(new GridLayout(5, 1));
        HashMap<String, Integer> singlePlayerScoresMap = leaderBoard.getScores("ONE_PLAYER");
        for (String playerName : singlePlayerScoresMap.keySet()) {
            JLabel scoreLabel = new JLabel(playerName + " : " + singlePlayerScoresMap.get(playerName), JLabel.CENTER);
            singlePlayerScores.add(scoreLabel);
        }
        singlePlayer.add(singlePlayerScores, BorderLayout.CENTER);
        singlePlayer.revalidate();
        JPanel twoPlayer = new JPanel(new BorderLayout());
        twoPlayer.setSize(userWindowWidth / 2, userWindowHeight);
        twoPlayer.add(new JLabel("Classement des scores en mode 2 joueurs", JLabel.CENTER), BorderLayout.NORTH);
        twoPlayer.add(previousCategory, BorderLayout.WEST);
        twoPlayer.add(nextCategory, BorderLayout.EAST);
        JPanel twoPlayerScores = new JPanel(new GridLayout(6, 1));
        HashMap<String, Integer> twoPlayerScoresMap = leaderBoard.getScores("TWO_PLAYER");
        for (String playerName : twoPlayerScoresMap.keySet()) {
            JLabel scoreLabel = new JLabel(playerName + " : " + twoPlayerScoresMap.get(playerName), JLabel.CENTER);
            twoPlayerScores.add(scoreLabel);
        }
        twoPlayer.add(twoPlayerScores, BorderLayout.CENTER);
        twoPlayer.revalidate();
        JPanel threePlayer = new JPanel(new BorderLayout());
        threePlayer.setSize(userWindowWidth / 2, userWindowHeight);
        threePlayer.add(new JLabel("Classement des scores en mode 3 joueurs", JLabel.CENTER), BorderLayout.NORTH);
        threePlayer.add(previousCategory, BorderLayout.WEST);
        threePlayer.add(nextCategory, BorderLayout.EAST);
        JPanel threePlayerScores = new JPanel(new GridLayout(6, 1));
        HashMap<String, Integer> threePlayerScoresMap = leaderBoard.getScores("THREE_PLAYER");
        for (String playerName : threePlayerScoresMap.keySet()) {
            JLabel scoreLabel = new JLabel(playerName + " : " + threePlayerScoresMap.get(playerName), JLabel.CENTER);
            threePlayerScores.add(scoreLabel);
        }
        threePlayer.add(threePlayerScores, BorderLayout.CENTER);
        threePlayer.revalidate();
        JPanel fourPlayer = new JPanel(new BorderLayout());
        fourPlayer.setSize(userWindowWidth / 2, userWindowHeight);
        fourPlayer.add(new JLabel("Classement des scores en mode 4 joueurs", JLabel.CENTER), BorderLayout.NORTH);
        fourPlayer.add(previousCategory, BorderLayout.WEST);
        fourPlayer.add(nextCategory, BorderLayout.EAST);
        JPanel fourPlayerScores = new JPanel(new GridLayout(6, 1));
        HashMap<String, Integer> fourPlayerScoresMap = leaderBoard.getScores("FOUR_PLAYER");
        for (String playerName : fourPlayerScoresMap.keySet()) {
            JLabel scoreLabel = new JLabel(playerName + " : " + fourPlayerScoresMap.get(playerName), JLabel.CENTER);
            fourPlayerScores.add(scoreLabel);
        }
        fourPlayer.add(fourPlayerScores, BorderLayout.CENTER);
        fourPlayer.revalidate();
        leaderBoardPanel.add(singlePlayer, "singlePlayer");
        leaderBoardPanel.add(twoPlayer, "twoPlayer");
        leaderBoardPanel.add(threePlayer, "threePlayer");
        leaderBoardPanel.add(fourPlayer, "fourPlayer");
        leaderBoardPanel.setBounds(userWindowWidth/2, 0, userWindowWidth / 2, userWindowHeight);
        windowSize = new JComboBox<>(WindowSizes);
        windowSize.setBounds(50, 100, 200, 30);
        windowSize.setSelectedItem(userWindowSize);
        windowSize.addActionListener(e -> {
            String[] size = windowSize.getSelectedItem().toString().split("x");
            SettingsParser.changeResolution(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
            App.getInstance().getScreen()
                    .setPreferredSize(new Dimension(Integer.parseInt(size[0]), Integer.parseInt(size[1])));
            // Recreate the background image
            loadBackgroundImage();
            App.getInstance().pack();
            leaderBoardPanel.setSize(App.getInstance().getScreen().getPreferredSize().width / 2,
                    App.getInstance().getScreen().getPreferredSize().height);
            leaderBoardPanel.revalidate();
            App.getInstance().revalidate();
            leaderBoardPanel.setLocation(App.getInstance().getScreen().getPreferredSize().width / 2, 0);
        });
        JLabel windowSizeLabel = new JLabel("Résolution de l'écran:");
        windowSizeLabel.setForeground(java.awt.Color.WHITE);
        windowSizeLabel.setBounds(50, 50, 200, 30);
        JButton leaderBoardResetButton = UIFactory.createStyledButton("Réinitialiser les scores", new Dimension(300, 30),
                new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        LeaderBoard leaderBoard = new LeaderBoard();
                        leaderBoard.resetLeaderBoard();
                    }
                });
        leaderBoardResetButton.setBounds(50, 200, 300, 30);
        JButton backButton = UIFactory.createStyledButton("Retour", new Dimension(300,30), new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                switcher.show(getParent(), "choicePanel");
            }
        });
        backButton.setBounds(50, 250, 300, 30);
        backButton.addActionListener(e -> {
            switcher.show(getParent(), "choicePanel");
        });
        add(windowSizeLabel, new Integer(1));
        add(windowSize, new Integer(1));
        add(leaderBoardResetButton, new Integer(1));
        add(backButton, new Integer(1));
        add(leaderBoardPanel, new Integer(1));
    }

    /**
     * Loads the background image of the settings panel.
     */
    private void loadBackgroundImage() {
        try {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/menu/settingsbg.gif"));
            Image image = imageIcon.getImage();
            Dimension screenSize = App.getInstance().getScreen().getPreferredSize();
            Image scaledImage = image.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_DEFAULT);
            backGroundLabel.setIcon(new ImageIcon(scaledImage));
            backGroundLabel.setSize(screenSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
