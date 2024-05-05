package view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JLayeredPane;
import java.awt.event.ActionListener;
import java.awt.Image;
import javax.swing.JPanel;
import model.LeaderBoard;
import java.awt.CardLayout;
import javax.swing.ImageIcon;
import java.util.Arrays;
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
        // It will have an arrow to display each leaderboard category
        leaderBoardPanel.setLayout(null);
        leaderBoardPanel.setBounds(0, 0, userWindowWidth, userWindowHeight);
        windowSize = new JComboBox<>(WindowSizes);
        windowSize.setBounds(50, 100, 200, 30);
        windowSize.setSelectedItem(userWindowSize);
        windowSize.addActionListener(e -> {
            String[] size = windowSize.getSelectedItem().toString().split("x");
            SettingsParser.changeResolution(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
            App.getInstance().getScreen()
                    .setPreferredSize(new Dimension(Integer.parseInt(size[0]), Integer.parseInt(size[1])));
            App.getInstance().pack();
            App.getInstance().revalidate();
            // Recreate the background image
            loadBackgroundImage();
        });
        JLabel windowSizeLabel = new JLabel("Résolution de l'écran:");
        windowSizeLabel.setForeground(java.awt.Color.WHITE);
        windowSizeLabel.setBounds(50, 50, 200, 30);
        JButton leaderBoardResetButton = UIFactory.createStyledButton("Réinitialiser les scores",
                new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        LeaderBoard leaderBoard = new LeaderBoard();
                        leaderBoard.resetLeaderBoard();
                    }
                });
        leaderBoardResetButton.setBounds(50, 200, 300, 30);
        JButton backButton = UIFactory.createStyledButton("Retour", new ActionListener() {
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
