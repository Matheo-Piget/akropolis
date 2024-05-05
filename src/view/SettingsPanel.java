package view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JLayeredPane;
import javax.swing.OverlayLayout;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.util.Arrays;
import util.SettingsParser;

/**
 * In this panel, the user can change the settings of the game.
 * We can change the window size here.
 */
public class SettingsPanel extends JLayeredPane {
    private JComboBox<String> windowSize = new JComboBox<>();
    private JLabel backGroundLabel = new JLabel();
    private String [] WindowSizes = {"800x600", "1024x768", "1500x700", "1280x1024", "1600x900", "1920x1080"};

    /**
     * Constructor for the SettingsPanel class.
     * Initializes the spinner.
     */
    public SettingsPanel() {
        loadBackgroundImage();
        int userWindowWidth = SettingsParser.getResolutionWidth();
        int userWindowHeight = SettingsParser.getResolutionHeight();
        backGroundLabel.setBounds(0,0, userWindowWidth, userWindowHeight);
        // Handle the case where the user has a custom window size which is not in the array we add it to the array
        String userWindowSize = userWindowWidth + "x" + userWindowHeight;
        if (!Arrays.asList(WindowSizes).contains(userWindowSize)) {
            String[] newWindowSizes = new String[WindowSizes.length + 1];
            System.arraycopy(WindowSizes, 0, newWindowSizes, 0, WindowSizes.length);
            newWindowSizes[WindowSizes.length] = userWindowSize;
            WindowSizes = newWindowSizes;
        }
        windowSize = new JComboBox<>(WindowSizes);
        windowSize.setSelectedItem(userWindowSize);
        JLabel windowSizeLabel = new JLabel("Résolution de l'écran:");
        windowSizeLabel.setBounds(50, 50, 200, 30);
        JLabel leaderBoardLabel = new JLabel("Tableau des scores:");
        leaderBoardLabel.setBounds(50, 100, 200, 30);
        JButton leaderBoardButton = new JButton("Voir le tableau des scores");
        leaderBoardButton.setBounds(50, 150, 200, 30);
        JLabel leaderBoardResetLabel = new JLabel("Réinitialiser le tableau des scores:");
        leaderBoardResetLabel.setBounds(50, 200, 200, 30);
        JButton leaderBoardResetButton = new JButton("Réinitialiser");
        leaderBoardResetButton.setBounds(50, 250, 200, 30);
        add(windowSizeLabel, new Integer(1));
        add(windowSize, new Integer(1));
        add(leaderBoardLabel, new Integer(1));
        add(leaderBoardButton,new Integer(1));
        add(leaderBoardResetLabel, new Integer(1));
        add(leaderBoardResetButton, new Integer(1));
    }

    private void loadBackgroundImage() {
        try {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/menu/settingsbg.gif"));
            Image image = imageIcon.getImage();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Image scaledImage = image.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_DEFAULT);
            backGroundLabel.setIcon(new ImageIcon(scaledImage));
            add(backGroundLabel, new Integer(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
